package com.ecommerce.supplychain.inventory.controller;

import com.ecommerce.supplychain.inventory.dto.*;
import com.ecommerce.supplychain.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Inventory Monitoring and Stock Alert Management APIs.
 */
@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * API 1: Add product to inventory monitoring
     * POST /api/inventory/monitoring
     */
    @PostMapping("/monitoring")
    public ResponseEntity<Map<String, Object>> addToInventoryMonitoring(@Valid @RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryResponseDTO response = inventoryService.addToInventoryMonitoring(inventoryDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Product added to inventory monitoring successfully");
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
     * API 2: Resolve stock alert
     * PUT /api/inventory/alerts/resolve
     */
    @PutMapping("/alerts/resolve")
    public ResponseEntity<Map<String, Object>> resolveStockAlert(@Valid @RequestBody StockAlertDTO alertDTO) {
        try {
            StockAlertResponseDTO response = inventoryService.resolveStockAlert(alertDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Stock alert resolved successfully");
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
     * Update inventory stock (for sales, returns, adjustments)
     * POST /api/inventory/stock-update
     */
    @PostMapping("/stock-update")
    public ResponseEntity<Map<String, Object>> updateInventoryStock(@Valid @RequestBody StockUpdateDTO stockUpdateDTO) {
        try {
            InventoryResponseDTO response = inventoryService.updateInventoryStock(stockUpdateDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Inventory stock updated successfully");
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
     * Get all inventory items
     * GET /api/inventory/monitoring
     */
    @GetMapping("/monitoring")
    public ResponseEntity<Map<String, Object>> getAllInventory() {
        List<InventoryResponseDTO> inventory = inventoryService.getAllInventory();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", inventory.size());
        response.put("data", inventory);

        return ResponseEntity.ok(response);
    }

    /**
     * Get inventory by product ID
     * GET /api/inventory/monitoring/product/{productId}
     */
    @GetMapping("/monitoring/product/{productId}")
    public ResponseEntity<Map<String, Object>> getInventoryByProductId(@PathVariable Long productId) {
        try {
            InventoryResponseDTO inventory = inventoryService.getInventoryByProductId(productId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", inventory);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get low stock items
     * GET /api/inventory/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockItems() {
        List<InventoryResponseDTO> lowStockItems = inventoryService.getLowStockItems();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Low stock items");
        response.put("count", lowStockItems.size());
        response.put("data", lowStockItems);

        return ResponseEntity.ok(response);
    }

    /**
     * Get out of stock items
     * GET /api/inventory/out-of-stock
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<Map<String, Object>> getOutOfStockItems() {
        List<InventoryResponseDTO> outOfStockItems = inventoryService.getOutOfStockItems();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Out of stock items");
        response.put("count", outOfStockItems.size());
        response.put("data", outOfStockItems);

        return ResponseEntity.ok(response);
    }

    /**
     * Get overstock items
     * GET /api/inventory/overstock
     */
    @GetMapping("/overstock")
    public ResponseEntity<Map<String, Object>> getOverstockItems() {
        List<InventoryResponseDTO> overstockItems = inventoryService.getOverstockItems();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Overstock items");
        response.put("count", overstockItems.size());
        response.put("data", overstockItems);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all active stock alerts
     * GET /api/inventory/alerts/active
     */
    @GetMapping("/alerts/active")
    public ResponseEntity<Map<String, Object>> getActiveAlerts() {
        List<StockAlertResponseDTO> activeAlerts = inventoryService.getActiveAlerts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active stock alerts");
        response.put("count", activeAlerts.size());
        response.put("data", activeAlerts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get critical unresolved alerts
     * GET /api/inventory/alerts/critical
     */
    @GetMapping("/alerts/critical")
    public ResponseEntity<Map<String, Object>> getCriticalAlerts() {
        List<StockAlertResponseDTO> criticalAlerts = inventoryService.getCriticalAlerts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Critical unresolved alerts");
        response.put("count", criticalAlerts.size());
        response.put("data", criticalAlerts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get alerts by product
     * GET /api/inventory/alerts/product/{productId}
     */
    @GetMapping("/alerts/product/{productId}")
    public ResponseEntity<Map<String, Object>> getAlertsByProductId(@PathVariable Long productId) {
        List<StockAlertResponseDTO> alerts = inventoryService.getAlertsByProductId(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("productId", productId);
        response.put("count", alerts.size());
        response.put("data", alerts);

        return ResponseEntity.ok(response);
    }

    /**
     * Run inventory health check
     * POST /api/inventory/health-check
     */
    @PostMapping("/health-check")
    public ResponseEntity<Map<String, Object>> runInventoryHealthCheck() {
        List<StockAlertResponseDTO> newAlerts = inventoryService.runInventoryHealthCheck();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Inventory health check completed");
        response.put("newAlertsGenerated", newAlerts.size());
        response.put("data", newAlerts);

        return ResponseEntity.ok(response);
    }
}