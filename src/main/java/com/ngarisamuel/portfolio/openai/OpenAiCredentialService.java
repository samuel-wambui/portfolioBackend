package com.ngarisamuel.portfolio.openai;

import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpenAiCredentialService {
    private static final String DEFAULT_ENDPOINT_URL = "https://api.openai.com/v1/embeddings";
    private static final String DEFAULT_EMBEDDING_MODEL = "text-embedding-3-small";
    private static final int DEFAULT_EMBEDDING_DIMENSIONS = 1536;
    private static final int GCM_TAG_BITS = 128;
    private static final int NONCE_BYTES = 12;

    private final OpenAiCredentialRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.secrets.encryption-key}")
    private String encryptionKey;

    public List<OpenAiCredentialResponse> findAll() {
        return repository.findByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(OpenAiCredentialService::toResponse)
                .toList();
    }

    public Optional<ActiveOpenAiCredential> findActive() {
        return repository.findFirstByActiveTrueAndExpiredFalseAndDeletedFalseOrderByUpdatedAtDesc()
                .map(credential -> new ActiveOpenAiCredential(
                        decrypt(credential.getApiKeyCiphertext(), credential.getApiKeyNonce()),
                        credential.getEndpointUrl(),
                        credential.getEmbeddingModel(),
                        credential.getEmbeddingDimensions()
                ));
    }

    @Transactional
    public OpenAiCredentialResponse create(OpenAiCredentialRequest request) {
        String apiKey = text(request == null ? "" : request.apiKey());
        String label = request == null ? "" : request.label();
        String endpointUrl = request == null ? "" : request.endpointUrl();
        String embeddingModel = request == null ? "" : request.embeddingModel();
        Integer embeddingDimensions = request == null ? null : request.embeddingDimensions();

        if (apiKey.isBlank()) {
            throw new IllegalArgumentException("OpenAI API key is required");
        }

        repository.findByDeletedFalseOrderByCreatedAtDesc().forEach(existing -> existing.setActive(false));

        EncryptedValue encryptedValue = encrypt(apiKey);
        OpenAiCredential credential = new OpenAiCredential();
        credential.setLabel(defaultText(label, "OpenAI embedding key"));
        credential.setEndpointUrl(defaultText(endpointUrl, DEFAULT_ENDPOINT_URL));
        credential.setEmbeddingModel(defaultText(embeddingModel, DEFAULT_EMBEDDING_MODEL));
        credential.setEmbeddingDimensions(embeddingDimensions == null || embeddingDimensions <= 0
                ? DEFAULT_EMBEDDING_DIMENSIONS
                : embeddingDimensions);
        credential.setApiKeyCiphertext(encryptedValue.ciphertext());
        credential.setApiKeyNonce(encryptedValue.nonce());
        credential.setApiKeyLastFour(lastFour(apiKey));
        credential.setActive(true);
        credential.setExpired(false);

        return toResponse(repository.save(credential));
    }

    @Transactional
    public OpenAiCredentialResponse activate(Long id) {
        OpenAiCredential credential = findExisting(id);
        repository.findByDeletedFalseOrderByCreatedAtDesc().forEach(existing -> existing.setActive(false));
        credential.setActive(true);
        credential.setExpired(false);
        credential.setExpiredAt(null);
        return toResponse(credential);
    }

    @Transactional
    public OpenAiCredentialResponse expire(Long id) {
        OpenAiCredential credential = findExisting(id);
        credential.setActive(false);
        credential.setExpired(true);
        credential.setExpiredAt(Instant.now());
        return toResponse(credential);
    }

    @Transactional
    public OpenAiCredentialResponse delete(Long id) {
        OpenAiCredential credential = findExisting(id);
        credential.setActive(false);
        credential.setDeleted(true);
        credential.setDeletedAt(Instant.now());
        return toResponse(credential);
    }

    private OpenAiCredential findExisting(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("OpenAI credential not found"));
    }

    private EncryptedValue encrypt(String value) {
        try {
            byte[] nonce = new byte[NONCE_BYTES];
            secureRandom.nextBytes(nonce);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey(), new GCMParameterSpec(GCM_TAG_BITS, nonce));

            return new EncryptedValue(
                    Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8))),
                    Base64.getEncoder().encodeToString(nonce)
            );
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not encrypt OpenAI credential", exception);
        }
    }

    private String decrypt(String ciphertext, String nonce) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey(),
                    new GCMParameterSpec(GCM_TAG_BITS, Base64.getDecoder().decode(nonce))
            );

            return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(
                    cipher.doFinal(Base64.getDecoder().decode(ciphertext))
            )).toString();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not decrypt OpenAI credential", exception);
        }
    }

    private SecretKeySpec secretKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new SecretKeySpec(digest.digest(encryptionKey.getBytes(StandardCharsets.UTF_8)), "AES");
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not create credential encryption key", exception);
        }
    }

    private static OpenAiCredentialResponse toResponse(OpenAiCredential credential) {
        return new OpenAiCredentialResponse(
                credential.getId(),
                credential.getLabel(),
                credential.getEndpointUrl(),
                credential.getEmbeddingModel(),
                credential.getEmbeddingDimensions(),
                "****" + credential.getApiKeyLastFour(),
                credential.isActive(),
                credential.isExpired(),
                credential.getCreatedAt(),
                credential.getUpdatedAt(),
                credential.getExpiredAt(),
                credential.getDeletedAt()
        );
    }

    private static String defaultText(String value, String fallback) {
        String normalized = text(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String text(String value) {
        return value == null ? "" : value.trim();
    }

    private static String lastFour(String value) {
        String normalized = text(value);
        return normalized.length() <= 4 ? normalized : normalized.substring(normalized.length() - 4);
    }

    private record EncryptedValue(String ciphertext, String nonce) {
    }
}
