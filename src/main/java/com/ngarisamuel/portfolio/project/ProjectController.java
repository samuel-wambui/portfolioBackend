package com.ngarisamuel.portfolio.project;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved", projectService.findAll(portfolioId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Project retrieved", projectService.findById(id, portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Project created", projectService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody ProjectRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Project updated", projectService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        projectService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Project deleted", null));
    }
}
