package com.ecommerce.supplychain.quality.controller;

import com.ecommerce.supplychain.quality.dto.*;
import com.ecommerce.supplychain.quality.service.QualityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Quality Control Management APIs.
 */
@RestController
@RequestMapping("/api/quality")
@CrossOrigin(origins = "*")
public class QualityController {

    @Autowired
    private QualityService qualityService;

    /**
     * API 1: Create quality check for package
     * POST /api/quality/checks
     */
    @PostMapping("/checks")
    public ResponseEntity<Map<String, Object>> createQualityCheck(@Valid @RequestBody QualityCheckDTO qualityCheckDTO) {
        try {
            QualityCheckResponseDTO response = qualityService.createQualityCheck(qualityCheckDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Quality check created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * API 2: Submit quality check results
     * PUT /api/quality/checks/results
     */
    @PutMapping("/checks/results")
    public ResponseEntity<Map<String, Object>> submitQualityResults(@Valid @RequestBody QualityResultDTO resultDTO) {
        try {
            QualityCheckResponseDTO response = qualityService.submitQualityResults(resultDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Quality results submitted successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Request quality recheck
     * PUT /api/quality/checks/recheck
     */
    @PutMapping("/checks/recheck")
    public ResponseEntity<Map<String, Object>> requestRecheck(@Valid @RequestBody RecheckRequestDTO recheckDTO) {
        try {
            QualityCheckResponseDTO response = qualityService.requestRecheck(recheckDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Quality recheck requested successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all quality checks
     * GET /api/quality/checks
     */
    @GetMapping("/checks")
    public ResponseEntity<Map<String, Object>> getAllQualityChecks() {
        List<QualityCheckResponseDTO> qualityChecks = qualityService.getAllQualityChecks();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", qualityChecks.size());
        response.put("data", qualityChecks);

        return ResponseEntity.ok(response);
    }

    /**
     * Get quality check by ID
     * GET /api/quality/checks/{checkId}
     */
    @GetMapping("/checks/{checkId}")
    public ResponseEntity<Map<String, Object>> getQualityCheckById(@PathVariable Long checkId) {
        try {
            QualityCheckResponseDTO qualityCheck = qualityService.getQualityCheckById(checkId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", qualityCheck);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get quality checks by package
     * GET /api/quality/checks/package/{packageId}
     */
    @GetMapping("/checks/package/{packageId}")
    public ResponseEntity<Map<String, Object>> getQualityChecksByPackageId(@PathVariable Long packageId) {
        List<QualityCheckResponseDTO> qualityChecks = qualityService.getQualityChecksByPackageId(packageId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("packageId", packageId);
        response.put("count", qualityChecks.size());
        response.put("data", qualityChecks);

        return ResponseEntity.ok(response);
    }

    /**
     * Get quality checks by status
     * GET /api/quality/checks/status/{status}
     */
    @GetMapping("/checks/status/{status}")
    public ResponseEntity<Map<String, Object>> getQualityChecksByStatus(@PathVariable String status) {
        List<QualityCheckResponseDTO> qualityChecks = qualityService.getQualityChecksByStatus(status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status);
        response.put("count", qualityChecks.size());
        response.put("data", qualityChecks);

        return ResponseEntity.ok(response);
    }

    /**
     * Get approved quality checks
     * GET /api/quality/checks/approved
     */
    @GetMapping("/checks/approved")
    public ResponseEntity<Map<String, Object>> getApprovedQualityChecks() {
        List<QualityCheckResponseDTO> approvedChecks = qualityService.getApprovedQualityChecks();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Quality checks approved for shipment");
        response.put("count", approvedChecks.size());
        response.put("data", approvedChecks);

        return ResponseEntity.ok(response);
    }

    /**
     * Get checks requiring recheck
     * GET /api/quality/checks/require-recheck
     */
    @GetMapping("/checks/require-recheck")
    public ResponseEntity<Map<String, Object>> getChecksRequiringRecheck() {
        List<QualityCheckResponseDTO> recheckChecks = qualityService.getChecksRequiringRecheck();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Quality checks requiring recheck");
        response.put("count", recheckChecks.size());
        response.put("data", recheckChecks);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all quality standards
     * GET /api/quality/standards
     */
    @GetMapping("/standards")
    public ResponseEntity<Map<String, Object>> getAllQualityStandards() {
        List<QualityStandardResponseDTO> qualityStandards = qualityService.getAllQualityStandards();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", qualityStandards.size());
        response.put("data", qualityStandards);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active quality standards
     * GET /api/quality/standards/active
     */
    @GetMapping("/standards/active")
    public ResponseEntity<Map<String, Object>> getActiveQualityStandards() {
        List<QualityStandardResponseDTO> activeStandards = qualityService.getActiveQualityStandards();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active quality standards");
        response.put("count", activeStandards.size());
        response.put("data", activeStandards);

        return ResponseEntity.ok(response);
    }

    /**
     * Get quality standards by type
     * GET /api/quality/standards/type/{checkType}
     */
    @GetMapping("/standards/type/{checkType}")
    public ResponseEntity<Map<String, Object>> getQualityStandardsByType(@PathVariable String checkType) {
        List<QualityStandardResponseDTO> standards = qualityService.getQualityStandardsByType(checkType);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("checkType", checkType);
        response.put("count", standards.size());
        response.put("data", standards);

        return ResponseEntity.ok(response);
    }

    /**
     * Validate package against quality standards
     * POST /api/quality/checks/{checkId}/validate-standards
     */
    @PostMapping("/checks/{checkId}/validate-standards")
    public ResponseEntity<Map<String, Object>> validateAgainstStandards(@PathVariable Long checkId) {
        try {
            QualityCheckResponseDTO response = qualityService.validateAgainstStandards(checkId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Package validated against quality standards");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get quality dashboard statistics
     * GET /api/quality/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getQualityDashboard() {
        List<QualityCheckResponseDTO> allChecks = qualityService.getAllQualityChecks();
        List<QualityCheckResponseDTO> approvedChecks = qualityService.getApprovedQualityChecks();
        List<QualityCheckResponseDTO> recheckChecks = qualityService.getChecksRequiringRecheck();

        long totalChecks = allChecks.size();
        long passedChecks = approvedChecks.size();
        long failedChecks = allChecks.stream()
                .filter(check -> "FAIL".equals(check.getOverallResult()))
                .count();
        double passRate = totalChecks > 0 ? (double) passedChecks / totalChecks * 100 : 0.0;

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalChecks", totalChecks);
        dashboard.put("passedChecks", passedChecks);
        dashboard.put("failedChecks", failedChecks);
        dashboard.put("recheckRequired", recheckChecks.size());
        dashboard.put("passRate", Math.round(passRate * 100.0) / 100.0);
        dashboard.put("averageScore", calculateAverageScore(allChecks));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", dashboard);

        return ResponseEntity.ok(response);
    }

    /**
     * Calculate average quality score
     */
    private Double calculateAverageScore(List<QualityCheckResponseDTO> checks) {
        if (checks.isEmpty()) return 0.0;

        double totalScore = checks.stream()
                .mapToDouble(check -> check.getScorePercentage() != null ? check.getScorePercentage() : 0.0)
                .sum();

        return Math.round((totalScore / checks.size()) * 100.0) / 100.0;
    }
}