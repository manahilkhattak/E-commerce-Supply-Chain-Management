package com.ecommerce.supplychain.quality.service;

import com.ecommerce.supplychain.quality.dto.*;
import com.ecommerce.supplychain.quality.model.QualityCheck;
import com.ecommerce.supplychain.quality.model.QualityStandard;
import com.ecommerce.supplychain.quality.repository.QualityCheckRepository;
import com.ecommerce.supplychain.quality.repository.QualityStandardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling quality control business logic.
 */
@Service
public class QualityService {

    @Autowired
    private QualityCheckRepository qualityCheckRepository;

    @Autowired
    private QualityStandardRepository qualityStandardRepository;

    /**
     * API 1: Create quality check for package
     */
    @Transactional
    public QualityCheckResponseDTO createQualityCheck(QualityCheckDTO qualityCheckDTO) {
        // Generate unique check number
        String checkNumber = generateCheckNumber();

        // Check if quality check already exists for this package
        List<QualityCheck> existingChecks = qualityCheckRepository.findByPackageId(qualityCheckDTO.getPackageId());
        if (!existingChecks.isEmpty()) {
            throw new IllegalArgumentException("Quality check already exists for package ID: " + qualityCheckDTO.getPackageId());
        }

        // Create quality check entity
        QualityCheck qualityCheck = new QualityCheck();
        qualityCheck.setCheckNumber(checkNumber);
        qualityCheck.setPackageId(qualityCheckDTO.getPackageId());
        qualityCheck.setTrackingNumber(qualityCheckDTO.getTrackingNumber());
        qualityCheck.setOrderId(qualityCheckDTO.getOrderId());
        qualityCheck.setOrderNumber(qualityCheckDTO.getOrderNumber());
        qualityCheck.setWarehouseId(qualityCheckDTO.getWarehouseId());
        qualityCheck.setCheckType(qualityCheckDTO.getCheckType());
        qualityCheck.setInspectorName(qualityCheckDTO.getInspectorName());
        qualityCheck.setPackageIntegrityScore(qualityCheckDTO.getPackageIntegrityScore());
        qualityCheck.setContentAccuracyScore(qualityCheckDTO.getContentAccuracyScore());
        qualityCheck.setLabelAccuracyScore(qualityCheckDTO.getLabelAccuracyScore());
        qualityCheck.setWeightAccuracyScore(qualityCheckDTO.getWeightAccuracyScore());
        qualityCheck.setSafetyComplianceScore(qualityCheckDTO.getSafetyComplianceScore());
        qualityCheck.setIsPackageDamaged(qualityCheckDTO.getIsPackageDamaged());
        qualityCheck.setIsContentCorrect(qualityCheckDTO.getIsContentCorrect());
        qualityCheck.setIsWeightAccurate(qualityCheckDTO.getIsWeightAccurate());
        qualityCheck.setAreLabelsCorrect(qualityCheckDTO.getAreLabelsCorrect());
        qualityCheck.setIsHazardousCompliant(qualityCheckDTO.getIsHazardousCompliant());
        qualityCheck.setIssuesFound(qualityCheckDTO.getIssuesFound());
        qualityCheck.setCorrectiveActions(qualityCheckDTO.getCorrectiveActions());
        qualityCheck.setCheckNotes(qualityCheckDTO.getCheckNotes());
        qualityCheck.setCreatedAt(LocalDateTime.now());
        qualityCheck.setUpdatedAt(LocalDateTime.now());

        // Start the check
        qualityCheck.startCheck(qualityCheckDTO.getInspectorName());

        QualityCheck savedCheck = qualityCheckRepository.save(qualityCheck);

        return mapToQualityCheckResponseDTO(savedCheck);
    }

    /**
     * API 2: Submit quality check results
     */
    @Transactional
    public QualityCheckResponseDTO submitQualityResults(QualityResultDTO resultDTO) {
        QualityCheck qualityCheck = qualityCheckRepository.findById(resultDTO.getCheckId())
                .orElseThrow(() -> new IllegalArgumentException("Quality check not found with ID: " + resultDTO.getCheckId()));

        // Update quality check with results
        qualityCheck.setPackageIntegrityScore(resultDTO.getPackageIntegrityScore());
        qualityCheck.setContentAccuracyScore(resultDTO.getContentAccuracyScore());
        qualityCheck.setLabelAccuracyScore(resultDTO.getLabelAccuracyScore());
        qualityCheck.setWeightAccuracyScore(resultDTO.getWeightAccuracyScore());
        qualityCheck.setSafetyComplianceScore(resultDTO.getSafetyComplianceScore());
        qualityCheck.setIsPackageDamaged(resultDTO.getIsPackageDamaged());
        qualityCheck.setIsContentCorrect(resultDTO.getIsContentCorrect());
        qualityCheck.setIsWeightAccurate(resultDTO.getIsWeightAccurate());
        qualityCheck.setAreLabelsCorrect(resultDTO.getAreLabelsCorrect());
        qualityCheck.setIsHazardousCompliant(resultDTO.getIsHazardousCompliant());
        qualityCheck.setIssuesFound(resultDTO.getIssuesFound());
        qualityCheck.setCorrectiveActions(resultDTO.getCorrectiveActions());
        qualityCheck.setCheckNotes(resultDTO.getCheckNotes());
        qualityCheck.setInspectorName(resultDTO.getInspectorName());
        qualityCheck.setUpdatedAt(LocalDateTime.now());

        // Auto-calculate results
        qualityCheck.calculateOverallScore();
        qualityCheck.determineOverallResult();

        QualityCheck updatedCheck = qualityCheckRepository.save(qualityCheck);

        return mapToQualityCheckResponseDTO(updatedCheck);
    }

