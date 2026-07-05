package com.ngarisamuel.portfolio.analytics;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/events")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createEvent(
            @Valid @RequestBody AnalyticsEventRequest request,
            HttpServletRequest servletRequest
    ) {
        analyticsService.record(request, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Analytics event recorded", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnalyticsEventResponse>>> getEvents(
            @RequestParam(defaultValue = "100") Integer limit
    ) {
        return ResponseEntity.ok(ApiResponse.success("Analytics events retrieved", analyticsService.findRecent(limit)));
    }
}
