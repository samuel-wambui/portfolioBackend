package com.ngarisamuel.portfolio.blog;

import java.util.List;

public record BlogPostResponse(
        Long id,
        String portfolioId,
        String slug,
        String title,
        String excerpt,
        String publishedAt,
        String readTime,
        List<String> tags,
        String body
) {
    public static BlogPostResponse from(BlogPost post) {
        return new BlogPostResponse(
                post.getId(),
                post.getPortfolioId(),
                post.getSlug(),
                post.getTitle(),
                post.getExcerpt(),
                textOrEmpty(post.getPublishedAt()),
                post.getReadTime(),
                post.getTags(),
                post.getBody()
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
