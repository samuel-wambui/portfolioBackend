package com.ngarisamuel.portfolio.blog;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<BlogPost> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);

    Optional<BlogPost> findBySlugAndPortfolioIdAndDeletedFalse(String slug, String portfolioId);
}
