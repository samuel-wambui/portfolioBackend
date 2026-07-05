package com.ngarisamuel.portfolio.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<Project> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
