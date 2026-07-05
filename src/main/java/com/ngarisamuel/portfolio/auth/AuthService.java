package com.ngarisamuel.portfolio.auth;

import com.ngarisamuel.portfolio.common.api.ApiCode;
import com.ngarisamuel.portfolio.common.api.ApiResponse;
import com.ngarisamuel.portfolio.security.JwtBlacklistService;
import com.ngarisamuel.portfolio.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;

    public AuthResult<LoginResponse> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            AdminUser adminUser = adminUserRepository.findByUsername(request.username())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            adminUser.setLoggedIn(true);
            adminUser.setLastLoginAt(Instant.now());
            adminUserRepository.save(adminUser);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> authorities = authorityNames(userDetails);
            List<String> roles = List.of(adminUser.getRole());
            String accessToken = jwtService.generateToken(adminUser.getUsername(), authorities, roles);
            String refreshToken = jwtService.generateRefreshToken(adminUser.getUsername(), authorities);

            LoginResponse response = toLoginResponse(adminUser, accessToken, refreshToken, roles);
            return success("Login successful", response);
        } catch (LockedException exception) {
            return failure(HttpStatus.LOCKED, "Account is locked");
        } catch (DisabledException exception) {
            return failure(HttpStatus.FORBIDDEN, "Account is disabled");
        } catch (BadCredentialsException exception) {
            return failure(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    public AuthResult<LoginResponse> refreshToken(TokenRefreshRequest request) {
        try {
            if (!jwtService.validateRefreshToken(request.refreshToken())) {
                return failure(HttpStatus.UNAUTHORIZED, "Refresh token expired");
            }

            String username = jwtService.extractUserName(request.refreshToken());
            AdminUser adminUser = adminUserRepository.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            if (!adminUser.isEnabled()) {
                return failure(HttpStatus.FORBIDDEN, "Account is disabled");
            }

            if (adminUser.isLocked()) {
                return failure(HttpStatus.LOCKED, "Account is locked");
            }

            List<String> roles = List.of(adminUser.getRole());
            List<String> authorities = roles;
            String accessToken = jwtService.generateToken(adminUser.getUsername(), authorities, roles);
            String refreshToken = jwtService.generateRefreshToken(adminUser.getUsername(), authorities);

            adminUser.setLoggedIn(true);
            adminUserRepository.save(adminUser);

            return success("Token refreshed", toLoginResponse(adminUser, accessToken, refreshToken, roles));
        } catch (RuntimeException exception) {
            return failure(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    public AuthResult<Void> logout(String username, HttpServletRequest request) {
        adminUserRepository.findByUsername(username).ifPresent(adminUser -> {
            adminUser.setLoggedIn(false);
            adminUserRepository.save(adminUser);
        });

        String token = bearerToken(request);
        if (token != null) {
            jwtBlacklistService.blacklist(token, jwtService.extractExpirationInstant(token));
        }

        return new AuthResult<>(
                HttpStatus.OK,
                ApiResponse.success("Logout successful", null)
        );
    }

    private static List<String> authorityNames(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .distinct()
                .toList();
    }

    private static String bearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7).trim();
    }

    private static LoginResponse toLoginResponse(
            AdminUser adminUser,
            String accessToken,
            String refreshToken,
            List<String> roles
    ) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                adminUser.getUsername(),
                adminUser.getFirstName(),
                adminUser.getEmail(),
                adminUser.getLastName(),
                adminUser.isLoggedIn(),
                roles,
                false
        );
    }

    private static AuthResult<LoginResponse> success(String message, LoginResponse response) {
        return new AuthResult<>(HttpStatus.OK, ApiResponse.success(message, response));
    }

    private static AuthResult<LoginResponse> failure(HttpStatus status, String message) {
        String code = status == HttpStatus.FORBIDDEN ? ApiCode.FORBIDDEN : ApiCode.UNAUTHORIZED;
        return new AuthResult<>(status, ApiResponse.failure(message, code, null));
    }
}
