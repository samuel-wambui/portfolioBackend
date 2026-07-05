package com.ngarisamuel.portfolio.experience;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<Experience> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
