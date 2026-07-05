create extension if not exists vector;

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
);

create index if not exists idx_portfolio_knowledge_lookup
on portfolio_knowledge_chunks (portfolio_id, source_type, deleted);

create index if not exists idx_portfolio_knowledge_metadata
on portfolio_knowledge_chunks using gin (metadata);

create index if not exists idx_portfolio_knowledge_embedding
on portfolio_knowledge_chunks
using hnsw (embedding vector_cosine_ops)
where deleted = false;

create or replace view portfolio_agent_facts as
select
    p.portfolio_id,
    p.full_name,
    p.title,
    (
        select count(*)
        from certifications c
        where c.portfolio_id = p.portfolio_id
          and c.deleted = false
    ) as certification_count,
    (
        select count(*)
        from projects pr
        where pr.portfolio_id = p.portfolio_id
          and pr.deleted = false
    ) as project_count,
    (
        select count(*)
        from experience_items e
        where e.portfolio_id = p.portfolio_id
          and e.deleted = false
    ) as experience_count,
    (
        select count(*)
        from education_items ed
        where ed.portfolio_id = p.portfolio_id
          and ed.deleted = false
    ) as education_count,
    (
        select count(*)
        from skills s
        where s.portfolio_id = p.portfolio_id
          and s.deleted = false
    ) as skill_count,
    (
        select count(*)
        from leadership_impact_items li
        where li.portfolio_id = p.portfolio_id
          and li.deleted = false
    ) as leadership_impact_count,
    (
        select count(*)
        from blog_posts b
        where b.portfolio_id = p.portfolio_id
          and b.deleted = false
    ) as blog_post_count
from profiles p;
