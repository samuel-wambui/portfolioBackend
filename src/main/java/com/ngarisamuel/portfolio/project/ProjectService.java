package com.ngarisamuel.portfolio.project;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectResponse> findAll(String portfolioId) {
        return projectRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by(
                        Sort.Order.asc("displayOrder").nullsLast(),
                        Sort.Order.asc("id")
                ))
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    public ProjectResponse findById(Long id, String portfolioId) {
        return projectRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .map(ProjectResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        applyRequest(project, request, PortfolioIds.normalize(request.portfolioId()));
        return ProjectResponse.from(projectRepository.save(project));
    }

    public ProjectResponse update(Long id, String portfolioId, ProjectRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        Project project = projectRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
        applyRequest(project, request, normalizedPortfolioId);
        return ProjectResponse.from(projectRepository.save(project));
    }

    public void delete(Long id, String portfolioId) {
        Project project = projectRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
        project.setDeleted(true);
        projectRepository.save(project);
    }

    private void applyRequest(Project project, ProjectRequest request, String portfolioId) {
        project.setPortfolioId(portfolioId);
        project.setTitle(textOrEmpty(request.title()));
        project.setProblem(textOrEmpty(request.problem()));
        project.setArchitecture(textOrEmpty(request.architecture()));
        project.setDisplayOrder(request.displayOrder());
        project.setScreenshots(listOrEmpty(request.screenshots()));
        project.setTechnologies(listOrEmpty(request.technologies()));
        project.setChallenges(listOrEmpty(request.challenges()));
        project.setResults(listOrEmpty(request.results()));
        project.setLessonsLearned(listOrEmpty(request.lessonsLearned()));
        project.setGithubUrl(nullableText(request.githubUrl()));
        project.setLiveDemoUrl(nullableText(request.liveDemoUrl()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static String nullableText(String value) {
        String normalized = textOrEmpty(value);
        return normalized.isEmpty() ? null : normalized;
    }

    private static List<String> listOrEmpty(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }
}
