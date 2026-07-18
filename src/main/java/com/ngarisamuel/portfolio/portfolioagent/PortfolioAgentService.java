package com.ngarisamuel.portfolio.portfolioagent;

import com.ngarisamuel.portfolio.blog.BlogPostResponse;
import com.ngarisamuel.portfolio.blog.BlogPostService;
import com.ngarisamuel.portfolio.certification.CertificationResponse;
import com.ngarisamuel.portfolio.certification.CertificationService;
import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.education.EducationResponse;
import com.ngarisamuel.portfolio.education.EducationService;
import com.ngarisamuel.portfolio.experience.ExperienceResponse;
import com.ngarisamuel.portfolio.experience.ExperienceService;
import com.ngarisamuel.portfolio.leadershipimpact.LeadershipImpactResponse;
import com.ngarisamuel.portfolio.leadershipimpact.LeadershipImpactService;
import com.ngarisamuel.portfolio.profile.ProfileResponse;
import com.ngarisamuel.portfolio.profile.ProfileService;
import com.ngarisamuel.portfolio.project.ProjectResponse;
import com.ngarisamuel.portfolio.project.ProjectService;
import com.ngarisamuel.portfolio.skill.SkillResponse;
import com.ngarisamuel.portfolio.skill.SkillService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAgentService {
    private static final int DEFAULT_CONTEXT_MAX_CHARS = 16000;
    private static final int MIN_CONTEXT_MAX_CHARS = 4000;
    private static final int MAX_CONTEXT_MAX_CHARS = 40000;
    private static final int DEFAULT_SEARCH_LIMIT = 8;
    private static final int MAX_SEARCH_LIMIT = 20;

    private final ProfileService profileService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final ExperienceService experienceService;
    private final EducationService educationService;
    private final CertificationService certificationService;
    private final LeadershipImpactService leadershipImpactService;
    private final BlogPostService blogPostService;
    private final PortfolioAgentEmbeddingService embeddingService;
    private final PortfolioAgentVectorStore vectorStore;

    public PortfolioAgentSnapshot snapshot(String portfolioId) {
        String normalizedPortfolioId = PortfolioIds.normalize(portfolioId);
        ProfileResponse profile = profileService.findProfile(normalizedPortfolioId);
        List<ProjectResponse> projects = projectService.findAll(normalizedPortfolioId);
        List<SkillResponse> skills = skillService.findAll(normalizedPortfolioId);
        List<ExperienceResponse> experience = experienceService.findAll(normalizedPortfolioId);
        List<EducationResponse> education = educationService.findAll(normalizedPortfolioId);
        List<CertificationResponse> certifications = certificationService.findAll(normalizedPortfolioId);
        List<LeadershipImpactResponse> leadershipImpact = leadershipImpactService.findAll(normalizedPortfolioId);
        List<BlogPostResponse> blogPosts = blogPostService.findAll(normalizedPortfolioId);
        PortfolioAgentFacts facts = new PortfolioAgentFacts(
                normalizedPortfolioId,
                profile == null ? "" : text(profile.fullName()),
                profile == null ? "" : text(profile.title()),
                certifications.size(),
                projects.size(),
                experience.size(),
                education.size(),
                skills.size(),
                leadershipImpact.size(),
                blogPosts.size()
        );
        List<PortfolioAgentChunk> chunks = buildChunks(
                normalizedPortfolioId,
                profile,
                projects,
                skills,
                experience,
                education,
                certifications,
                leadershipImpact,
                blogPosts,
                facts
        );

        return new PortfolioAgentSnapshot(
                normalizedPortfolioId,
                profile,
                facts,
                projects,
                skills,
                experience,
                education,
                certifications,
                leadershipImpact,
                blogPosts,
                chunks
        );
    }

    public PortfolioAgentFacts facts(String portfolioId) {
        return snapshot(portfolioId).facts();
    }

    public List<PortfolioAgentChunk> chunks(String portfolioId) {
        return snapshot(portfolioId).chunks();
    }

    public PortfolioAgentContextResponse context(String portfolioId, int maxChars) {
        PortfolioAgentSnapshot snapshot = snapshot(portfolioId);
        String profileName = snapshot.profile() == null
                ? text(snapshot.facts().fullName())
                : text(snapshot.profile().fullName());

        return new PortfolioAgentContextResponse(
                snapshot.portfolioId(),
                profileName,
                snapshot.facts(),
                snapshot.chunks(),
                buildContext(snapshot.chunks(), normalizeMaxChars(maxChars))
        );
    }

    public PortfolioAgentReindexResponse reindex(PortfolioAgentReindexRequest request) {
        String normalizedPortfolioId = PortfolioIds.normalize(request == null ? null : request.portfolioId());
        PortfolioAgentSnapshot snapshot = snapshot(normalizedPortfolioId);
        List<PortfolioAgentChunk> chunks = snapshot.chunks();

        vectorStore.ensureSchema();
        vectorStore.markPortfolioDeleted(normalizedPortfolioId);

        List<List<Double>> embeddings = embeddingService.embedAll(chunks.stream()
                .map(PortfolioAgentChunk::content)
                .toList());

        for (int index = 0; index < chunks.size(); index++) {
            vectorStore.upsertChunk(
                    normalizedPortfolioId,
                    chunks.get(index),
                    embeddings.get(index),
                    embeddingService.embeddingModel()
            );
        }

        return new PortfolioAgentReindexResponse(
                normalizedPortfolioId,
                embeddingService.embeddingModel(),
                chunks.size()
        );
    }

    public PortfolioAgentSearchResponse search(PortfolioAgentSearchRequest request) {
        String normalizedPortfolioId = PortfolioIds.normalize(request == null ? null : request.portfolioId());
        String query = text(request == null ? "" : request.query());

        if (query.isBlank()) {
            throw new IllegalArgumentException("Search query is required");
        }

        PortfolioAgentSnapshot snapshot = snapshot(normalizedPortfolioId);
        String profileName = snapshot.profile() == null
                ? text(snapshot.facts().fullName())
                : text(snapshot.profile().fullName());
        int limit = normalizeSearchLimit(request == null ? null : request.limit());
        int maxContextChars = normalizeMaxChars(request == null || request.maxContextChars() == null
                ? DEFAULT_CONTEXT_MAX_CHARS
                : request.maxContextChars());

        vectorStore.ensureSchema();

        List<Double> queryEmbedding = embeddingService.embed(query);
        List<PortfolioAgentSearchResult> results = vectorStore.search(
                normalizedPortfolioId,
                queryEmbedding,
                embeddingService.embeddingModel(),
                limit
        );

        if (results.isEmpty()) {
            reindex(new PortfolioAgentReindexRequest(normalizedPortfolioId));
            results = vectorStore.search(
                    normalizedPortfolioId,
                    queryEmbedding,
                    embeddingService.embeddingModel(),
                    limit
            );
        }

        return new PortfolioAgentSearchResponse(
                normalizedPortfolioId,
                profileName,
                snapshot.facts(),
                query,
                embeddingService.embeddingModel(),
                results,
                buildSearchContext(results, maxContextChars)
        );
    }

    private List<PortfolioAgentChunk> buildChunks(
            String portfolioId,
            ProfileResponse profile,
            List<ProjectResponse> projects,
            List<SkillResponse> skills,
            List<ExperienceResponse> experience,
            List<EducationResponse> education,
            List<CertificationResponse> certifications,
            List<LeadershipImpactResponse> leadershipImpact,
            List<BlogPostResponse> blogPosts,
            PortfolioAgentFacts facts
    ) {
        List<PortfolioAgentChunk> chunks = new ArrayList<>();

        if (profile != null) {
            chunks.add(chunk(
                    portfolioId,
                    "profile",
                    String.valueOf(profile.id()),
                    "summary",
                    text(profile.fullName()),
                    "Profile",
                    lines(
                            sentence("%s is %s.", profile.fullName(), profile.title()),
                            sentence("Hero message: %s", profile.heroText()),
                            sentence("Summary: %s", profile.summary()),
                            yearsExperienceSentence(profile),
                            sentence("Roles: %s", join(profile.roles())),
                            sentence("Specialized in: %s", join(profile.specializedIn())),
                            sentence("Currently focused on: %s", join(profile.currentlyFocusedOn())),
                            sentence("Location: %s", profile.location()),
                            sentence("Email: %s", profile.email()),
                            sentence("Phone: %s", profile.phone()),
                            sentence("GitHub: %s", profile.githubUrl()),
                            sentence("LinkedIn: %s", profile.linkedinUrl())
                    ),
                    Map.of("table", "profiles", "id", profile.id(), "portfolioId", portfolioId)
            ));
        }

        chunks.add(chunk(
                portfolioId,
                "facts",
                portfolioId,
                "counts",
                "Portfolio facts",
                "Facts",
                lines(
                        sentence("Portfolio %s belongs to %s.", facts.portfolioId(), facts.fullName()),
                        yearsExperienceSentence(profile),
                        experienceTimelineSentence(facts.fullName(), experience),
                        sentence("%s has %d certifications.", facts.fullName(), facts.certificationCount()),
                        sentence("%s has %d projects.", facts.fullName(), facts.projectCount()),
                        sentence("%s has %d experience entries.", facts.fullName(), facts.experienceCount()),
                        sentence("%s has %d education entries.", facts.fullName(), facts.educationCount()),
                        sentence("%s has %d skills.", facts.fullName(), facts.skillCount()),
                        sentence("%s has %d leadership and impact entries.", facts.fullName(), facts.leadershipImpactCount()),
                        sentence("%s has %d blog posts.", facts.fullName(), facts.blogPostCount())
                ),
                Map.of("table", "portfolio_agent_facts", "portfolioId", portfolioId)
        ));

        projects.forEach(project -> chunks.add(chunk(
                portfolioId,
                "project",
                String.valueOf(project.id()),
                "project-" + project.id(),
                text(project.title()),
                "Projects",
                lines(
                        sentence("Project: %s", project.title()),
                        sentence("Problem: %s", project.problem()),
                        sentence("Architecture: %s", project.architecture()),
                        sentence("Technologies: %s", join(project.technologies())),
                        sentence("Challenges: %s", join(project.challenges())),
                        sentence("Results: %s", join(project.results())),
                        sentence("Lessons learned: %s", join(project.lessonsLearned())),
                        sentence("GitHub: %s", project.githubUrl()),
                        sentence("Live demo: %s", project.liveDemoUrl())
                ),
                Map.of("table", "projects", "id", project.id(), "portfolioId", portfolioId)
        )));

        experience.forEach(item -> chunks.add(chunk(
                portfolioId,
                "experience",
                String.valueOf(item.id()),
                "experience-" + item.id(),
                text(item.company() + " - " + item.role()),
                "Experience",
                lines(
                        sentence("Experience: %s at %s.", item.role(), item.company()),
                        sentence("Dates: %s to %s.", item.startDate(), item.current() ? "Present" : item.endDate()),
                        sentence("Description: %s", item.description()),
                        sentence("Technologies: %s", join(item.technologies()))
                ),
                Map.of("table", "experience_items", "id", item.id(), "portfolioId", portfolioId)
        )));

        education.forEach(item -> chunks.add(chunk(
                portfolioId,
                "education",
                String.valueOf(item.id()),
                "education-" + item.id(),
                text(item.institution()),
                "Education",
                lines(
                        sentence("Education: %s at %s.", item.course(), item.institution()),
                        sentence("Grade: %s", item.grade()),
                        sentence("Dates: %s to %s.", item.startDate(), item.endDate()),
                        sentence("Description: %s", item.description())
                ),
                Map.of("table", "education_items", "id", item.id(), "portfolioId", portfolioId)
        )));

        certifications.forEach(item -> chunks.add(chunk(
                portfolioId,
                "certification",
                String.valueOf(item.id()),
                "certification-" + item.id(),
                text(item.name()),
                "Certifications",
                lines(
                        sentence("Certification: %s", item.name()),
                        sentence("Issuer: %s", item.issuer()),
                        sentence("Date issued: %s", item.dateIssued()),
                        sentence("Credential URL: %s", item.credentialUrl())
                ),
                Map.of("table", "certifications", "id", item.id(), "portfolioId", portfolioId)
        )));

        skillsByCategory(skills).forEach((category, items) -> chunks.add(chunk(
                portfolioId,
                "skills",
                category,
                "skills-" + slug(category),
                category + " skills",
                "Skills",
                sentence("%s skills: %s", category, join(items.stream()
                        .map(skill -> skill.name() + " (" + skill.level() + ")")
                        .toList())),
                Map.of("table", "skills", "category", category, "portfolioId", portfolioId)
        )));

        leadershipImpact.forEach(item -> chunks.add(chunk(
                portfolioId,
                "leadership_impact",
                String.valueOf(item.id()),
                "leadership-impact-" + item.id(),
                text(item.title()),
                text(item.category()),
                lines(
                        sentence("%s: %s", item.category(), item.title()),
                        sentence("Description: %s", item.description()),
                        sentence("Impact: %s", item.impact()),
                        sentence("Metric: %s %s", item.metricValue(), item.metricLabel()),
                        sentence("Tags: %s", join(item.tags()))
                ),
                Map.of("table", "leadership_impact_items", "id", item.id(), "portfolioId", portfolioId)
        )));

        blogPosts.forEach(post -> chunks.add(chunk(
                portfolioId,
                "blog_post",
                String.valueOf(post.id()),
                "blog-post-" + post.id(),
                text(post.title()),
                "Blog",
                lines(
                        sentence("Blog post: %s", post.title()),
                        sentence("Excerpt: %s", post.excerpt()),
                        sentence("Published: %s", post.publishedAt()),
                        sentence("Read time: %s", post.readTime()),
                        sentence("Tags: %s", join(post.tags())),
                        sentence("Body: %s", post.body())
                ),
                Map.of("table", "blog_posts", "id", post.id(), "slug", post.slug(), "portfolioId", portfolioId)
        )));

        return chunks.stream()
                .filter(chunk -> !chunk.content().isBlank())
                .toList();
    }

    private static PortfolioAgentChunk chunk(
            String portfolioId,
            String sourceType,
            String sourceId,
            String chunkKey,
            String title,
            String section,
            String content,
            Map<String, Object> metadata
    ) {
        Map<String, Object> enrichedMetadata = new LinkedHashMap<>(metadata);
        enrichedMetadata.put("portfolioId", portfolioId);
        enrichedMetadata.put("sourceType", sourceType);
        enrichedMetadata.put("sourceId", sourceId);
        enrichedMetadata.put("chunkKey", chunkKey);
        enrichedMetadata.put("section", section);

        return new PortfolioAgentChunk(
                sourceType,
                sourceId,
                chunkKey,
                title,
                section,
                text(content),
                enrichedMetadata
        );
    }

    private static String sentence(String template, Object... values) {
        Object[] normalized = new Object[values.length];

        for (int index = 0; index < values.length; index++) {
            normalized[index] = values[index] == null ? "" : values[index];
        }

        return text(String.format(template, normalized));
    }

    private static String lines(String... values) {
        List<String> normalized = new ArrayList<>();

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                normalized.add(value.trim());
            }
        }

        return String.join("\n", normalized);
    }

    private static String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        return String.join(", ", values.stream()
                .map(PortfolioAgentService::text)
                .filter(value -> !value.isBlank())
                .toList());
    }

    private static String yearsExperienceSentence(ProfileResponse profile) {
        if (profile == null || profile.yearsExperience() == null || profile.yearsExperience() <= 0) {
            return "";
        }

        String fullName = text(profile.fullName()).isBlank() ? "Samuel" : text(profile.fullName());

        return sentence(
                "%s has %d+ years of professional software engineering, automation, and AI engineering experience.",
                fullName,
                profile.yearsExperience()
        );
    }

    private static String experienceTimelineSentence(String fullName, List<ExperienceResponse> experience) {
        if (experience == null || experience.isEmpty()) {
            return "";
        }

        String name = text(fullName).isBlank() ? "Samuel" : text(fullName);
        List<String> roles = experience.stream()
                .map(item -> sentence("%s at %s (%s to %s)",
                        item.role(),
                        item.company(),
                        item.startDate(),
                        item.current() ? "Present" : item.endDate()))
                .filter(value -> !value.isBlank())
                .toList();

        return sentence("%s's experience timeline includes: %s.", name, String.join("; ", roles));
    }

    private static Map<String, List<SkillResponse>> skillsByCategory(List<SkillResponse> skills) {
        Map<String, List<SkillResponse>> groups = new LinkedHashMap<>();

        for (SkillResponse skill : skills) {
            String category = text(skill.category());
            if (category.isBlank()) {
                category = "Other";
            }
            groups.computeIfAbsent(category, ignored -> new ArrayList<>()).add(skill);
        }

        return groups;
    }

    private static String slug(String value) {
        return text(value).toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private static String buildContext(List<PortfolioAgentChunk> chunks, int maxChars) {
        String context = String.join("\n\n", chunks.stream()
                .map(chunk -> lines(
                        sentence("[%s] %s", chunk.section(), chunk.title()),
                        chunk.content()
                ))
                .filter(value -> !value.isBlank())
                .toList());

        if (context.length() <= maxChars) {
            return context;
        }

        return context.substring(0, maxChars).trim();
    }

    private static String buildSearchContext(List<PortfolioAgentSearchResult> results, int maxChars) {
        String context = String.join("\n\n", results.stream()
                .map(result -> lines(
                        sentence("[%s] %s", result.section(), result.title()),
                        sentence("Similarity: %.4f", result.similarity()),
                        result.content()
                ))
                .filter(value -> !value.isBlank())
                .toList());

        if (context.length() <= maxChars) {
            return context;
        }

        return context.substring(0, maxChars).trim();
    }

    private static int normalizeMaxChars(int maxChars) {
        if (maxChars <= 0) {
            return DEFAULT_CONTEXT_MAX_CHARS;
        }

        return Math.min(Math.max(maxChars, MIN_CONTEXT_MAX_CHARS), MAX_CONTEXT_MAX_CHARS);
    }

    private static int normalizeSearchLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_SEARCH_LIMIT;
        }

        return Math.min(limit, MAX_SEARCH_LIMIT);
    }

    private static String text(String value) {
        return value == null ? "" : value.trim();
    }
}
