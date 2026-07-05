package com.ngarisamuel.portfolio.portfolioagent;

public record PortfolioAgentSearchRequest(
        String portfolioId,
        String query,
        Integer limit,
        Integer maxContextChars
) {
}
