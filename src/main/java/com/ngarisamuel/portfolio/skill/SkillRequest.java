package com.ngarisamuel.portfolio.skill;

public record SkillRequest(
        String portfolioId,
        String name,
        String category,
        String level
) {
}
