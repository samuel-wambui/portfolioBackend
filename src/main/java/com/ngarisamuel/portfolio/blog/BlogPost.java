package com.ngarisamuel.portfolio.blog;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.ngarisamuel.portfolio.common.PortfolioIds.DEFAULT_PORTFOLIO_ID;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String excerpt;

    @Column(name = "published_at", nullable = false)
    private String publishedAt;

    @Column(name = "read_time", nullable = false)
    private String readTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "blog_tags", joinColumns = @JoinColumn(name = "blog_post_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "tag", nullable = false)
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "text")
    private String body;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
