package com.ngarisamuel.portfolio.certification;

import com.ngarisamuel.portfolio.common.PortfolioIds;
import com.ngarisamuel.portfolio.common.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public List<CertificationResponse> findAll(String portfolioId) {
        return certificationRepository.findByPortfolioIdAndDeletedFalse(PortfolioIds.normalize(portfolioId), Sort.by("id").ascending())
                .stream()
                .map(CertificationResponse::from)
                .toList();
    }

    public CertificationResponse create(CertificationRequest request) {
        Certification certification = new Certification();
        applyRequest(certification, request, PortfolioIds.normalize(request.portfolioId()));
        return CertificationResponse.from(certificationRepository.save(certification));
    }

    public CertificationResponse update(Long id, String portfolioId, CertificationRequest request) {
        String normalizedPortfolioId = resolvePortfolioId(portfolioId, request.portfolioId());
        Certification certification = certificationRepository.findByIdAndPortfolioIdAndDeletedFalse(id, normalizedPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found: " + id));
        applyRequest(certification, request, normalizedPortfolioId);
        return CertificationResponse.from(certificationRepository.save(certification));
    }

    public void delete(Long id, String portfolioId) {
        Certification certification = certificationRepository.findByIdAndPortfolioIdAndDeletedFalse(id, PortfolioIds.normalize(portfolioId))
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found: " + id));
        certification.setDeleted(true);
        certificationRepository.save(certification);
    }

    private void applyRequest(Certification certification, CertificationRequest request, String portfolioId) {
        certification.setPortfolioId(portfolioId);
        certification.setName(textOrEmpty(request.name()));
        certification.setIssuer(textOrEmpty(request.issuer()));
        certification.setDateIssued(textOrEmpty(request.dateIssued()));
        certification.setCredentialUrl(nullableText(request.credentialUrl()));
    }

    private String resolvePortfolioId(String portfolioId, String requestPortfolioId) {
        return PortfolioIds.normalize(portfolioId == null ? requestPortfolioId : portfolioId);
    }

    private static String textOrEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private static String nullableText(String value) {
        String normalized = textOrEmpty(value);
        return normalized.isEmpty() ? null : normalized;
    }
}
