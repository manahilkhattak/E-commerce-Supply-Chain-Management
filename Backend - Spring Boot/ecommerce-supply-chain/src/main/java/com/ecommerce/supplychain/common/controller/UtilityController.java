package com.ecommerce.supplychain.common.controller;

import com.ecommerce.supplychain.common.util.DateUtil;
import com.ecommerce.supplychain.common.util.ValidationUtil;
import com.ecommerce.supplychain.common.util.InventoryCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/common/utils")
public class UtilityController {

    /**
     * API 1: Validate various data formats
     * POST /api/common/utils/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateData(@RequestBody Map<String, String> validationRequests) {
        log.info("Validation request received for {} fields", validationRequests.size());

        Map<String, Object> validationResults = new HashMap<>();
        validationResults.put("timestamp", DateUtil.getCurrentTimestamp());
        validationResults.put("totalValidations", validationRequests.size());

        Map<String, Boolean> results = new HashMap<>();
        int validCount = 0;

        for (Map.Entry<String, String> entry : validationRequests.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            boolean isValid = false;

            switch (field.toLowerCase()) {
                case "email":
                    isValid = ValidationUtil.isValidEmail(value);
                    break;
                case "phone":
                    isValid = ValidationUtil.isValidPhone(value);
                    break;
                case "sku":
                    isValid = ValidationUtil.isValidSku(value);
                    break;
                case "trackingnumber":
                    isValid = ValidationUtil.isValidTrackingNumber(value);
                    break;
                case "zipcode":
                    isValid = ValidationUtil.isValidZipCode(value);
                    break;
                default:
                    isValid = ValidationUtil.isNotBlank(value);
                    break;
            }

            results.put(field, isValid);
            if (isValid) validCount++;
        }

        validationResults.put("results", results);
        validationResults.put("validCount", validCount);
        validationResults.put("invalidCount", validationRequests.size() - validCount);
        validationResults.put("successRate", ((double) validCount / validationRequests.size()) * 100);

        return ResponseEntity.ok(validationResults);
    }

    /**
     * API 2: Generate system reports and calculations
     * POST /api/common/utils/calculate
     */
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> performCalculations(@RequestBody Map<String, Object> calculationRequest) {
        log.info("Calculation request received: {}", calculationRequest.get("type"));

        String calculationType = (String) calculationRequest.get("type");
        Map<String, Object> parameters = (Map<String, Object>) calculationRequest.get("parameters");
        Map<String, Object> results = new HashMap<>();

        results.put("type", calculationType);
        results.put("timestamp", DateUtil.getCurrentTimestamp());

        switch (calculationType.toUpperCase()) {
            case "EOQ":
                results.put("result", calculateEOQ(parameters));
                break;
            case "REORDER_POINT":
                results.put("result", calculateReorderPoint(parameters));
                break;
            case "INVENTORY_TURNOVER":
                results.put("result", calculateInventoryTurnover(parameters));
                break;
            case "SERVICE_LEVEL":
                results.put("result", calculateServiceLevel(parameters));
                break;
            case "DATE_DIFFERENCE":
                results.put("result", calculateDateDifference(parameters));
                break;
            default:
                results.put("error", "Unsupported calculation type: " + calculationType);
                break;
        }

        return ResponseEntity.ok(results);
    }

    /**
     * Get system constants and configuration
     * GET /api/common/utils/constants
     */
    @GetMapping("/constants")
    public ResponseEntity<Map<String, Object>> getSystemConstants() {
        Map<String, Object> constants = new HashMap<>();

        // Order status constants
        constants.put("ORDER_STATUS", getOrderStatusConstants());

        // Shipment status constants
        constants.put("SHIPMENT_STATUS", getShipmentStatusConstants());

        // Quality status constants
        constants.put("QUALITY_STATUS", getQualityStatusConstants());

        // Return status constants
        constants.put("RETURN_STATUS", getReturnStatusConstants());

        // Exception status constants
        constants.put("EXCEPTION_STATUS", getExceptionStatusConstants());

        // System configuration
        constants.put("SYSTEM_CONFIG", getSystemConfiguration());

        return ResponseEntity.ok(constants);
    }

