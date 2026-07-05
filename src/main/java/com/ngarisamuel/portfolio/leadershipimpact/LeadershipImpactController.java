package com.ngarisamuel.portfolio.leadershipimpact;

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
@RequestMapping("/api/leadership-impact")
@RequiredArgsConstructor
public class LeadershipImpactController {

    private final LeadershipImpactService leadershipImpactService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeadershipImpactResponse>>> getLeadershipImpact(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Leadership impact items retrieved", leadershipImpactService.findAll(portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LeadershipImpactResponse>> createLeadershipImpactItem(
            @RequestBody LeadershipImpactRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Leadership impact item created", leadershipImpactService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadershipImpactResponse>> updateLeadershipImpactItem(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody LeadershipImpactRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Leadership impact item updated", leadershipImpactService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeadershipImpactItem(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        leadershipImpactService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Leadership impact item deleted", null));
    }
}
