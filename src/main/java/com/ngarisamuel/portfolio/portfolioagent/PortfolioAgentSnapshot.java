package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.blog.BlogPostResponse;
import com.ngarisamuel.portfolio.certification.CertificationResponse;
import com.ngarisamuel.portfolio.education.EducationResponse;
import com.ngarisamuel.portfolio.experience.ExperienceResponse;
import com.ngarisamuel.portfolio.leadershipimpact.LeadershipImpactResponse;
import com.ngarisamuel.portfolio.profile.ProfileResponse;
import com.ngarisamuel.portfolio.project.ProjectResponse;
import com.ngarisamuel.portfolio.skill.SkillResponse;
import java.util.List;

public record PortfolioAgentSnapshot(
        String portfolioId,
        ProfileResponse profile,
        PortfolioAgentFacts facts,
        List<ProjectResponse> projects,
        List<SkillResponse> skills,
        List<ExperienceResponse> experience,
        List<EducationResponse> education,
        List<CertificationResponse> certifications,
        List<LeadershipImpactResponse> leadershipImpact,
        List<BlogPostResponse> blogPosts,
        List<PortfolioAgentChunk> chunks
) {
}
