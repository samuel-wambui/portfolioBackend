package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio-agent")
@RequiredArgsConstructor
public class PortfolioAgentController {

    private final PortfolioAgentService portfolioAgentService;
    private final PortfolioAgentN8nService portfolioAgentN8nService;

    @GetMapping("/snapshot")
    public ResponseEntity<ApiResponse<PortfolioAgentSnapshot>> getSnapshot(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent snapshot retrieved",
                portfolioAgentService.snapshot(portfolioId)
        ));
    }

    @GetMapping("/facts")
    public ResponseEntity<ApiResponse<PortfolioAgentFacts>> getFacts(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent facts retrieved",
                portfolioAgentService.facts(portfolioId)
        ));
    }

    @GetMapping("/chunks")
    public ResponseEntity<ApiResponse<List<PortfolioAgentChunk>>> getChunks(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent chunks retrieved",
                portfolioAgentService.chunks(portfolioId)
        ));
    }

    @GetMapping("/context")
    public ResponseEntity<ApiResponse<PortfolioAgentContextResponse>> getContext(
            @RequestParam(required = false) String portfolioId,
            @RequestParam(required = false, defaultValue = "16000") int maxChars
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent context retrieved",
                portfolioAgentService.context(portfolioId, maxChars)
        ));
    }

    @PostMapping("/reindex")
    public ResponseEntity<ApiResponse<PortfolioAgentReindexResponse>> reindex(
            @RequestBody(required = false) PortfolioAgentReindexRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent knowledge update requested",
                portfolioAgentN8nService.reindex(request)
        ));
    }

    @PostMapping({"", "/ask"})
    public ResponseEntity<ApiResponse<PortfolioAgentAskResponse>> ask(
            @RequestBody(required = false) PortfolioAgentAskRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio agent answered",
                portfolioAgentN8nService.ask(request)
        ));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PortfolioAgentSearchResponse>> search(
            @RequestBody PortfolioAgentSearchRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Portfolio semantic search completed",
                portfolioAgentService.search(request)
        ));
    }
}
