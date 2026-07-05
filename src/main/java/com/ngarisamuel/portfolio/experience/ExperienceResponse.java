package com.ngarisamuel.portfolio.experience;

import java.util.List;

public record ExperienceResponse(
        Long id,
        String portfolioId,
        String company,
        String role,
        String startDate,
        String endDate,
        boolean current,
        String description,
        Integer displayOrder,
        List<String> technologies
) {
    public static ExperienceResponse from(Experience experience) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getPortfolioId(),
                experience.getCompany(),
                experience.getRole(),
                textOrEmpty(experience.getStartDate()),
                textOrEmpty(experience.getEndDate()),
                experience.isCurrent(),
                experience.getDescription(),
                experience.getDisplayOrder(),
                experience.getTechnologies()
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
