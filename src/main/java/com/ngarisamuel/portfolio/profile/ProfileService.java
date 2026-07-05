package com.ngarisamuel.portfolio.profile;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileResponse findProfile(String portfolioId) {
        return profileRepository.findByPortfolioId(PortfolioIds.normalize(portfolioId))
                .map(ProfileResponse::from)
                .orElse(null);
    }

    public ProfileResponse upsert(String portfolioId, ProfileRequest request) {
        String normalizedPortfolioId = PortfolioIds.normalize(portfolioId == null ? request.portfolioId() : portfolioId);
        Profile profile = profileRepository.findByPortfolioId(normalizedPortfolioId)
                .orElseGet(Profile::new);
        profile.setPortfolioId(normalizedPortfolioId);
        applyRequest(profile, request);
        return ProfileResponse.from(profileRepository.save(profile));
    }

    private void applyRequest(Profile profile, ProfileRequest request) {
        profile.setFullName(request.fullName());
        profile.setTitle(request.title());
        profile.setRoles(listOrEmpty(request.roles()));
        profile.setSpecializedIn(listOrEmpty(request.specializedIn()));
        profile.setCurrentlyFocusedOn(listOrEmpty(request.currentlyFocusedOn()));
        profile.setHeroText(request.heroText());
        profile.setSummary(request.summary());
        profile.setEmail(request.email());
        profile.setPhone(request.phone());
        profile.setLocation(request.location());
        profile.setGithubUrl(request.githubUrl());
        profile.setLinkedinUrl(request.linkedinUrl());
        profile.setPortfolioUrl(request.portfolioUrl());
        profile.setCvUrl(request.cvUrl());
        profile.setPhotoUrl(request.photoUrl());
        profile.setProjectsCompleted(request.projectsCompleted());
        profile.setYearsExperience(request.yearsExperience());
        profile.setLearningLabel(request.learningLabel());
        profile.setCollaborationLabel(request.collaborationLabel());
    }

    private static List<String> listOrEmpty(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }
}
