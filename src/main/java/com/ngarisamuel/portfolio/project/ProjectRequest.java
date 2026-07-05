package com.ngarisamuel.portfolio.project;

import java.util.List;

public record ProjectRequest(
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
}
