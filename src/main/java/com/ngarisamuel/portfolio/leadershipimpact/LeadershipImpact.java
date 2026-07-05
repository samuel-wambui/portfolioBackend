package com.ngarisamuel.portfolio.leadershipimpact;

import static com.ngarisamuel.portfolio.common.PortfolioIds.DEFAULT_PORTFOLIO_ID;

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

@Entity
@Table(name = "leadership_impact_items")
@Getter
@Setter
@NoArgsConstructor
public class LeadershipImpact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "text")
    private String impact;

    @Column(name = "metric_value", nullable = false)
    private String metricValue;

    @Column(name = "metric_label", nullable = false)
    private String metricLabel;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "leadership_impact_tags", joinColumns = @JoinColumn(name = "leadership_impact_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "tag", nullable = false)
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
