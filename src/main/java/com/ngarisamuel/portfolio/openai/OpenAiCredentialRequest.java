package com.ngarisamuel.portfolio.openai;

public record OpenAiCredentialRequest(
        String label,
        String apiKey,
        String endpointUrl,
        String embeddingModel,
        Integer embeddingDimensions
) {
}
