package com.ngarisamuel.portfolio.leadershipimpact;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeadershipImpactService {

    private final LeadershipImpactRepository leadershipImpactRepository;

    public List<LeadershipImpactResponse> findAll(String portfolioId) {
        return leadershipImpactRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by(
                        Sort.Order.asc("displayOrder").nullsLast(),
                        Sort.Order.asc("id")
                ))
                .stream()
                .map(LeadershipImpactResponse::from)
                .toList();
    }

    public LeadershipImpactResponse create(LeadershipImpactRequest request) {
        LeadershipImpact item = new LeadershipImpact();
        applyRequest(item, request, PortfolioIds.normalize(request.portfolioId()));
        return LeadershipImpactResponse.from(leadershipImpactRepository.save(item));
    }

    public LeadershipImpactResponse update(Long id, String portfolioId, LeadershipImpactRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        LeadershipImpact item = leadershipImpactRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Leadership impact item not found: " + id));
        applyRequest(item, request, normalizedPortfolioId);
        return LeadershipImpactResponse.from(leadershipImpactRepository.save(item));
    }

    public void delete(Long id, String portfolioId) {
        LeadershipImpact item = leadershipImpactRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Leadership impact item not found: " + id));
        item.setDeleted(true);
        leadershipImpactRepository.save(item);
    }

    private void applyRequest(LeadershipImpact item, LeadershipImpactRequest request, String portfolioId) {
        item.setPortfolioId(portfolioId);
        item.setCategory(textOrEmpty(request.category()));
        item.setTitle(textOrEmpty(request.title()));
        item.setDescription(textOrEmpty(request.description()));
        item.setImpact(textOrEmpty(request.impact()));
        item.setMetricValue(textOrEmpty(request.metricValue()));
        item.setMetricLabel(textOrEmpty(request.metricLabel()));
        item.setDisplayOrder(request.displayOrder());
        item.setTags(listOrEmpty(request.tags()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static List<String> listOrEmpty(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }
}
