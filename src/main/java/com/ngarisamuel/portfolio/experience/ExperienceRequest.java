package com.ngarisamuel.portfolio.experience;

import java.util.List;

public record ExperienceRequest(
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
}
