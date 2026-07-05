package com.ngarisamuel.portfolio.skill;

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
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getSkills(
            @RequestParam(required = false) String portfolioId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Skills retrieved", skillService.findAll(portfolioId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(@RequestBody SkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Skill created", skillService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId,
            @RequestBody SkillRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Skill updated", skillService.update(id, portfolioId, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(
            @PathVariable Long id,
            @RequestParam(required = false) String portfolioId
    ) {
        skillService.delete(id, portfolioId);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted", null));
    }
}
