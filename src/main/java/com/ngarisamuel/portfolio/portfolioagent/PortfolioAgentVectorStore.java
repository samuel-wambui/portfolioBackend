package com.ngarisamuel.portfolio.portfolioagent;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class PortfolioAgentVectorStore {
    private static final TypeReference<Map<String, Object>> JSON_MAP = new TypeReference<>() {
    };

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public void ensureSchema() {
        jdbcTemplate.execute("create extension if not exists vector");
        jdbcTemplate.execute("""
                create table if not exists portfolio_knowledge_chunks (
                    id bigserial primary key,
                    portfolio_id varchar(32) not null,
                    source_type varchar(80) not null,
                    source_id varchar(80) not null,
                    chunk_key varchar(120) not null,
                    title varchar(255),
                    section varchar(120),
                    content text not null,
                    metadata jsonb not null default '{}',
                    embedding vector(1536),
                    embedding_model varchar(120),
                    content_hash varchar(64) not null,
                    deleted boolean not null default false,
                    created_at timestamp with time zone not null default now(),
                    updated_at timestamp with time zone not null default now(),
                    embedded_at timestamp with time zone,
                    unique (portfolio_id, source_type, source_id, chunk_key)
                )
                """);
        jdbcTemplate.execute("""
                create index if not exists idx_portfolio_knowledge_lookup
                on portfolio_knowledge_chunks (portfolio_id, source_type, deleted)
                """);
        jdbcTemplate.execute("""
                create index if not exists idx_portfolio_knowledge_metadata
                on portfolio_knowledge_chunks using gin (metadata)
                """);
        jdbcTemplate.execute("""
                create index if not exists idx_portfolio_knowledge_embedding
                on portfolio_knowledge_chunks
                using hnsw (embedding vector_cosine_ops)
                where deleted = false
                """);
    }

    public void markPortfolioDeleted(String portfolioId) {
        jdbcTemplate.update("""
                update portfolio_knowledge_chunks
                set deleted = true, updated_at = now()
                where portfolio_id = ?
                """, portfolioId);
    }

    public void upsertChunk(
            String portfolioId,
            PortfolioAgentChunk chunk,
            List<Double> embedding,
            String embeddingModel
    ) {
        jdbcTemplate.update("""
                        insert into portfolio_knowledge_chunks (
                            portfolio_id,
                            source_type,
                            source_id,
                            chunk_key,
                            title,
                            section,
                            content,
                            metadata,
                            embedding,
                            embedding_model,
                            content_hash,
                            deleted,
                            updated_at,
                            embedded_at
                        )
                        values (?, ?, ?, ?, ?, ?, ?, cast(? as jsonb), cast(? as vector), ?, ?, false, now(), now())
                        on conflict (portfolio_id, source_type, source_id, chunk_key)
                        do update set
                            title = excluded.title,
                            section = excluded.section,
                            content = excluded.content,
                            metadata = excluded.metadata,
                            embedding = excluded.embedding,
                            embedding_model = excluded.embedding_model,
                            content_hash = excluded.content_hash,
                            deleted = false,
                            updated_at = now(),
                            embedded_at = now()
                        """,
                portfolioId,
                chunk.sourceType(),
                chunk.sourceId(),
                chunk.chunkKey(),
                chunk.title(),
                chunk.section(),
                chunk.content(),
                toJson(chunk.metadata()),
                toVectorLiteral(embedding),
                embeddingModel,
                sha256(chunk.content())
        );
    }

    public List<PortfolioAgentSearchResult> search(
            String portfolioId,
            List<Double> queryEmbedding,
            String embeddingModel,
            int limit
    ) {
        return jdbcTemplate.query("""
                        select
                            source_type,
                            source_id,
                            chunk_key,
                            title,
                            section,
                            content,
                            metadata::text as metadata,
                            1 - (embedding <=> cast(? as vector)) as similarity
                        from portfolio_knowledge_chunks
                        where portfolio_id = ?
                          and deleted = false
                          and embedding is not null
                          and embedding_model = ?
                        order by embedding <=> cast(? as vector)
                        limit ?
                        """,
                (resultSet, rowNumber) -> new PortfolioAgentSearchResult(
                        resultSet.getString("source_type"),
                        resultSet.getString("source_id"),
                        resultSet.getString("chunk_key"),
                        resultSet.getString("title"),
                        resultSet.getString("section"),
                        resultSet.getString("content"),
                        fromJson(resultSet.getString("metadata")),
                        resultSet.getDouble("similarity")
                ),
                toVectorLiteral(queryEmbedding),
                portfolioId,
                embeddingModel,
                toVectorLiteral(queryEmbedding),
                limit
        );
    }

    private String toJson(Map<String, Object> value) {
        try {
            return objectMapper.writeValueAsString(value == null ? Map.of() : value);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize chunk metadata", exception);
        }
    }

    private Map<String, Object> fromJson(String value) {
        try {
            return objectMapper.readValue(value, JSON_MAP);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not parse chunk metadata", exception);
        }
    }

    private static String toVectorLiteral(List<Double> values) {
        return "[" + String.join(",", values.stream().map(String::valueOf).toList()) + "]";
    }

    private static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
