package com.ngarisamuel.portfolio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtBlacklistService jwtBlacklistService;

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.access-token-ttl-ms}")
    private long accessTokenTtlMs;

    @Value("${app.jwt.refresh-token-ttl-ms}")
    private long refreshTokenTtlMs;

    public JwtService(JwtBlacklistService jwtBlacklistService) {
        this.jwtBlacklistService = jwtBlacklistService;
    }

    public String generateToken(String username, List<String> authorities, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);
        claims.put("roles", roles);
        claims.put("role", roles);
        return buildToken(username, claims, accessTokenTtlMs);
    }

    public String generateRefreshToken(String username, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);
        return buildToken(username, claims, refreshTokenTtlMs);
    }

    public boolean validateRefreshToken(String token) {
        return !isTokenExpired(token) && !jwtBlacklistService.isTokenBlacklisted(token);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Instant extractExpirationInstant(String token) {
        return extractExpiration(token).toInstant();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !jwtBlacklistService.isTokenBlacklisted(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private String buildToken(String username, Map<String, Object> claims, long ttlMs) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(ttlMs)))
                .signWith(signingKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
