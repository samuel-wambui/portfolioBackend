package com.ngarisamuel.portfolio.experience;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public List<ExperienceResponse> findAll(String portfolioId) {
        return experienceRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by("id").ascending())
                .stream()
                .sorted(Comparator.comparing(ExperienceService::dateSortKey).reversed().thenComparing(Experience::getId))
                .map(ExperienceResponse::from)
                .toList();
    }

    public ExperienceResponse create(ExperienceRequest request) {
        Experience experience = new Experience();
        applyRequest(experience, request, PortfolioIds.normalize(request.portfolioId()));
        return ExperienceResponse.from(experienceRepository.save(experience));
    }

    public ExperienceResponse update(Long id, String portfolioId, ExperienceRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        Experience experience = experienceRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found: " + id));
        applyRequest(experience, request, normalizedPortfolioId);
        return ExperienceResponse.from(experienceRepository.save(experience));
    }

    public void delete(Long id, String portfolioId) {
        Experience experience = experienceRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found: " + id));
        experience.setDeleted(true);
        experienceRepository.save(experience);
    }

    private void applyRequest(Experience experience, ExperienceRequest request, String portfolioId) {
        experience.setPortfolioId(portfolioId);
        experience.setCompany(textOrEmpty(request.company()));
        experience.setRole(textOrEmpty(request.role()));
        experience.setStartDate(textOrEmpty(request.startDate()));
        experience.setEndDate(textOrEmpty(request.endDate()));
        experience.setCurrent(request.current());
        experience.setDescription(textOrEmpty(request.description()));
        experience.setTechnologies(listOrEmpty(request.technologies()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String dateSortKey(Experience experience) {
        String endDate = textOrEmpty(experience.getEndDate());
        return endDate.isEmpty() ? textOrEmpty(experience.getStartDate()) : endDate;
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static List<String> listOrEmpty(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }
}
