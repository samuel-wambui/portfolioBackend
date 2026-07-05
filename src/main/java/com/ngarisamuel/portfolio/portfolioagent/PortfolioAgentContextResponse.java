package com.ngarisamuel.portfolio.portfolioagent;

import java.util.List;

public record PortfolioAgentContextResponse(
        String portfolioId,
        String profileName,
        PortfolioAgentFacts facts,
        List<PortfolioAgentChunk> chunks,
        String context
) {
}
