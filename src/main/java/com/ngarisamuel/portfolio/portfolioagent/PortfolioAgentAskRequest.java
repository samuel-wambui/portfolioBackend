package com.ngarisamuel.portfolio.portfolioagent;

public record PortfolioAgentAskRequest(
        String portfolioId,
        String question,
        String query
) {
}
