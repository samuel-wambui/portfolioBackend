package com.ngarisamuel.portfolio.contact;

import com.ngarisamuel.portfolio.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMessage(@Valid @RequestBody ContactRequest request) {
        contactMessageService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Contact message received", null));
    }
}
