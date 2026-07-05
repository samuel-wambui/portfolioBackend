package com.ngarisamuel.portfolio.analytics;

import java.time.Instant;

public record AnalyticsEventResponse(
        Long id,
        String visitorId,
        String eventType,
        String pageUrl,
        String path,
        String pageTitle,
        String referrer,
        String targetUrl,
        String targetLabel,
        String deviceType,
        String browser,
        String os,
        String language,
        String timezone,
        String screenSize,
        String utmSource,
        String utmMedium,
        String utmCampaign,
        String ipAddress,
        String country,
        String region,
        String city,
        String latitude,
        String longitude,
        String userAgent,
        Instant createdAt
) {
    public static AnalyticsEventResponse from(PortfolioVisitEvent event) {
        return new AnalyticsEventResponse(
                event.getId(),
                event.getVisitorId(),
                event.getEventType(),
                event.getPageUrl(),
                event.getPath(),
                event.getPageTitle(),
                event.getReferrer(),
                event.getTargetUrl(),
                event.getTargetLabel(),
                event.getDeviceType(),
                event.getBrowser(),
                event.getOs(),
                event.getLanguage(),
                event.getTimezone(),
                event.getScreenSize(),
                event.getUtmSource(),
                event.getUtmMedium(),
                event.getUtmCampaign(),
                event.getIpAddress(),
                event.getCountry(),
                event.getRegion(),
                event.getCity(),
                event.getLatitude(),
                event.getLongitude(),
                event.getUserAgent(),
                event.getCreatedAt()
        );
    }
}
