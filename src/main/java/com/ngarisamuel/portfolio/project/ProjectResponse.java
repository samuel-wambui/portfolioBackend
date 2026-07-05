package com.ngarisamuel.portfolio.project;

import java.util.List;

public record ProjectResponse(
        Long id,
        String portfolioId,
        String title,
        String problem,
        String architecture,
        Integer displayOrder,
        List<String> screenshots,
        List<String> technologies,
        List<String> challenges,
        List<String> results,
        List<String> lessonsLearned,
        String githubUrl,
        String liveDemoUrl
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getPortfolioId(),
                project.getTitle(),
                project.getProblem(),
                project.getArchitecture(),
                project.getDisplayOrder(),
                project.getScreenshots(),
                project.getTechnologies(),
                project.getChallenges(),
                project.getResults(),
                project.getLessonsLearned(),
                project.getGithubUrl(),
                project.getLiveDemoUrl()
        );
    }
}