    /**
     * Request quality recheck
     */
    @Transactional
    public QualityCheckResponseDTO requestRecheck(RecheckRequestDTO recheckDTO) {
        QualityCheck qualityCheck = qualityCheckRepository.findById(recheckDTO.getCheckId())
                .orElseThrow(() -> new IllegalArgumentException("Quality check not found with ID: " + recheckDTO.getCheckId()));

        qualityCheck.markForRecheck(recheckDTO.getRecheckNotes());

        QualityCheck updatedCheck = qualityCheckRepository.save(qualityCheck);

        return mapToQualityCheckResponseDTO(updatedCheck);
    }

    /**
     * Get all quality checks
     */
    public List<QualityCheckResponseDTO> getAllQualityChecks() {
        return qualityCheckRepository.findAll().stream()
                .map(this::mapToQualityCheckResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get quality check by ID
     */
    public QualityCheckResponseDTO getQualityCheckById(Long checkId) {
        QualityCheck qualityCheck = qualityCheckRepository.findById(checkId)
                .orElseThrow(() -> new IllegalArgumentException("Quality check not found with ID: " + checkId));
        return mapToQualityCheckResponseDTO(qualityCheck);
    }

    /**
     * Get quality checks by package
     */
    public List<QualityCheckResponseDTO> getQualityChecksByPackageId(Long packageId) {
        return qualityCheckRepository.findByPackageId(packageId).stream()
                .map(this::mapToQualityCheckResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get quality checks by status
     */
    public List<QualityCheckResponseDTO> getQualityChecksByStatus(String status) {
        return qualityCheckRepository.findByCheckStatus(status).stream()
                .map(this::mapToQualityCheckResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get approved quality checks
     */
    public List<QualityCheckResponseDTO> getApprovedQualityChecks() {
        return qualityCheckRepository.findApprovedChecks().stream()
                .map(this::mapToQualityCheckResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get checks requiring recheck
     */
    public List<QualityCheckResponseDTO> getChecksRequiringRecheck() {
        return qualityCheckRepository.findChecksRequiringRecheck().stream()
                .map(this::mapToQualityCheckResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create quality standard
     */
    @Transactional
    public QualityStandardResponseDTO createQualityStandard(QualityStandard qualityStandard) {
        // Validate standard code uniqueness
        if (qualityStandardRepository.existsByStandardCode(qualityStandard.getStandardCode())) {
            throw new IllegalArgumentException("Quality standard with code " + qualityStandard.getStandardCode() + " already exists");
        }

        QualityStandard savedStandard = qualityStandardRepository.save(qualityStandard);

        return mapToQualityStandardResponseDTO(savedStandard);
    }

    /**
     * Get all quality standards
     */
    public List<QualityStandardResponseDTO> getAllQualityStandards() {
        return qualityStandardRepository.findAll().stream()
                .map(this::mapToQualityStandardResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active quality standards
     */
    public List<QualityStandardResponseDTO> getActiveQualityStandards() {
        return qualityStandardRepository.findAllActiveStandards().stream()
                .map(this::mapToQualityStandardResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get quality standards by type
     */
    public List<QualityStandardResponseDTO> getQualityStandardsByType(String checkType) {
        return qualityStandardRepository.findActiveStandardsByType(checkType).stream()
                .map(this::mapToQualityStandardResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validate package against quality standards
     */
    public QualityCheckResponseDTO validateAgainstStandards(Long checkId) {
        QualityCheck qualityCheck = qualityCheckRepository.findById(checkId)
                .orElseThrow(() -> new IllegalArgumentException("Quality check not found with ID: " + checkId));

        // Get relevant standards for this check type
        List<QualityStandard> standards = qualityStandardRepository.findActiveStandardsByType(qualityCheck.getCheckType());

        // Validate against each standard (simplified)
        boolean meetsAllStandards = standards.stream()
                .allMatch(standard -> standard.meetsStandard(qualityCheck.getPackageIntegrityScore()));

        // Update check based on standards validation
        if (meetsAllStandards && qualityCheck.getApprovedForShipment()) {
            qualityCheck.setCheckNotes("Package meets all quality standards for shipment");
        } else {
            qualityCheck.setCheckNotes("Package requires review against quality standards");
        }

        QualityCheck updatedCheck = qualityCheckRepository.save(qualityCheck);

        return mapToQualityCheckResponseDTO(updatedCheck);
    }

    /**
     * Generate unique check number
     */
    private String generateCheckNumber() {
        return "QC-" + System.currentTimeMillis();
    }

    /**
     * Helper method to convert QualityCheck to ResponseDTO
     */
    private QualityCheckResponseDTO mapToQualityCheckResponseDTO(QualityCheck qualityCheck) {
        boolean meetsShippingStandards = qualityCheck.getApprovedForShipment() &&
                "PASS".equals(qualityCheck.getOverallResult());

        String recommendation;
        if (qualityCheck.getApprovedForShipment()) {
            recommendation = "APPROVE";
        } else if (qualityCheck.getRecheckRequired()) {
            recommendation = "RE_CHECK";
        } else {
            recommendation = "REJECT";
        }

        return QualityCheckResponseDTO.builder()
                .checkId(qualityCheck.getCheckId())
                .checkNumber(qualityCheck.getCheckNumber())
                .packageId(qualityCheck.getPackageId())
                .trackingNumber(qualityCheck.getTrackingNumber())
                .orderId(qualityCheck.getOrderId())
                .orderNumber(qualityCheck.getOrderNumber())
                .warehouseId(qualityCheck.getWarehouseId())
                .checkType(qualityCheck.getCheckType())
                .inspectorName(qualityCheck.getInspectorName())
                .checkStatus(qualityCheck.getCheckStatus())
                .overallResult(qualityCheck.getOverallResult())
                .scorePercentage(qualityCheck.getScorePercentage())
                .packageIntegrityScore(qualityCheck.getPackageIntegrityScore())
                .contentAccuracyScore(qualityCheck.getContentAccuracyScore())
                .labelAccuracyScore(qualityCheck.getLabelAccuracyScore())
                .weightAccuracyScore(qualityCheck.getWeightAccuracyScore())
                .safetyComplianceScore(qualityCheck.getSafetyComplianceScore())
                .isPackageDamaged(qualityCheck.getIsPackageDamaged())
                .isContentCorrect(qualityCheck.getIsContentCorrect())
                .isWeightAccurate(qualityCheck.getIsWeightAccurate())
                .areLabelsCorrect(qualityCheck.getAreLabelsCorrect())
                .isHazardousCompliant(qualityCheck.getIsHazardousCompliant())
                .issuesFound(qualityCheck.getIssuesFound())
                .correctiveActions(qualityCheck.getCorrectiveActions())
                .recheckRequired(qualityCheck.getRecheckRequired())
                .recheckNotes(qualityCheck.getRecheckNotes())
                .approvedForShipment(qualityCheck.getApprovedForShipment())
                .checkNotes(qualityCheck.getCheckNotes())
                .startedAt(qualityCheck.getStartedAt())
                .completedAt(qualityCheck.getCompletedAt())
                .createdAt(qualityCheck.getCreatedAt())
                .updatedAt(qualityCheck.getUpdatedAt())
                .meetsShippingStandards(meetsShippingStandards)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Helper method to convert QualityStandard to ResponseDTO
     */
    private QualityStandardResponseDTO mapToQualityStandardResponseDTO(QualityStandard standard) {
        boolean isCurrentlyEffective = standard.isCurrentlyEffective();

        return QualityStandardResponseDTO.builder()
                .standardId(standard.getStandardId())
                .standardCode(standard.getStandardCode())
                .standardName(standard.getStandardName())
                .checkType(standard.getCheckType())
                .productCategory(standard.getProductCategory())
                .minimumScore(standard.getMinimumScore())
                .targetScore(standard.getTargetScore())
                .weightTolerancePercentage(standard.getWeightTolerancePercentage())
                .dimensionTolerancePercentage(standard.getDimensionTolerancePercentage())
                .requiredDocumentation(standard.getRequiredDocumentation())
                .safetyRequirements(standard.getSafetyRequirements())
                .packagingRequirements(standard.getPackagingRequirements())
                .labelingRequirements(standard.getLabelingRequirements())
                .testingProcedures(standard.getTestingProcedures())
                .isActive(standard.getIsActive())
                .version(standard.getVersion())
                .effectiveDate(standard.getEffectiveDate())
                .expiryDate(standard.getExpiryDate())
                .createdBy(standard.getCreatedBy())
                .notes(standard.getNotes())
                .createdAt(standard.getCreatedAt())
                .updatedAt(standard.getUpdatedAt())
                .isCurrentlyEffective(isCurrentlyEffective)
                .build();
    }
}