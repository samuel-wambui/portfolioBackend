package com.ngarisamuel.portfolio.certification;

public record CertificationResponse(
        Long id,
        String portfolioId,
        String name,
        String issuer,
        String dateIssued,
        String credentialUrl
) {
    public static CertificationResponse from(Certification certification) {
        return new CertificationResponse(
                certification.getId(),
                certification.getPortfolioId(),
                certification.getName(),
                certification.getIssuer(),
                textOrEmpty(certification.getDateIssued()),
                certification.getCredentialUrl()
        );
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
