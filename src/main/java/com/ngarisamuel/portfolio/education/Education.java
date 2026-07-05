package com.ngarisamuel.portfolio.education;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.ngarisamuel.portfolio.common.PortfolioIds.DEFAULT_PORTFOLIO_ID;

@Entity
@Table(name = "education_items")
@Getter
@Setter
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(nullable = false)
    private String institution;

    @Column(nullable = false)
    private String course;

    @Column(nullable = false)
    private String grade;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
