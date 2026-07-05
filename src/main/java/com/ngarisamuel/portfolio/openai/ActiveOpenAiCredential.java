package com.ngarisamuel.portfolio.openai;

public record ActiveOpenAiCredential(
        String apiKey,
        String endpointUrl,
        String embeddingModel,
        int embeddingDimensions
) {
}
