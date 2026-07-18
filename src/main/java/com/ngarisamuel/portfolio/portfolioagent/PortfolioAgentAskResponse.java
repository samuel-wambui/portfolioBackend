package com.ngarisamuel.portfolio.portfolioagent;

import java.util.List;

public record PortfolioAgentAskResponse(
        String answer,
        List<Object> sources
) {
}
