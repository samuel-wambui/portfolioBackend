package com.ngarisamuel.portfolio.education;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;

    public List<EducationResponse> findAll(String portfolioId) {
        return educationRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by("id").ascending())
                .stream()
                .sorted(Comparator.comparing(EducationService::dateSortKey).reversed().thenComparing(Education::getId))
                .map(EducationResponse::from)
                .toList();
    }

    public EducationResponse create(EducationRequest request) {
        Education education = new Education();
        applyRequest(education, request, PortfolioIds.normalize(request.portfolioId()));
        return EducationResponse.from(educationRepository.save(education));
    }

    public EducationResponse update(Long id, String portfolioId, EducationRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        Education education = educationRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found: " + id));
        applyRequest(education, request, normalizedPortfolioId);
        return EducationResponse.from(educationRepository.save(education));
    }

    public void delete(Long id, String portfolioId) {
        Education education = educationRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Education not found: " + id));
        education.setDeleted(true);
        educationRepository.save(education);
    }

    private void applyRequest(Education education, EducationRequest request, String portfolioId) {
        education.setPortfolioId(portfolioId);
        education.setInstitution(textOrEmpty(request.institution()));
        education.setCourse(textOrEmpty(request.course()));
        education.setGrade(textOrEmpty(request.grade()));
        education.setStartDate(textOrEmpty(request.startDate()));
        education.setEndDate(textOrEmpty(request.endDate()));
        education.setDescription(textOrEmpty(request.description()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String dateSortKey(Education education) {
        String endDate = textOrEmpty(education.getEndDate());
        return endDate.isEmpty() ? textOrEmpty(education.getStartDate()) : endDate;
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

}
