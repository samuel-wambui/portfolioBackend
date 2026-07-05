package com.ngarisamuel.portfolio.openai;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenAiCredentialRepository extends JpaRepository<OpenAiCredential, Long> {
    List<OpenAiCredential> findByDeletedFalseOrderByCreatedAtDesc();

    Optional<OpenAiCredential> findByIdAndDeletedFalse(Long id);

    Optional<OpenAiCredential> findFirstByActiveTrueAndExpiredFalseAndDeletedFalseOrderByUpdatedAtDesc();
}
