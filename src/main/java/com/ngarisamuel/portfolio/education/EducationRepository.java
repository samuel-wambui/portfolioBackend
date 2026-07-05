package com.ngarisamuel.portfolio.education;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<Education> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
