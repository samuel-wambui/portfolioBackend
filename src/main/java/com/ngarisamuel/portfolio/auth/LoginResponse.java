package com.ngarisamuel.portfolio.auth;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String username,
        String firstName,
        String email,
        String lastName,
        Boolean loggedIn,
        List<String> roles,
        Boolean passwordChangeRequired
) {
}
