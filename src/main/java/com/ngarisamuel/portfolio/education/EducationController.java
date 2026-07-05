package com.ngarisamuel.portfolio.education;

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
@RequestMapping("/api/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EducationResponse>>> getEducation(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Education retrieved", educationService.findAll(portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EducationResponse>> createEducation(@RequestBody EducationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Education created", educationService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EducationResponse>> updateEducation(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody EducationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Education updated", educationService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEducation(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        educationService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Education deleted", null));
    }
}
