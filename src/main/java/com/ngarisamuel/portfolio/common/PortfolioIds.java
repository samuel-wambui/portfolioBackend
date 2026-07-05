package com.ngarisamuel.portfolio.common;

public final class PortfolioIds {

    public static final String DEFAULT_PORTFOLIO_ID = "PORT001";

    private PortfolioIds() {
    }

    public static String normalize(String portfolioId) {
        if (portfolioId == null || portfolioId.isBlank()) {
            return DEFAULT_PORTFOLIO_ID;
        }

        return portfolioId.trim().toUpperCase();
    }
}
