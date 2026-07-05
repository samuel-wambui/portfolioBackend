package com.ngarisamuel.portfolio.profile;

import java.util.List;

public record ProfileRequest(
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
}
