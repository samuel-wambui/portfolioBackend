package com.ngarisamuel.portfolio.certification;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<Certification> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
