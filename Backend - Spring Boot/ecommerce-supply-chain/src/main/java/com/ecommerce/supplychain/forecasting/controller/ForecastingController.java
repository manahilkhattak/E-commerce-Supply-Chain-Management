package com.ecommerce.supplychain.forecasting.controller;

import com.ecommerce.supplychain.forecasting.dto.*;
import com.ecommerce.supplychain.forecasting.service.ForecastingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Demand Forecasting and Reorder Planning APIs.
 */
@RestController
@RequestMapping("/api/forecasting")
@CrossOrigin(origins = "*")
public class ForecastingController {

    @Autowired
    private ForecastingService forecastingService;

    /**
     * API 1: Generate demand forecast
     * POST /api/forecasting/demand-forecasts
     */
    @PostMapping("/demand-forecasts")
    public ResponseEntity<Map<String, Object>> generateDemandForecast(@Valid @RequestBody ForecastDTO forecastDTO) {
        try {
            ForecastResponseDTO response = forecastingService.generateDemandForecast(forecastDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Demand forecast generated successfully");
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
     * API 2: Generate reorder plan
     * POST /api/forecasting/reorder-plans
     */
    @PostMapping("/reorder-plans")
    public ResponseEntity<Map<String, Object>> generateReorderPlan(@Valid @RequestBody ReorderPlanDTO reorderPlanDTO) {
        try {
            ReorderPlanResponseDTO response = forecastingService.generateReorderPlan(reorderPlanDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reorder plan generated successfully");
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
     * Convert reorder plan to purchase order
     * PUT /api/forecasting/reorder-plans/convert-to-po
     */
    @PutMapping("/reorder-plans/convert-to-po")
    public ResponseEntity<Map<String, Object>> convertToPurchaseOrder(@Valid @RequestBody ConvertToPODTO convertDTO) {
        try {
            ReorderPlanResponseDTO response = forecastingService.convertToPurchaseOrder(convertDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reorder plan converted to purchase order successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all demand forecasts
     * GET /api/forecasting/demand-forecasts
     */
    @GetMapping("/demand-forecasts")
    public ResponseEntity<Map<String, Object>> getAllDemandForecasts() {
        List<ForecastResponseDTO> forecasts = forecastingService.getAllDemandForecasts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", forecasts.size());
        response.put("data", forecasts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get forecast by ID
     * GET /api/forecasting/demand-forecasts/{forecastId}
     */
    @GetMapping("/demand-forecasts/{forecastId}")
    public ResponseEntity<Map<String, Object>> getForecastById(@PathVariable Long forecastId) {
        try {
            ForecastResponseDTO forecast = forecastingService.getForecastById(forecastId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", forecast);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get forecasts by product
     * GET /api/forecasting/demand-forecasts/product/{productId}
     */
    @GetMapping("/demand-forecasts/product/{productId}")
    public ResponseEntity<Map<String, Object>> getForecastsByProductId(@PathVariable Long productId) {
        List<ForecastResponseDTO> forecasts = forecastingService.getForecastsByProductId(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("productId", productId);
        response.put("count", forecasts.size());
        response.put("data", forecasts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active forecasts
     * GET /api/forecasting/demand-forecasts/active
     */
    @GetMapping("/demand-forecasts/active")
    public ResponseEntity<Map<String, Object>> getActiveForecasts() {
        List<ForecastResponseDTO> forecasts = forecastingService.getActiveForecasts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active demand forecasts");
        response.put("count", forecasts.size());
        response.put("data", forecasts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all reorder plans
     * GET /api/forecasting/reorder-plans
     */
    @GetMapping("/reorder-plans")
    public ResponseEntity<Map<String, Object>> getAllReorderPlans() {
        List<ReorderPlanResponseDTO> reorderPlans = forecastingService.getAllReorderPlans();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", reorderPlans.size());
        response.put("data", reorderPlans);

        return ResponseEntity.ok(response);
    }

    /**
     * Get reorder plan by ID
     * GET /api/forecasting/reorder-plans/{planId}
     */
    @GetMapping("/reorder-plans/{planId}")
    public ResponseEntity<Map<String, Object>> getReorderPlanById(@PathVariable Long planId) {
        try {
            ReorderPlanResponseDTO reorderPlan = forecastingService.getReorderPlanById(planId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reorderPlan);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get pending reorder plans
     * GET /api/forecasting/reorder-plans/pending
     */
    @GetMapping("/reorder-plans/pending")
    public ResponseEntity<Map<String, Object>> getPendingReorderPlans() {
        List<ReorderPlanResponseDTO> pendingPlans = forecastingService.getPendingReorderPlans();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Pending reorder plans (not converted to PO)");
        response.put("count", pendingPlans.size());
        response.put("data", pendingPlans);

        return ResponseEntity.ok(response);
    }

    /**
     * Get critical reorder plans
     * GET /api/forecasting/reorder-plans/critical
     */
    @GetMapping("/reorder-plans/critical")
    public ResponseEntity<Map<String, Object>> getCriticalReorderPlans() {
        List<ReorderPlanResponseDTO> criticalPlans = forecastingService.getCriticalReorderPlans();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Critical reorder plans requiring immediate attention");
        response.put("count", criticalPlans.size());
        response.put("data", criticalPlans);

        return ResponseEntity.ok(response);
    }

    /**
     * Update forecast with actual demand
     * PUT /api/forecasting/demand-forecasts/{forecastId}/actual-demand
     */
    @PutMapping("/demand-forecasts/{forecastId}/actual-demand")
    public ResponseEntity<Map<String, Object>> updateForecastWithActualDemand(
            @PathVariable Long forecastId,
            @RequestParam Integer actualDemand) {
        try {
            ForecastResponseDTO response = forecastingService.updateForecastWithActualDemand(forecastId, actualDemand);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Forecast updated with actual demand successfully");
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
     * Run automated forecasting
     * POST /api/forecasting/automated-forecasting
     */
    @PostMapping("/automated-forecasting")
    public ResponseEntity<Map<String, Object>> runAutomatedForecasting() {
        List<ForecastResponseDTO> newForecasts = forecastingService.runAutomatedForecasting();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Automated forecasting completed");
        response.put("newForecastsGenerated", newForecasts.size());
        response.put("data", newForecasts);

        return ResponseEntity.ok(response);
    }
}