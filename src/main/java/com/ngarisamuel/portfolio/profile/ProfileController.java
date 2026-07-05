package com.ngarisamuel.portfolio.profile;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", profileService.findProfile(portfolioId)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestParam(required = false) String portfolioId,
            @RequestBody ProfileRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated", profileService.upsert(portfolioId, request)));
    }
}
