package com.ecommerce.supplychain.returns.controller;

import com.ecommerce.supplychain.returns.dto.*;
import com.ecommerce.supplychain.returns.service.ReturnsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = "*")
public class ReturnsController {

    @Autowired
    private ReturnsService returnsService;

    /**
     * API 1: Create return order
     * POST /api/returns
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReturnOrder(@Valid @RequestBody ReturnOrderDTO returnOrderDTO) {
        try {
            ReturnOrderResponseDTO response = returnsService.createReturnOrder(returnOrderDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Return order created successfully");
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
     * API 2: Process restocking
     * POST /api/returns/restock
     */
    @PostMapping("/restock")
    public ResponseEntity<Map<String, Object>> processRestocking(@Valid @RequestBody RestockDTO restockDTO) {
        try {
            RestockResponseDTO response = returnsService.processRestocking(restockDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Restocking processed successfully");
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
     * Approve return order
     * PUT /api/returns/{returnOrderId}/approve
     */
    @PutMapping("/{returnOrderId}/approve")
    public ResponseEntity<Map<String, Object>> approveReturn(
            @PathVariable Long returnOrderId,
            @RequestParam String approvedBy) {
        try {
            ReturnOrderResponseDTO response = returnsService.approveReturn(returnOrderId, approvedBy);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Return order approved successfully");
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
     * Reject return order
     * PUT /api/returns/{returnOrderId}/reject
     */
    @PutMapping("/{returnOrderId}/reject")
    public ResponseEntity<Map<String, Object>> rejectReturn(
            @PathVariable Long returnOrderId,
            @RequestParam String rejectionReason) {
        try {
            ReturnOrderResponseDTO response = returnsService.rejectReturn(returnOrderId, rejectionReason);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Return order rejected successfully");
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
     * Mark return as received
     * PUT /api/returns/{returnOrderId}/mark-received
     */
    @PutMapping("/{returnOrderId}/mark-received")
    public ResponseEntity<Map<String, Object>> markAsReceived(@PathVariable Long returnOrderId) {
        try {
            ReturnOrderResponseDTO response = returnsService.markAsReceived(returnOrderId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Return marked as received successfully");
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
     * Complete return processing
     * PUT /api/returns/{returnOrderId}/complete
     */
    @PutMapping("/{returnOrderId}/complete")
    public ResponseEntity<Map<String, Object>> completeReturn(@PathVariable Long returnOrderId) {
        try {
            ReturnOrderResponseDTO response = returnsService.completeReturn(returnOrderId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Return processing completed successfully");
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
     * Get all return orders
     * GET /api/returns
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReturnOrders() {
        List<ReturnOrderResponseDTO> returns = returnsService.getAllReturnOrders();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", returns.size());
        response.put("data", returns);

        return ResponseEntity.ok(response);
    }

    /**
     * Get return order by ID
     * GET /api/returns/{returnOrderId}
     */
    @GetMapping("/{returnOrderId}")
    public ResponseEntity<Map<String, Object>> getReturnOrderById(@PathVariable Long returnOrderId) {
        try {
            ReturnOrderResponseDTO returnOrder = returnsService.getReturnOrderById(returnOrderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", returnOrder);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get returns by order ID
     * GET /api/returns/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getReturnsByOrderId(@PathVariable Long orderId) {
        List<ReturnOrderResponseDTO> returns = returnsService.getReturnsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("count", returns.size());
        response.put("data", returns);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active returns
     * GET /api/returns/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveReturns() {
        List<ReturnOrderResponseDTO> activeReturns = returnsService.getActiveReturns();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active return orders (REQUESTED, APPROVED, RECEIVED, INSPECTING)");
        response.put("count", activeReturns.size());
        response.put("data", activeReturns);

        return ResponseEntity.ok(response);
    }

    /**
     * Get returns requiring pickup
     * GET /api/returns/requiring-pickup
     */
    @GetMapping("/requiring-pickup")
    public ResponseEntity<Map<String, Object>> getReturnsRequiringPickup() {
        List<ReturnOrderResponseDTO> pickupReturns = returnsService.getReturnsRequiringPickup();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Return orders requiring pickup");
        response.put("count", pickupReturns.size());
        response.put("data", pickupReturns);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete return order (for testing cleanup)
     * DELETE /api/returns/{returnOrderId}
     */
    @DeleteMapping("/{returnOrderId}")
    public ResponseEntity<Map<String, Object>> deleteReturnOrder(@PathVariable Long returnOrderId) {
        try {
            // In real implementation, this would soft delete or archive
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Return order deletion would be implemented here");
            response.put("deletedReturnOrderId", returnOrderId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}