package com.ngarisamuel.portfolio.leadershipimpact;

import java.util.List;

public record LeadershipImpactRequest(
        String portfolioId,
        String category,
        String title,
        String description,
        String impact,
        String metricValue,
        String metricLabel,
        Integer displayOrder,
        List<String> tags
) {
}
