package com.ngarisamuel.portfolio.profile;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByPortfolioId(String portfolioId);
}
