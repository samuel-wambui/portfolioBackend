package com.ngarisamuel.portfolio.leadershipimpact;

import java.util.List;

public record LeadershipImpactResponse(
        Long id,
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
    public static LeadershipImpactResponse from(LeadershipImpact item) {
        return new LeadershipImpactResponse(
                item.getId(),
                item.getPortfolioId(),
                textOrEmpty(item.getCategory()),
                textOrEmpty(item.getTitle()),
                textOrEmpty(item.getDescription()),
                textOrEmpty(item.getImpact()),
                textOrEmpty(item.getMetricValue()),
                textOrEmpty(item.getMetricLabel()),
                item.getDisplayOrder(),
                item.getTags()
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
