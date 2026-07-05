package com.ngarisamuel.portfolio.config;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class CorsOrigins {

    private static final List<String> DEFAULT_ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "https://protfolio-frontend-liard.vercel.app"
    );

    private CorsOrigins() {
    }

    public static List<String> resolve(String configuredOrigins) {
        LinkedHashSet<String> origins = new LinkedHashSet<>(DEFAULT_ALLOWED_ORIGINS);

        if (configuredOrigins != null) {
            Arrays.stream(configuredOrigins.split(","))
                    .map(String::trim)
                    .filter(origin -> !origin.isBlank())
                    .forEach(origins::add);
        }

        return origins.stream().toList();
    }
}
