package com.ngarisamuel.portfolio.skill;

public record SkillResponse(
        Long id,
        String portfolioId,
        String name,
        String category,
        String level
) {
    public static SkillResponse from(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getPortfolioId(),
                skill.getName(),
                skill.getCategory(),
                skill.getLevel()
        );
    }
}
