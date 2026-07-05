package com.ngarisamuel.portfolio.skill;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<SkillResponse> findAll(String portfolioId) {
        return skillRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by("id").ascending())
                .stream()
                .map(SkillResponse::from)
                .toList();
    }

    public SkillResponse create(SkillRequest request) {
        Skill skill = new Skill();
        applyRequest(skill, request, PortfolioIds.normalize(request.portfolioId()));
        return SkillResponse.from(skillRepository.save(skill));
    }

    public SkillResponse update(Long id, String portfolioId, SkillRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        Skill skill = skillRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id));
        applyRequest(skill, request, normalizedPortfolioId);
        return SkillResponse.from(skillRepository.save(skill));
    }

    public void delete(Long id, String portfolioId) {
        Skill skill = skillRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id));
        skill.setDeleted(true);
        skillRepository.save(skill);
    }

    private void applyRequest(Skill skill, SkillRequest request, String portfolioId) {
        skill.setPortfolioId(portfolioId);
        skill.setName(textOrEmpty(request.name()));
        skill.setCategory(textOrEmpty(request.category()));
        skill.setLevel(textOrEmpty(request.level()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
