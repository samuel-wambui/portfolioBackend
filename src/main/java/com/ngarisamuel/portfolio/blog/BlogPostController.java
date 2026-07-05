package com.ngarisamuel.portfolio.blog;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getPosts(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Blog posts retrieved", blogPostService.findAll(portfolioId)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BlogPostResponse>> getPost(
            @PathVariable String slug,
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Blog post retrieved", blogPostService.findBySlug(slug, portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BlogPostResponse>> createPost(@RequestBody BlogPostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Blog post created", blogPostService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPostResponse>> updatePost(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody BlogPostRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Blog post updated", blogPostService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        blogPostService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Blog post deleted", null));
    }
}
