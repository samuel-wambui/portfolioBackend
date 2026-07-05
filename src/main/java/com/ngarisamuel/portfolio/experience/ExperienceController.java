package com.ngarisamuel.portfolio.experience;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExperienceResponse>>> getExperience(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Experience retrieved", experienceService.findAll(portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExperienceResponse>> createExperience(@RequestBody ExperienceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Experience created", experienceService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExperienceResponse>> updateExperience(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody ExperienceRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Experience updated", experienceService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        experienceService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Experience deleted", null));
    }
}
