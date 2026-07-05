package com.ngarisamuel.portfolio.project;

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
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String problem;

    @Column(nullable = false, columnDefinition = "text")
    private String architecture;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screenshots", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "screenshot", nullable = false)
    private List<String> screenshots = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "technology", nullable = false)
    private List<String> technologies = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_challenges", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "challenge", nullable = false, columnDefinition = "text")
    private List<String> challenges = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_results", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "result", nullable = false, columnDefinition = "text")
    private List<String> results = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_lessons_learned", joinColumns = @JoinColumn(name = "project_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "lesson", nullable = false, columnDefinition = "text")
    private List<String> lessonsLearned = new ArrayList<>();

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "live_demo_url")
    private String liveDemoUrl;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
