package com.ngarisamuel.portfolio.openai;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "openai_credentials")
public class OpenAiCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, length = 300)
    private String endpointUrl;

    @Column(nullable = false, length = 120)
    private String embeddingModel;

    @Column(nullable = false)
    private int embeddingDimensions;

    @Column(nullable = false, columnDefinition = "text")
    private String apiKeyCiphertext;

    @Column(nullable = false, length = 80)
    private String apiKeyNonce;

    @Column(nullable = false, length = 32)
    private String apiKeyLastFour;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean expired = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    private Instant expiredAt;

    private Instant deletedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
