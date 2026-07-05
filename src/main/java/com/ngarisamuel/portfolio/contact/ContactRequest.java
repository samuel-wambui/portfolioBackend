package com.ngarisamuel.portfolio.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must be 120 characters or fewer")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 160, message = "Email must be 160 characters or fewer")
        String email,

        @NotBlank(message = "Subject is required")
        @Size(max = 180, message = "Subject must be 180 characters or fewer")
        String subject,

        @NotBlank(message = "Message is required")
        @Size(max = 4000, message = "Message must be 4000 characters or fewer")
        String message
) {
}
