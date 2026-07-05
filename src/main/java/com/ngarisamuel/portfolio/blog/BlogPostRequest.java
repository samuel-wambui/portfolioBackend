package com.ngarisamuel.portfolio.blog;

import java.util.List;

public record BlogPostRequest(
        String portfolioId,
        String slug,
        String title,
        String excerpt,
        String publishedAt,
        String readTime,
        List<String> tags,
        String body
) {
}
