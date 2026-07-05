package com.ngarisamuel.portfolio.openai;

import java.time.Instant;

public record OpenAiCredentialResponse(
        Long id,
        String label,
        String endpointUrl,
        String embeddingModel,
        int embeddingDimensions,
        String maskedApiKey,
        boolean active,
        boolean expired,
        Instant createdAt,
        Instant updatedAt,
        Instant expiredAt,
        Instant deletedAt
) {
}
