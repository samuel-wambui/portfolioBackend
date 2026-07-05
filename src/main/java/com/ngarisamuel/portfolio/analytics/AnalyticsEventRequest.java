package com.ngarisamuel.portfolio.analytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnalyticsEventRequest(
        @Size(max = 80, message = "Visitor ID must be 80 characters or fewer")
        String visitorId,

        @NotBlank(message = "Event type is required")
        @Size(max = 40, message = "Event type must be 40 characters or fewer")
        String eventType,

        @Size(max = 2048, message = "Page URL must be 2048 characters or fewer")
        String pageUrl,

        @Size(max = 500, message = "Path must be 500 characters or fewer")
        String path,

        @Size(max = 300, message = "Page title must be 300 characters or fewer")
        String pageTitle,

        @Size(max = 2048, message = "Referrer must be 2048 characters or fewer")
        String referrer,

        @Size(max = 2048, message = "Target URL must be 2048 characters or fewer")
        String targetUrl,

        @Size(max = 300, message = "Target label must be 300 characters or fewer")
        String targetLabel,

        @Size(max = 40, message = "Device type must be 40 characters or fewer")
        String deviceType,

        @Size(max = 80, message = "Browser must be 80 characters or fewer")
        String browser,

        @Size(max = 80, message = "OS must be 80 characters or fewer")
        String os,

        @Size(max = 40, message = "Language must be 40 characters or fewer")
        String language,

        @Size(max = 80, message = "Timezone must be 80 characters or fewer")
        String timezone,

        @Size(max = 40, message = "Screen size must be 40 characters or fewer")
        String screenSize,

        @Size(max = 120, message = "UTM source must be 120 characters or fewer")
        String utmSource,

        @Size(max = 120, message = "UTM medium must be 120 characters or fewer")
        String utmMedium,

        @Size(max = 160, message = "UTM campaign must be 160 characters or fewer")
        String utmCampaign,

        @Size(max = 120, message = "IP address must be 120 characters or fewer")
        String ipAddress,

        @Size(max = 80, message = "Country must be 80 characters or fewer")
        String country,

        @Size(max = 120, message = "Region must be 120 characters or fewer")
        String region,

        @Size(max = 120, message = "City must be 120 characters or fewer")
        String city,

        @Size(max = 40, message = "Latitude must be 40 characters or fewer")
        String latitude,

        @Size(max = 40, message = "Longitude must be 40 characters or fewer")
        String longitude
) {
}
