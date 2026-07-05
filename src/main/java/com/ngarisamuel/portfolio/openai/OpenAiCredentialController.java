package com.ngarisamuel.portfolio.openai;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai-credentials")
@RequiredArgsConstructor
public class OpenAiCredentialController {

    private final OpenAiCredentialService credentialService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpenAiCredentialResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(
                "OpenAI credentials retrieved",
                credentialService.findAll()
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OpenAiCredentialResponse>> create(
            @RequestBody OpenAiCredentialRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "OpenAI credential saved",
                credentialService.create(request)
        ));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<OpenAiCredentialResponse>> activate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "OpenAI credential activated",
                credentialService.activate(id)
        ));
    }

    @PatchMapping("/{id}/expire")
    public ResponseEntity<ApiResponse<OpenAiCredentialResponse>> expire(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "OpenAI credential marked expired",
                credentialService.expire(id)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<OpenAiCredentialResponse>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "OpenAI credential deleted",
                credentialService.delete(id)
        ));
    }
}
