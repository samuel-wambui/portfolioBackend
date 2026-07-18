package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class PortfolioAgentN8nService {
    private static final String DEFAULT_ASK_WEBHOOK_URL = "https://n8n.digitalbank365.com/webhook/portfolio-agent-ask";
    private static final String DEFAULT_REINDEX_WEBHOOK_URL = "https://n8n.digitalbank365.com/webhook/portfolio-agent-reindex";
    private static final TypeReference<List<Object>> SOURCE_LIST_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    private final String askWebhookUrl;
    private final String reindexWebhookUrl;

    public PortfolioAgentN8nService(
            ObjectMapper objectMapper,
            @Value("${app.portfolio-agent.n8n.ask-webhook-url:" + DEFAULT_ASK_WEBHOOK_URL + "}") String askWebhookUrl,
            @Value("${app.portfolio-agent.n8n.reindex-webhook-url:" + DEFAULT_REINDEX_WEBHOOK_URL + "}") String reindexWebhookUrl
    ) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
        this.askWebhookUrl = askWebhookUrl;
        this.reindexWebhookUrl = reindexWebhookUrl;
    }

    public PortfolioAgentAskResponse ask(PortfolioAgentAskRequest request) {
        String question = firstText(request == null ? null : request.question(), request == null ? null : request.query());

        if (question.isBlank()) {
            throw new IllegalArgumentException("Question is required");
        }

        JsonNode payload = postJson(askWebhookUrl, Map.of(
                "portfolioId", PortfolioIds.normalize(request == null ? null : request.portfolioId()),
                "question", question
        ));
        JsonNode root = rootPayload(payload);
        String answer = firstText(
                textAt(root, "answer"),
                textAt(root, "output"),
                textAt(root, "message"),
                textAt(root.path("data"), "answer"),
                textAt(root.path("data"), "output"),
                textAt(root.path("data"), "message"),
                root.isTextual() ? root.asText() : ""
        );

        return new PortfolioAgentAskResponse(answer, sources(root));
    }

    public PortfolioAgentReindexResponse reindex(PortfolioAgentReindexRequest request) {
        String portfolioId = PortfolioIds.normalize(request == null ? null : request.portfolioId());
        JsonNode payload = postJson(reindexWebhookUrl, Map.of("portfolioId", portfolioId));
        JsonNode root = rootPayload(payload);

        return new PortfolioAgentReindexResponse(
                firstText(textAt(root, "portfolioId"), textAt(root.path("data"), "portfolioId"), portfolioId),
                firstText(textAt(root, "embeddingModel"), textAt(root.path("data"), "embeddingModel")),
                firstInt(root, "chunksIndexed", "indexedChunks", "count")
        );
    }

    private JsonNode postJson(String webhookUrl, Map<String, String> body) {
        try {
            return restClient
                    .post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientResponseException error) {
            String message = errorMessage(error.getResponseBodyAsString());
            throw new IllegalStateException(message.isBlank() ? "Portfolio agent webhook request failed" : message, error);
        } catch (RestClientException error) {
            throw new IllegalStateException("Portfolio agent webhook is not available right now", error);
        }
    }

    private String errorMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }

        try {
            JsonNode root = rootPayload(objectMapper.readTree(responseBody));
            return firstText(textAt(root, "message"), textAt(root, "error"));
        } catch (Exception ignored) {
            return responseBody;
        }
    }

    private JsonNode rootPayload(JsonNode payload) {
        if (payload == null || payload.isNull()) {
            return objectMapper.createObjectNode();
        }

        if (payload.isArray() && !payload.isEmpty()) {
            return payload.get(0);
        }

        return payload;
    }

    private List<Object> sources(JsonNode root) {
        JsonNode sources = root.path("sources");

        if (sources.isMissingNode() || sources.isNull()) {
            sources = root.path("data").path("sources");
        }

        if (!sources.isArray()) {
            return Collections.emptyList();
        }

        return objectMapper.convertValue(sources, SOURCE_LIST_TYPE);
    }

    private int firstInt(JsonNode root, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode field = root.path(fieldName);

            int value = intValue(field);

            if (value >= 0) {
                return value;
            }
        }

        JsonNode data = root.path("data");

        for (String fieldName : fieldNames) {
            JsonNode field = data.path(fieldName);

            int value = intValue(field);

            if (value >= 0) {
                return value;
            }
        }

        return 0;
    }

    private int intValue(JsonNode value) {
        if (value.isNumber()) {
            return value.asInt();
        }

        if (!value.isTextual()) {
            return -1;
        }

        try {
            return Integer.parseInt(value.asText().trim());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    private String textAt(JsonNode root, String fieldName) {
        JsonNode value = root.path(fieldName);
        return value.isTextual() ? value.asText().trim() : "";
    }

    private String firstText(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isBlank()) {
                return value.trim();
            }
        }

        return "";
    }
}
