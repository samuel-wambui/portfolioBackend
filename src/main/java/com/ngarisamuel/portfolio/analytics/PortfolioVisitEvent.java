package com.ngarisamuel.portfolio.analytics;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "portfolio_visit_events",
        indexes = {
                @Index(name = "idx_visit_events_created_at", columnList = "created_at"),
                @Index(name = "idx_visit_events_event_type", columnList = "event_type"),
                @Index(name = "idx_visit_events_path", columnList = "path")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PortfolioVisitEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_id", length = 80)
    private String visitorId;

    @Column(name = "event_type", nullable = false, length = 40)
    private String eventType;

    @Column(name = "page_url", columnDefinition = "text")
    private String pageUrl;

    @Column(length = 500)
    private String path;

    @Column(name = "page_title", length = 300)
    private String pageTitle;

    @Column(columnDefinition = "text")
    private String referrer;

    @Column(name = "target_url", columnDefinition = "text")
    private String targetUrl;

    @Column(name = "target_label", length = 300)
    private String targetLabel;

    @Column(name = "device_type", length = 40)
    private String deviceType;

    @Column(length = 80)
    private String browser;

    @Column(length = 80)
    private String os;

    @Column(length = 40)
    private String language;

    @Column(length = 80)
    private String timezone;

    @Column(name = "screen_size", length = 40)
    private String screenSize;

    @Column(name = "utm_source", length = 120)
    private String utmSource;

    @Column(name = "utm_medium", length = 120)
    private String utmMedium;

    @Column(name = "utm_campaign", length = 160)
    private String utmCampaign;

    @Column(name = "ip_address", length = 120)
    private String ipAddress;

    @Column(length = 80)
    private String country;

    @Column(length = 120)
    private String region;

    @Column(length = 120)
    private String city;

    @Column(length = 40)
    private String latitude;

    @Column(length = 40)
    private String longitude;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
