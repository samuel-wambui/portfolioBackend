package com.ngarisamuel.portfolio.profile;

import java.util.List;

public record ProfileResponse(
        Long id,
        String portfolioId,
        String fullName,
        String title,
        List<String> roles,
        List<String> specializedIn,
        List<String> currentlyFocusedOn,
        String heroText,
        String summary,
        String email,
        String phone,
        String location,
        String githubUrl,
        String linkedinUrl,
        String portfolioUrl,
        String cvUrl,
        String photoUrl,
        Integer projectsCompleted,
        Integer yearsExperience,
        String learningLabel,
        String collaborationLabel
) {
    public static ProfileResponse from(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getPortfolioId(),
                profile.getFullName(),
                profile.getTitle(),
                profile.getRoles(),
                profile.getSpecializedIn(),
                profile.getCurrentlyFocusedOn(),
                profile.getHeroText(),
                profile.getSummary(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getGithubUrl(),
                profile.getLinkedinUrl(),
                profile.getPortfolioUrl(),
                profile.getCvUrl(),
                profile.getPhotoUrl(),
                profile.getProjectsCompleted(),
                profile.getYearsExperience(),
                profile.getLearningLabel(),
                profile.getCollaborationLabel()
        );
    }
}
