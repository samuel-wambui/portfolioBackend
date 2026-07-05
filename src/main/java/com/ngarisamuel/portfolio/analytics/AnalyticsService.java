package com.ngarisamuel.portfolio.analytics;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final int DEFAULT_LIMIT = 100;
    private static final int MAX_LIMIT = 500;

    private final PortfolioVisitEventRepository portfolioVisitEventRepository;

    public void record(AnalyticsEventRequest request, HttpServletRequest servletRequest) {
        PortfolioVisitEvent event = new PortfolioVisitEvent();
        event.setVisitorId(clean(request.visitorId()));
        event.setEventType(clean(request.eventType()));
        event.setPageUrl(clean(request.pageUrl()));
        event.setPath(clean(request.path()));
        event.setPageTitle(clean(request.pageTitle()));
        event.setReferrer(clean(request.referrer()));
        event.setTargetUrl(clean(request.targetUrl()));
        event.setTargetLabel(clean(request.targetLabel()));
        event.setDeviceType(clean(request.deviceType()));
        event.setBrowser(clean(request.browser()));
        event.setOs(clean(request.os()));
        event.setLanguage(clean(request.language()));
        event.setTimezone(clean(request.timezone()));
        event.setScreenSize(clean(request.screenSize()));
        event.setUtmSource(clean(request.utmSource()));
        event.setUtmMedium(clean(request.utmMedium()));
        event.setUtmCampaign(clean(request.utmCampaign()));
        event.setIpAddress(firstAvailable(clean(request.ipAddress()), clientIp(servletRequest)));
        event.setCountry(firstAvailable(clean(request.country()), header(servletRequest, "CF-IPCountry")));
        event.setRegion(clean(request.region()));
        event.setCity(clean(request.city()));
        event.setLatitude(clean(request.latitude()));
        event.setLongitude(clean(request.longitude()));
        event.setUserAgent(header(servletRequest, "User-Agent"));
        event.setCreatedAt(Instant.now());

        portfolioVisitEventRepository.save(event);
    }

    public List<AnalyticsEventResponse> findRecent(Integer requestedLimit) {
        int limit = requestedLimit == null ? DEFAULT_LIMIT : Math.max(1, Math.min(requestedLimit, MAX_LIMIT));
        return portfolioVisitEventRepository.findAll(
                        PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
                )
                .stream()
                .map(AnalyticsEventResponse::from)
                .toList();
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = header(request, "X-Forwarded-For");
        if (forwardedFor != null) {
            String[] addresses = forwardedFor.split(",");
            if (addresses.length > 0) {
                String first = clean(addresses[0]);
                if (first != null) {
                    return first;
                }
            }
        }

        String realIp = header(request, "X-Real-IP");
        if (realIp != null) {
            return realIp;
        }

        String cloudflareIp = header(request, "CF-Connecting-IP");
        if (cloudflareIp != null) {
            return cloudflareIp;
        }

        return clean(request.getRemoteAddr());
    }

    private String firstAvailable(String preferred, String fallback) {
        return preferred != null ? preferred : fallback;
    }

    private String header(HttpServletRequest request, String name) {
        return clean(request.getHeader(name));
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
