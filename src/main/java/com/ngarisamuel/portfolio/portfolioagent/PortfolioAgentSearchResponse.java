package com.ngarisamuel.portfolio.portfolioagent;

import java.util.List;

public record PortfolioAgentSearchResponse(
        String portfolioId,
        String profileName,
        PortfolioAgentFacts facts,
        String query,
        String embeddingModel,
        List<PortfolioAgentSearchResult> results,
        String context
) {
}
