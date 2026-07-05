package com.ngarisamuel.portfolio.auth;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResult<LoginResponse> result = authService.login(request);
        return ResponseEntity.status(result.status()).body(result.body());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        AuthResult<LoginResponse> result = authService.refreshToken(request);
        return ResponseEntity.status(result.status()).body(result.body());
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<ApiResponse<Void>> logout(
            @PathVariable String username,
            HttpServletRequest request
    ) {
        AuthResult<Void> result = authService.logout(username, request);
        return ResponseEntity.status(result.status()).body(result.body());
    }
}
