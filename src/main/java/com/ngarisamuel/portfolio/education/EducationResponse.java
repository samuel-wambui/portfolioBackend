package com.ngarisamuel.portfolio.education;

public record EducationResponse(
        Long id,
        String portfolioId,
        String institution,
        String course,
        String grade,
        String startDate,
        String endDate,
        String description,
        Integer displayOrder
) {
    public static EducationResponse from(Education education) {
        return new EducationResponse(
                education.getId(),
                education.getPortfolioId(),
                education.getInstitution(),
                education.getCourse(),
                education.getGrade(),
                textOrEmpty(education.getStartDate()),
                textOrEmpty(education.getEndDate()),
                education.getDescription(),
                education.getDisplayOrder()
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
