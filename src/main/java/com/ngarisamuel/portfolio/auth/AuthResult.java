package com.ngarisamuel.portfolio.auth;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import org.springframework.http.HttpStatus;

public record AuthResult<T>(HttpStatus status, ApiResponse<T> body) {
}
