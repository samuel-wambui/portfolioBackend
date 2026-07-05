package com.ngarisamuel.portfolio.certification;

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
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String issuer;

    @Column(name = "date_issued", nullable = false)
    private String dateIssued;

    @Column(name = "credential_url")
    private String credentialUrl;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
}
