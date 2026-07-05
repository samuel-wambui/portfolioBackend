package com.ngarisamuel.portfolio.portfolioagent;

public record PortfolioAgentReindexResponse(
        String portfolioId,
        String embeddingModel,
        int chunksIndexed
) {
}
