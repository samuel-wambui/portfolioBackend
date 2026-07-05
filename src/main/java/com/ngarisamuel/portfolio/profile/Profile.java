package com.ngarisamuel.portfolio.profile;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.ngarisamuel.portfolio.common.PortfolioIds.DEFAULT_PORTFOLIO_ID;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portfolio_id", nullable = false, length = 32)
    private String portfolioId = DEFAULT_PORTFOLIO_ID;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_roles", joinColumns = @JoinColumn(name = "profile_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "role", nullable = false)
    private List<String> roles = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_specializations", joinColumns = @JoinColumn(name = "profile_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "specialization", nullable = false)
    private List<String> specializedIn = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_focus_items", joinColumns = @JoinColumn(name = "profile_id"))
    @OrderColumn(name = "sort_order")
    @Column(name = "focus_item", nullable = false)
    private List<String> currentlyFocusedOn = new ArrayList<>();

    @Column(name = "hero_text", columnDefinition = "text")
    private String heroText;

    @Column(columnDefinition = "text")
    private String summary;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String location;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "projects_completed")
    private Integer projectsCompleted;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "learning_label")
    private String learningLabel;

    @Column(name = "collaboration_label")
    private String collaborationLabel;
}
