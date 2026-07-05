package com.ngarisamuel.portfolio.portfolioagent;

import java.util.Map;

public record PortfolioAgentChunk(
        String sourceType,
        String sourceId,
        String chunkKey,
        String title,
        String section,
        String content,
        Map<String, Object> metadata
) {
}
