package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio-agent")
@RequiredArgsConstructor
public class PortfolioAgentController {

    private final PortfolioAgentService portfolioAgentService;

    @GetMapping("/snapshot")
    public ResponseEntity<ApiResponse<PortfolioAgentSnapshot>> getSnapshot(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent snapshot retrieved",
                portfolioAgentService.snapshot(portfolioId)
        ));
    }
}
