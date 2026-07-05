package com.ngarisamuel.portfolio.blog;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    public List<BlogPostResponse> findAll(String portfolioId) {
        return blogPostRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by("publishedAt").descending())
                .stream()
                .map(BlogPostResponse::from)
                .toList();
    }

    public BlogPostResponse findBySlug(String slug, String portfolioId) {
        return blogPostRepository.findBySlugAndPortfolioIdAndDeletedFalse(slug, PortfolioIds.normalize(portfolioId))
                .map(BlogPostResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + slug));
    }

    public BlogPostResponse create(BlogPostRequest request) {
        BlogPost post = new BlogPost();
        applyRequest(post, request, PortfolioIds.normalize(request.portfolioId()));
        return BlogPostResponse.from(blogPostRepository.save(post));
    }

    public BlogPostResponse update(Long id, String portfolioId, BlogPostRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        BlogPost post = blogPostRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + id));
        applyRequest(post, request, normalizedPortfolioId);
        return BlogPostResponse.from(blogPostRepository.save(post));
    }

    public void delete(Long id, String portfolioId) {
        BlogPost post = blogPostRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + id));
        post.setDeleted(true);
        blogPostRepository.save(post);
    }

    private void applyRequest(BlogPost post, BlogPostRequest request, String portfolioId) {
        post.setPortfolioId(portfolioId);
        post.setSlug(textOrEmpty(request.slug()));
        post.setTitle(textOrEmpty(request.title()));
        post.setExcerpt(textOrEmpty(request.excerpt()));
        post.setPublishedAt(textOrEmpty(request.publishedAt()));
        post.setReadTime(textOrEmpty(request.readTime()));
        post.setTags(listOrEmpty(request.tags()));
        post.setBody(textOrEmpty(request.body()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static List<String> listOrEmpty(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }
}
