package com.ngarisamuel.portfolio.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.contact.recipient-email:samuelngari13@gmail.com}")
    private String recipientEmail;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendContactMessage(ContactRequest request) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            throw new IllegalStateException("Contact recipient email is not configured");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail.trim());
        message.setFrom(fromEmail == null || fromEmail.isBlank() ? recipientEmail.trim() : fromEmail.trim());
        message.setReplyTo(request.email().trim());
        message.setSubject("Portfolio contact: " + request.subject().trim());
        message.setText(body(request));

        try {
            mailSender.send(message);
        } catch (MailException exception) {
            throw new IllegalStateException("Could not send contact email", exception);
        }
    }

    private String body(ContactRequest request) {
        return """
                New portfolio contact message

                Name: %s
                Email: %s
                Subject: %s

                Message:
                %s
                """.formatted(
                request.name().trim(),
                request.email().trim(),
                request.subject().trim(),
                request.message().trim()
        );
    }
}
