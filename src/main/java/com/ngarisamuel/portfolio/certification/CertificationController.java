package com.ngarisamuel.portfolio.certification;

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
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificationResponse>>> getCertifications(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Certifications retrieved", certificationService.findAll(portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CertificationResponse>> createCertification(@RequestBody CertificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Certification created", certificationService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificationResponse>> updateCertification(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody CertificationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Certification updated", certificationService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCertification(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        certificationService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Certification deleted", null));
    }
}
