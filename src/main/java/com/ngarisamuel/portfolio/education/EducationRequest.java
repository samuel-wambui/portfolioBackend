package com.ngarisamuel.portfolio.education;

public record EducationRequest(
        String portfolioId,
        String institution,
        String course,
        String grade,
        String startDate,
        String endDate,
        String description,
        Integer displayOrder
) {
}
