package com.ecommerce.supplychain.reconciliation.service;

import com.ecommerce.supplychain.reconciliation.dto.*;
import com.ecommerce.supplychain.reconciliation.model.ReconciliationReport;
import com.ecommerce.supplychain.reconciliation.model.InventoryDiscrepancy;
import com.ecommerce.supplychain.reconciliation.repository.ReconciliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReconciliationService {

    @Autowired
    private ReconciliationRepository reconciliationRepository;

    /**
     * API 1: Create reconciliation report
     */
    @Transactional
    public ReconciliationResponseDTO createReconciliationReport(ReconciliationDTO reconciliationDTO) {
        // Generate unique report number
        String reportNumber = generateReportNumber();

        // Create reconciliation report
        ReconciliationReport report = new ReconciliationReport();
        report.setReportNumber(reportNumber);
        report.setReportType(reconciliationDTO.getReportType());
        report.setWarehouseId(reconciliationDTO.getWarehouseId());
        report.setWarehouseName(reconciliationDTO.getWarehouseName());
        report.setReportPeriodStart(reconciliationDTO.getReportPeriodStart());
        report.setReportPeriodEnd(reconciliationDTO.getReportPeriodEnd());
        report.setConductedBy(reconciliationDTO.getConductedBy() != null ? reconciliationDTO.getConductedBy() : "system_auto");
        report.setNotes(reconciliationDTO.getNotes());

        ReconciliationReport savedReport = reconciliationRepository.save(report);

        // Automatically analyze inventory for discrepancies
        analyzeInventoryDiscrepancies(savedReport);

        return mapToReconciliationResponseDTO(savedReport);
    }

    /**
     * API 2: Generate comprehensive inventory report
     */
    public ReconciliationResponseDTO generateInventoryReport(Long warehouseId, String reportType) {
        // Create report for current inventory status
        ReconciliationDTO reportDTO = new ReconciliationDTO();
        reportDTO.setReportType(reportType);
        reportDTO.setWarehouseId(warehouseId);
        reportDTO.setReportPeriodStart(LocalDateTime.now().minusDays(30)); // Last 30 days
        reportDTO.setReportPeriodEnd(LocalDateTime.now());
        reportDTO.setConductedBy("system_auto");
        reportDTO.setNotes("Automated inventory health report");

        ReconciliationReport report = new ReconciliationReport();
        report.setReportNumber(generateReportNumber());
        report.setReportType(reportType);
        report.setWarehouseId(warehouseId);
        report.setReportPeriodStart(reportDTO.getReportPeriodStart());
        report.setReportPeriodEnd(reportDTO.getReportPeriodEnd());
        report.setConductedBy("system_auto");
        report.setNotes("Comprehensive inventory analysis report");

        // Analyze inventory from all integrated processes
        performComprehensiveAnalysis(report);

        ReconciliationReport savedReport = reconciliationRepository.save(report);

        return mapToReconciliationResponseDTO(savedReport);
    }

    /**
     * Complete reconciliation report
     */
    @Transactional
    public ReconciliationResponseDTO completeReport(Long reportId, ReportDTO reportDTO) {
        ReconciliationReport report = reconciliationRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reconciliation report not found with ID: " + reportId));

        report.setSummaryFindings(reportDTO.getSummaryFindings());
        report.setCorrectiveActions(reportDTO.getCorrectiveActions());
        report.setPreventiveMeasures(reportDTO.getPreventiveMeasures());
        report.completeReport();

        if (reportDTO.getReviewedBy() != null) {
            report.setReviewedBy(reportDTO.getReviewedBy());
            report.setReviewedDate(LocalDateTime.now());
        }

        ReconciliationReport updatedReport = reconciliationRepository.save(report);

        // Handle completion integration
        handleReportCompletionIntegration(updatedReport);

        return mapToReconciliationResponseDTO(updatedReport);
    }

    /**
     * Approve reconciliation report
     */
    @Transactional
    public ReconciliationResponseDTO approveReport(Long reportId, String approvedBy) {
        ReconciliationReport report = reconciliationRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reconciliation report not found with ID: " + reportId));

        report.approveReport(approvedBy);

        ReconciliationReport updatedReport = reconciliationRepository.save(report);

        // Handle approval integration
        handleReportApprovalIntegration(updatedReport);

        return mapToReconciliationResponseDTO(updatedReport);
    }

    /**
     * Add discrepancy to report
     */
    @Transactional
    public ReconciliationResponseDTO addDiscrepancy(Long reportId, InventoryDiscrepancy discrepancy) {
        ReconciliationReport report = reconciliationRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reconciliation report not found with ID: " + reportId));

        report.addDiscrepancy(discrepancy);

        ReconciliationReport updatedReport = reconciliationRepository.save(report);

        return mapToReconciliationResponseDTO(updatedReport);
    }

    /**
     * Get all reconciliation reports
     */
    public List<ReconciliationResponseDTO> getAllReconciliationReports() {
        return reconciliationRepository.findAll().stream()
                .map(this::mapToReconciliationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get reconciliation report by ID
     */
    public ReconciliationResponseDTO getReconciliationReportById(Long reportId) {
        ReconciliationReport report = reconciliationRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reconciliation report not found with ID: " + reportId));
        return mapToReconciliationResponseDTO(report);
    }

    /**
     * Get reports by warehouse
     */
    public List<ReconciliationResponseDTO> getReportsByWarehouseId(Long warehouseId) {
        return reconciliationRepository.findByWarehouseId(warehouseId).stream()
                .map(this::mapToReconciliationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active reports
     */
    public List<ReconciliationResponseDTO> getActiveReports() {
        return reconciliationRepository.findActiveReports().stream()
                .map(this::mapToReconciliationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get high variance reports
     */
    public List<ReconciliationResponseDTO> getHighVarianceReports() {
        return reconciliationRepository.findHighVarianceReports(5.0).stream() // >5% variance
                .map(this::mapToReconciliationResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get warehouse performance summary
     */
    public ReconciliationResponseDTO getWarehousePerformanceSummary(Long warehouseId) {
        // This would aggregate data from multiple reports
        // For now, we'll return the latest report as summary

        return reconciliationRepository.findLatestCycleCountByWarehouse(warehouseId)
                .map(this::mapToReconciliationResponseDTO)
                .orElse(ReconciliationResponseDTO.builder()
                        .warehouseId(warehouseId)
                        .reportType("PERFORMANCE_SUMMARY")
                        .accuracyRate(100.0)
                        .varianceRate(0.0)
                        .build());
    }

    /**
     * Analyze inventory discrepancies by comparing system records with physical counts
     */
    private void analyzeInventoryDiscrepancies(ReconciliationReport report) {
        // This would integrate with Process 6 (Inventory) to get system quantities
        // and compare with physical counts from Process 8 (Warehouse)

        System.out.println("Integration: Analyzing inventory discrepancies for warehouse " + report.getWarehouseId());

        // Simulate finding some discrepancies
        if ("CYCLE_COUNT".equals(report.getReportType())) {
            // Add sample discrepancies for demonstration
            addSampleDiscrepancies(report);
        }

        // Integration with Process 6 - Get system inventory data
        List<InventoryRecord> systemInventory = getSystemInventoryData(report.getWarehouseId());

        // Integration with Process 8 - Get physical count data
        List<PhysicalCount> physicalCounts = getPhysicalCountData(report.getWarehouseId());

        // Compare and identify discrepancies
        identifyDiscrepancies(report, systemInventory, physicalCounts);
    }

    /**
     * Perform comprehensive inventory analysis across all processes
     */
    private void performComprehensiveAnalysis(ReconciliationReport report) {
        System.out.println("Integration: Performing comprehensive inventory analysis for warehouse " + report.getWarehouseId());

        // Integration with ALL processes for complete analysis

        // Process 6: Current inventory status
        analyzeCurrentInventoryStatus(report);

        // Process 7: Demand forecasting accuracy
        analyzeForecastingAccuracy(report);

        // Process 8: Warehouse storage efficiency
        analyzeStorageEfficiency(report);

        // Process 9: Picking accuracy
        analyzePickingAccuracy(report);

        // Process 12: Delivery performance
        analyzeDeliveryPerformance(report);

        // Process 13: Exception impact
        analyzeExceptionImpact(report);

        // Process 14: Returns analysis
        analyzeReturnsImpact(report);

        // Generate comprehensive findings
        generateComprehensiveFindings(report);
    }

    /**
     * Integration methods for comprehensive analysis
     */
    private void analyzeCurrentInventoryStatus(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 6 - Current Inventory Status");
        // This would call InventoryService to get current stock levels, turnover rates, etc.
    }

    private void analyzeForecastingAccuracy(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 7 - Demand Forecasting Accuracy");
        // This would call ForecastingService to compare forecasts vs actuals
    }

    private void analyzeStorageEfficiency(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 8 - Warehouse Storage Efficiency");
        // This would call WarehouseService to analyze space utilization, location accuracy
    }

    private void analyzePickingAccuracy(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 9 - Picking Accuracy");
        // This would call PickingService to analyze pick accuracy and efficiency
    }

    private void analyzeDeliveryPerformance(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 12 - Delivery Performance");
        // This would call TrackingService to analyze on-time delivery and exceptions
    }

    private void analyzeExceptionImpact(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 13 - Exception Impact");
        // This would call ExceptionHandlingService to analyze exception rates and root causes
    }

    private void analyzeReturnsImpact(ReconciliationReport report) {
        System.out.println("Integration: Analyzing Process 14 - Returns Impact");
        // This would call ReturnsService to analyze return rates and reasons
    }

    private void generateComprehensiveFindings(ReconciliationReport report) {
        // Generate summary based on all integrated analyses
        StringBuilder findings = new StringBuilder();
        findings.append("Comprehensive Inventory Health Report\n");
        findings.append("=====================================\n");
        findings.append("• Inventory Accuracy: ").append(report.getAccuracyRate() != null ? String.format("%.2f%%", report.getAccuracyRate()) : "N/A").append("\n");
        findings.append("• Variance Rate: ").append(report.getVarianceRate() != null ? String.format("%.2f%%", report.getVarianceRate()) : "N/A").append("\n");
        findings.append("• Total Discrepancies: ").append(report.getTotalDiscrepanciesFound()).append("\n");
        findings.append("• Financial Impact: $").append(report.getDiscrepancyValue() != null ? String.format("%.2f", report.getDiscrepancyValue()) : "0.00").append("\n");

        report.setSummaryFindings(findings.toString());
        report.setCorrectiveActions("Implement regular cycle counting, improve receiving processes, enhance employee training");
        report.setPreventiveMeasures("Automated inventory tracking, real-time stock alerts, regular audits");
    }

    /**
     * Handle integration when report is completed
     */
    private void handleReportCompletionIntegration(ReconciliationReport report) {
        // Integration with Process 6 - Update inventory records
        updateInventoryRecords(report);

        // Integration with Management system - Notify stakeholders
        notifyStakeholders(report);

        // Integration with Analytics system - Log report data
        logAnalyticsData(report);

        System.out.println("Integration: Report completion processed for " + report.getReportNumber());
    }

    /**
     * Handle integration when report is approved
     */
    private void handleReportApprovalIntegration(ReconciliationReport report) {
        // Integration with Process 6 - Apply system adjustments
        applySystemAdjustments(report);

        // Integration with Finance system - Update financial records
        updateFinancialRecords(report);

        // Integration with Process 8 - Update warehouse processes
        updateWarehouseProcesses(report);

        System.out.println("Integration: Report approval processed for " + report.getReportNumber());
    }

    /**
     * Additional integration methods
     */
    private List<InventoryRecord> getSystemInventoryData(Long warehouseId) {
        System.out.println("Integration: Getting Process 6 system inventory data for warehouse " + warehouseId);
        return List.of(); // Would return actual data from InventoryService
    }

    private List<PhysicalCount> getPhysicalCountData(Long warehouseId) {
        System.out.println("Integration: Getting Process 8 physical count data for warehouse " + warehouseId);
        return List.of(); // Would return actual data from WarehouseService
    }

    private void identifyDiscrepancies(ReconciliationReport report, List<InventoryRecord> systemData, List<PhysicalCount> physicalData) {
        System.out.println("Integration: Identifying discrepancies between system and physical counts");
        // Complex logic to compare and identify discrepancies
    }

    private void updateInventoryRecords(ReconciliationReport report) {
        System.out.println("Integration: Updating Process 6 inventory records based on report " + report.getReportNumber());
    }

    private void notifyStakeholders(ReconciliationReport report) {
        System.out.println("Integration: Notifying stakeholders about report " + report.getReportNumber());
    }

    private void logAnalyticsData(ReconciliationReport report) {
        System.out.println("Integration: Logging analytics data for report " + report.getReportNumber());
    }

    private void applySystemAdjustments(ReconciliationReport report) {
        System.out.println("Integration: Applying system adjustments for report " + report.getReportNumber());
    }

    private void updateFinancialRecords(ReconciliationReport report) {
        System.out.println("Integration: Updating financial records for report " + report.getReportNumber());
    }

    private void updateWarehouseProcesses(ReconciliationReport report) {
        System.out.println("Integration: Updating Process 8 warehouse processes based on report " + report.getReportNumber());
    }

    /**
     * Add sample discrepancies for demonstration
     */
    private void addSampleDiscrepancies(ReconciliationReport report) {
        // Sample discrepancy 1
        InventoryDiscrepancy discrepancy1 = new InventoryDiscrepancy();
        discrepancy1.setProductId(101L);
        discrepancy1.setProductName("Wireless Mouse");
        discrepancy1.setProductSku("WM-BT-001");
        discrepancy1.setWarehouseId(report.getWarehouseId());
        discrepancy1.setShelfLocationId(1L);
        discrepancy1.setLocationCode("A-01-B-02");
        discrepancy1.setExpectedQuantity(50);
        discrepancy1.setActualQuantity(45);
        discrepancy1.setUnitCost(29.99);
        discrepancy1.setDiscrepancyCategory("COUNT_ERROR");
        discrepancy1.setRootCause("Miscount during previous receiving");
        discrepancy1.setCorrectiveAction("Recount and adjust system records");

        // Sample discrepancy 2
        InventoryDiscrepancy discrepancy2 = new InventoryDiscrepancy();
        discrepancy2.setProductId(102L);
        discrepancy2.setProductName("USB-C Cable");
        discrepancy2.setProductSku("USBC-002");
        discrepancy2.setWarehouseId(report.getWarehouseId());
        discrepancy2.setShelfLocationId(2L);
        discrepancy2.setLocationCode("A-02-C-01");
        discrepancy2.setExpectedQuantity(100);
        discrepancy2.setActualQuantity(105);
        discrepancy2.setUnitCost(12.00);
        discrepancy2.setDiscrepancyCategory("OVERAGE");
        discrepancy2.setRootCause("Extra units received but not recorded");
        discrepancy2.setCorrectiveAction("Verify with supplier and update records");

        report.addDiscrepancy(discrepancy1);
        report.addDiscrepancy(discrepancy2);
    }

    /**
     * Generate unique report number
     */
    private String generateReportNumber() {
        return "REC-" + System.currentTimeMillis();
    }

    /**
     * Map ReconciliationReport to ResponseDTO
     */
    private ReconciliationResponseDTO mapToReconciliationResponseDTO(ReconciliationReport report) {
        boolean isHighVariance = report.isHighVariance();
        String accuracyGrade = report.getAccuracyGrade();
        String performanceRating = calculatePerformanceRating(report);

        List<DiscrepancyResponseDTO> discrepancyDTOs = report.getDiscrepancies().stream()
                .map(this::mapToDiscrepancyResponseDTO)
                .collect(Collectors.toList());

        return ReconciliationResponseDTO.builder()
                .reportId(report.getReportId())
                .reportNumber(report.getReportNumber())
                .reportType(report.getReportType())
                .warehouseId(report.getWarehouseId())
                .warehouseName(report.getWarehouseName())
                .reportPeriodStart(report.getReportPeriodStart())
                .reportPeriodEnd(report.getReportPeriodEnd())
                .conductedBy(report.getConductedBy())
                .conductedDate(report.getConductedDate())
                .totalProductsCounted(report.getTotalProductsCounted())
                .totalSkuCounted(report.getTotalSkuCounted())
                .totalExpectedQuantity(report.getTotalExpectedQuantity())
                .totalActualQuantity(report.getTotalActualQuantity())
                .totalDiscrepanciesFound(report.getTotalDiscrepanciesFound())
                .discrepancyValue(report.getDiscrepancyValue())
                .accuracyRate(report.getAccuracyRate())
                .varianceRate(report.getVarianceRate())
                .reportStatus(report.getReportStatus())
                .reviewedBy(report.getReviewedBy())
                .reviewedDate(report.getReviewedDate())
                .approvalStatus(report.getApprovalStatus())
                .approvedBy(report.getApprovedBy())
                .approvedDate(report.getApprovedDate())
                .summaryFindings(report.getSummaryFindings())
                .correctiveActions(report.getCorrectiveActions())
                .preventiveMeasures(report.getPreventiveMeasures())
                .notes(report.getNotes())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .isHighVariance(isHighVariance)
                .accuracyGrade(accuracyGrade)
                .performanceRating(performanceRating)
                .discrepancies(discrepancyDTOs)
                .build();
    }

    /**
     * Map InventoryDiscrepancy to ResponseDTO
     */
    private DiscrepancyResponseDTO mapToDiscrepancyResponseDTO(InventoryDiscrepancy discrepancy) {
        boolean requiresImmediateAttention = discrepancy.requiresImmediateAttention();
        double variancePercentage = discrepancy.getVariancePercentage();
        String statusColor = getDiscrepancyStatusColor(discrepancy.getResolutionStatus());
        String impactLevel = getImpactLevel(discrepancy.getVarianceValue());

        return DiscrepancyResponseDTO.builder()
                .discrepancyId(discrepancy.getDiscrepancyId())
                .reportId(discrepancy.getReconciliationReport().getReportId())
                .productId(discrepancy.getProductId())
                .productName(discrepancy.getProductName())
                .productSku(discrepancy.getProductSku())
                .warehouseId(discrepancy.getWarehouseId())
                .shelfLocationId(discrepancy.getShelfLocationId())
                .locationCode(discrepancy.getLocationCode())
                .expectedQuantity(discrepancy.getExpectedQuantity())
                .actualQuantity(discrepancy.getActualQuantity())
                .varianceQuantity(discrepancy.getVarianceQuantity())
                .varianceType(discrepancy.getVarianceType())
                .unitCost(discrepancy.getUnitCost())
                .varianceValue(discrepancy.getVarianceValue())
                .discrepancySeverity(discrepancy.getDiscrepancySeverity())
                .discrepancyCategory(discrepancy.getDiscrepancyCategory())
                .rootCause(discrepancy.getRootCause())
                .correctiveAction(discrepancy.getCorrectiveAction())
                .assignedTo(discrepancy.getAssignedTo())
                .resolutionStatus(discrepancy.getResolutionStatus())
                .resolutionDate(discrepancy.getResolutionDate())
                .resolvedBy(discrepancy.getResolvedBy())
                .resolutionNotes(discrepancy.getResolutionNotes())
                .isAdjustedInSystem(discrepancy.getIsAdjustedInSystem())
                .systemAdjustmentDate(discrepancy.getSystemAdjustmentDate())
                .adjustedBy(discrepancy.getAdjustedBy())
                .notes(discrepancy.getNotes())
                .createdAt(discrepancy.getCreatedAt())
                .updatedAt(discrepancy.getUpdatedAt())
                .requiresImmediateAttention(requiresImmediateAttention)
                .variancePercentage(variancePercentage)
                .statusColor(statusColor)
                .impactLevel(impactLevel)
                .build();
    }

    /**
     * Calculate performance rating
     */
    private String calculatePerformanceRating(ReconciliationReport report) {
        if (report.getAccuracyRate() == null) return "N/A";

        if (report.getAccuracyRate() >= 99.5) return "EXCELLENT";
        if (report.getAccuracyRate() >= 99.0) return "VERY GOOD";
        if (report.getAccuracyRate() >= 98.0) return "GOOD";
        if (report.getAccuracyRate() >= 95.0) return "FAIR";
        return "POOR";
    }

    /**
     * Get discrepancy status color
     */
    private String getDiscrepancyStatusColor(String status) {
        switch (status) {
            case "OPEN": return "red";
            case "IN_PROGRESS": return "orange";
            case "RESOLVED": return "green";
            case "CLOSED": return "gray";
            default: return "blue";
        }
    }

    /**
     * Get impact level
     */
    private String getImpactLevel(Double value) {
        if (value == null) return "LOW";
        if (value >= 1000) return "CRITICAL";
        if (value >= 500) return "HIGH";
        if (value >= 100) return "MEDIUM";
        return "LOW";
    }

    // Helper classes for integration
    private static class InventoryRecord {
        // Would contain inventory data from Process 6
    }

    private static class PhysicalCount {
        // Would contain physical count data from Process 8
    }
}