    /**
     * Format dates and times
     * POST /api/common/utils/format-date
     */
    @PostMapping("/format-date")
    public ResponseEntity<Map<String, Object>> formatDate(@RequestBody Map<String, Object> dateRequest) {
        String dateString = (String) dateRequest.get("date");
        String format = (String) dateRequest.get("format");

        Map<String, Object> result = new HashMap<>();

        try {
            LocalDateTime dateTime = DateUtil.parseDateTime(dateString);

            if (dateTime != null) {
                result.put("original", dateString);
                result.put("formatted", DateUtil.formatDateTime(dateTime));
                result.put("dateOnly", DateUtil.formatDate(dateTime));
                result.put("timeOnly", DateUtil.formatTime(dateTime));
                result.put("isPast", DateUtil.isPast(dateTime));
                result.put("isFuture", DateUtil.isFuture(dateTime));
            } else {
                result.put("error", "Invalid date format");
            }
        } catch (Exception e) {
            result.put("error", "Failed to parse date: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    // Calculation helper methods
    private Map<String, Object> calculateEOQ(Map<String, Object> params) {
        double annualDemand = ((Number) params.get("annualDemand")).doubleValue();
        double orderingCost = ((Number) params.get("orderingCost")).doubleValue();
        double holdingCost = ((Number) params.get("holdingCost")).doubleValue();

        double eoq = InventoryCalculator.calculateEOQ(
                annualDemand, orderingCost, holdingCost
        );

        Map<String, Object> result = new HashMap<>();
        result.put("economicOrderQuantity", Math.round(eoq));
        result.put("annualDemand", annualDemand);
        result.put("orderingCost", orderingCost);
        result.put("holdingCost", holdingCost);

        return result;
    }

    private Map<String, Object> calculateReorderPoint(Map<String, Object> params) {
        double leadTimeDemand = ((Number) params.get("leadTimeDemand")).doubleValue();
        double safetyStock = ((Number) params.get("safetyStock")).doubleValue();

        double reorderPoint = InventoryCalculator.calculateReorderPoint(
                leadTimeDemand, safetyStock
        );

        Map<String, Object> result = new HashMap<>();
        result.put("reorderPoint", Math.round(reorderPoint));
        result.put("leadTimeDemand", leadTimeDemand);
        result.put("safetyStock", safetyStock);

        return result;
    }

    private Map<String, Object> calculateInventoryTurnover(Map<String, Object> params) {
        double costOfGoodsSold = ((Number) params.get("costOfGoodsSold")).doubleValue();
        double averageInventory = ((Number) params.get("averageInventory")).doubleValue();

        double turnover = InventoryCalculator.calculateInventoryTurnover(
                costOfGoodsSold, averageInventory
        );

        double daysOfInventory = InventoryCalculator.calculateDaysOfInventory(turnover);

        Map<String, Object> result = new HashMap<>();
        result.put("inventoryTurnover", Math.round(turnover * 100.0) / 100.0);
        result.put("daysOfInventory", Math.round(daysOfInventory * 100.0) / 100.0);
        result.put("costOfGoodsSold", costOfGoodsSold);
        result.put("averageInventory", averageInventory);

        return result;
    }

    private Map<String, Object> calculateServiceLevel(Map<String, Object> params) {
        int availableStock = ((Number) params.get("availableStock")).intValue();
        int demand = ((Number) params.get("demand")).intValue();
        int leadTimeDemand = ((Number) params.get("leadTimeDemand")).intValue(); // FIXED: removed extra .get

        double serviceLevel = InventoryCalculator.calculateServiceLevel(
                availableStock, demand, leadTimeDemand
        );

        Map<String, Object> result = new HashMap<>();
        result.put("serviceLevel", Math.round(serviceLevel * 100.0) / 100.0);
        result.put("availableStock", availableStock);
        result.put("demand", demand);
        result.put("leadTimeDemand", leadTimeDemand);

        return result;
    }

    private Map<String, Object> calculateDateDifference(Map<String, Object> params) {
        String startDateStr = (String) params.get("startDate");
        String endDateStr = (String) params.get("endDate");

        LocalDateTime startDate = DateUtil.parseDateTime(startDateStr);
        LocalDateTime endDate = DateUtil.parseDateTime(endDateStr);

        Map<String, Object> result = new HashMap<>();
        result.put("days", DateUtil.daysBetween(startDate, endDate));
        result.put("hours", DateUtil.hoursBetween(startDate, endDate));
        result.put("minutes", DateUtil.minutesBetween(startDate, endDate));
        result.put("startDate", startDateStr);
        result.put("endDate", endDateStr);

        return result;
    }

    // Constant provider methods
    private Map<String, String> getOrderStatusConstants() {
        Map<String, String> constants = new HashMap<>();
        constants.put("PENDING", "PENDING");
        constants.put("CONFIRMED", "CONFIRMED");
        constants.put("PROCESSING", "PROCESSING");
        constants.put("SHIPPED", "SHIPPED");
        constants.put("DELIVERED", "DELIVERED");
        constants.put("CANCELLED", "CANCELLED");
        return constants;
    }

    private Map<String, String> getShipmentStatusConstants() {
        Map<String, String> constants = new HashMap<>();
        constants.put("CREATED", "CREATED");
        constants.put("IN_TRANSIT", "IN_TRANSIT");
        constants.put("DELIVERED", "DELIVERED");
        constants.put("EXCEPTION", "EXCEPTION");
        return constants;
    }

    private Map<String, String> getQualityStatusConstants() {
        Map<String, String> constants = new HashMap<>();
        constants.put("PASSED", "PASSED");
        constants.put("FAILED", "FAILED");
        constants.put("PENDING", "PENDING");
        return constants;
    }

    private Map<String, String> getReturnStatusConstants() {
        Map<String, String> constants = new HashMap<>();
        constants.put("REQUESTED", "REQUESTED");
        constants.put("APPROVED", "APPROVED");
        constants.put("COMPLETED", "COMPLETED");
        return constants;
    }

    private Map<String, String> getExceptionStatusConstants() {
        Map<String, String> constants = new HashMap<>();
        constants.put("OPEN", "OPEN");
        constants.put("RESOLVED", "RESOLVED");
        constants.put("DAMAGED", "DAMAGED");
        constants.put("LOST", "LOST");
        return constants;
    }

    private Map<String, Object> getSystemConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("DEFAULT_PAGE_SIZE", 20);
        config.put("MAX_PAGE_SIZE", 100);
        config.put("TARGET_ON_TIME_DELIVERY", 95.0);
        config.put("TARGET_ORDER_ACCURACY", 99.5);
        config.put("MAX_RETURN_PERIOD_DAYS", 30);
        return config;
    }
}