package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.openai.ActiveOpenAiCredential;
import com.ngarisamuel.portfolio.openai.OpenAiCredentialService;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PortfolioAgentEmbeddingService {

    private final ObjectMapper objectMapper;
    private final OpenAiCredentialService credentialService;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${app.openai.api-key:}")
    private String apiKey;

    @Value("${app.openai.embeddings-url:https://api.openai.com/v1/embeddings}")
    private String embeddingsUrl;

    @Value("${app.openai.embedding-model:text-embedding-3-small}")
    private String embeddingModel;

    @Value("${app.openai.embedding-dimensions:1536}")
    private int embeddingDimensions;

    public String embeddingModel() {
        return activeCredential().embeddingModel();
    }

    public List<Double> embed(String input) {
        return embedAll(List.of(input)).get(0);
    }

    public List<List<Double>> embedAll(List<String> inputs) {
        ActiveOpenAiCredential credential = activeCredential();

        if (credential.apiKey() == null || credential.apiKey().isBlank()) {
            throw new IllegalStateException("OpenAI API key is required for portfolio semantic search");
        }

        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "model", credential.embeddingModel(),
                    "input", inputs,
                    "dimensions", credential.embeddingDimensions()
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(credential.endpointUrl()))
                    .header("Authorization", "Bearer " + credential.apiKey().trim())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("Embedding request failed with status " + response.statusCode());
            }

            JsonNode data = objectMapper.readTree(response.body()).path("data");

            if (!data.isArray() || data.size() != inputs.size()) {
                throw new IllegalStateException("Embedding response did not contain the expected embedding array");
            }

            List<List<Double>> embeddings = new ArrayList<>();

            for (JsonNode item : data) {
                JsonNode embedding = item.path("embedding");

                if (!embedding.isArray()) {
                    throw new IllegalStateException("Embedding response did not contain an embedding array");
                }

                embeddings.add(objectMapper.convertValue(
                        embedding,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class)
                ));
            }

            return embeddings;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not parse embedding response", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Embedding request was interrupted", exception);
        }
    }

    private ActiveOpenAiCredential activeCredential() {
        return credentialService.findActive()
                .orElseGet(() -> new ActiveOpenAiCredential(
                        apiKey,
                        embeddingsUrl,
                        embeddingModel,
                        embeddingDimensions
                ));
    }
}
