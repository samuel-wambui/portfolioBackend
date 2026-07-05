package com.ngarisamuel.portfolio.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, Instant expiresAt) {
        if (expiresAt.isAfter(Instant.now())) {
            blacklistedTokens.put(token, expiresAt);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        Instant expiresAt = blacklistedTokens.get(token);
        if (expiresAt == null) {
            return false;
        }

        if (expiresAt.isBefore(Instant.now())) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }
}
