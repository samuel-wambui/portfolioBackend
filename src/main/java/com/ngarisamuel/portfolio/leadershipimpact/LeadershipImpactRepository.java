package com.ngarisamuel.portfolio.leadershipimpact;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadershipImpactRepository extends JpaRepository<LeadershipImpact, Long> {

    List<LeadershipImpact> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<LeadershipImpact> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
