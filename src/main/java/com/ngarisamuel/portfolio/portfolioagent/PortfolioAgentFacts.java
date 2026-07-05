package com.ngarisamuel.portfolio.portfolioagent;

public record PortfolioAgentFacts(
        String portfolioId,
        String fullName,
        String title,
        int certificationCount,
        int projectCount,
        int experienceCount,
        int educationCount,
        int skillCount,
        int leadershipImpactCount,
        int blogPostCount
) {
}
