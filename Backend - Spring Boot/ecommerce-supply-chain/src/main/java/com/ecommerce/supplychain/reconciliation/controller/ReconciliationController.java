package com.ecommerce.supplychain.reconciliation.controller;

import com.ecommerce.supplychain.reconciliation.dto.*;
import com.ecommerce.supplychain.reconciliation.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation")
@CrossOrigin(origins = "*")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    /**
     * API 1: Create reconciliation report
     * POST /api/reconciliation/reports
     */
    @PostMapping("/reports")
    public ResponseEntity<Map<String, Object>> createReconciliationReport(@Valid @RequestBody ReconciliationDTO reconciliationDTO) {
        try {
            ReconciliationResponseDTO response = reconciliationService.createReconciliationReport(reconciliationDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reconciliation report created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * API 2: Generate comprehensive inventory report
     * POST /api/reconciliation/inventory-report
     */
    @PostMapping("/inventory-report")
    public ResponseEntity<Map<String, Object>> generateInventoryReport(
            @RequestParam Long warehouseId,
            @RequestParam(defaultValue = "SYSTEM_AUDIT") String reportType) {
        try {
            ReconciliationResponseDTO response = reconciliationService.generateInventoryReport(warehouseId, reportType);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Comprehensive inventory report generated successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Complete reconciliation report
     * PUT /api/reconciliation/reports/{reportId}/complete
     */
    @PutMapping("/reports/{reportId}/complete")
    public ResponseEntity<Map<String, Object>> completeReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ReportDTO reportDTO) {
        try {
            ReconciliationResponseDTO response = reconciliationService.completeReport(reportId, reportDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reconciliation report completed successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Approve reconciliation report
     * PUT /api/reconciliation/reports/{reportId}/approve
     */
    @PutMapping("/reports/{reportId}/approve")
    public ResponseEntity<Map<String, Object>> approveReport(
            @PathVariable Long reportId,
            @RequestParam String approvedBy) {
        try {
            ReconciliationResponseDTO response = reconciliationService.approveReport(reportId, approvedBy);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reconciliation report approved successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get all reconciliation reports
     * GET /api/reconciliation/reports
     */
    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> getAllReconciliationReports() {
        List<ReconciliationResponseDTO> reports = reconciliationService.getAllReconciliationReports();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", reports.size());
        response.put("data", reports);

        return ResponseEntity.ok(response);
    }

    /**
     * Get reconciliation report by ID
     * GET /api/reconciliation/reports/{reportId}
     */
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<Map<String, Object>> getReconciliationReportById(@PathVariable Long reportId) {
        try {
            ReconciliationResponseDTO report = reconciliationService.getReconciliationReportById(reportId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", report);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get reports by warehouse
     * GET /api/reconciliation/reports/warehouse/{warehouseId}
     */
    @GetMapping("/reports/warehouse/{warehouseId}")
    public ResponseEntity<Map<String, Object>> getReportsByWarehouseId(@PathVariable Long warehouseId) {
        List<ReconciliationResponseDTO> reports = reconciliationService.getReportsByWarehouseId(warehouseId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("warehouseId", warehouseId);
        response.put("count", reports.size());
        response.put("data", reports);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active reports
     * GET /api/reconciliation/reports/active
     */
    @GetMapping("/reports/active")
    public ResponseEntity<Map<String, Object>> getActiveReports() {
        List<ReconciliationResponseDTO> activeReports = reconciliationService.getActiveReports();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active reconciliation reports (IN_PROGRESS, UNDER_REVIEW)");
        response.put("count", activeReports.size());
        response.put("data", activeReports);

        return ResponseEntity.ok(response);
    }

    /**
     * Get high variance reports
     * GET /api/reconciliation/reports/high-variance
     */
    @GetMapping("/reports/high-variance")
    public ResponseEntity<Map<String, Object>> getHighVarianceReports() {
        List<ReconciliationResponseDTO> highVarianceReports = reconciliationService.getHighVarianceReports();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Reports with variance rate > 5%");
        response.put("count", highVarianceReports.size());
        response.put("data", highVarianceReports);

        return ResponseEntity.ok(response);
    }

    /**
     * Get warehouse performance summary
     * GET /api/reconciliation/performance/{warehouseId}
     */
    @GetMapping("/performance/{warehouseId}")
    public ResponseEntity<Map<String, Object>> getWarehousePerformanceSummary(@PathVariable Long warehouseId) {
        try {
            ReconciliationResponseDTO performance = reconciliationService.getWarehousePerformanceSummary(warehouseId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("warehouseId", warehouseId);
            response.put("data", performance);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Delete reconciliation report (for testing cleanup)
     * DELETE /api/reconciliation/reports/{reportId}
     */
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity<Map<String, Object>> deleteReconciliationReport(@PathVariable Long reportId) {
        try {
            // In real implementation, this would archive or soft delete
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reconciliation report deletion would be implemented here");
            response.put("deletedReportId", reportId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}