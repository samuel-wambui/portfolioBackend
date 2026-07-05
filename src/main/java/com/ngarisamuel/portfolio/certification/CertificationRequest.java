package com.ngarisamuel.portfolio.certification;

public record CertificationRequest(
        String portfolioId,
        String name,
        String issuer,
        String dateIssued,
        String credentialUrl
) {
}
