package com.ngarisamuel.portfolio.skill;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByPortfolioIdAndDeletedFalse(String portfolioId, Sort sort);

    Optional<Skill> findByIdAndPortfolioIdAndDeletedFalse(Long id, String portfolioId);
}
