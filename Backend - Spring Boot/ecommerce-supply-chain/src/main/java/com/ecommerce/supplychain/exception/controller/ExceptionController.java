package com.ecommerce.supplychain.exception.controller;

import com.ecommerce.supplychain.exception.dto.*;
import com.ecommerce.supplychain.exception.service.ExceptionHandlingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exceptions")
@CrossOrigin(origins = "*")
public class ExceptionController {

    @Autowired
    private ExceptionHandlingService exceptionHandlingService;

    /**
     * API 1: Create delivery exception
     * POST /api/exceptions
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDeliveryException(@Valid @RequestBody DeliveryExceptionDTO exceptionDTO) {
        try {
            DeliveryExceptionResponseDTO response = exceptionHandlingService.createDeliveryException(exceptionDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Delivery exception created successfully");
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
     * API 2: Resolve delivery exception
     * POST /api/exceptions/resolve
     */
    @PostMapping("/resolve")
    public ResponseEntity<Map<String, Object>> resolveException(@Valid @RequestBody ResolutionDTO resolutionDTO) {
        try {
            ResolutionResponseDTO response = exceptionHandlingService.resolveException(resolutionDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Delivery exception resolved successfully");
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
     * Get all delivery exceptions
     * GET /api/exceptions
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDeliveryExceptions() {
        List<DeliveryExceptionResponseDTO> exceptions = exceptionHandlingService.getAllDeliveryExceptions();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", exceptions.size());
        response.put("data", exceptions);

        return ResponseEntity.ok(response);
    }

    /**
     * Get delivery exception by ID
     * GET /api/exceptions/{exceptionId}
     */
    @GetMapping("/{exceptionId}")
    public ResponseEntity<Map<String, Object>> getDeliveryExceptionById(@PathVariable Long exceptionId) {
        try {
            DeliveryExceptionResponseDTO exception = exceptionHandlingService.getDeliveryExceptionById(exceptionId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", exception);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get exceptions by tracking number
     * GET /api/exceptions/tracking/{trackingNumber}
     */
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> getExceptionsByTrackingNumber(@PathVariable String trackingNumber) {
        List<DeliveryExceptionResponseDTO> exceptions = exceptionHandlingService.getExceptionsByTrackingNumber(trackingNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("trackingNumber", trackingNumber);
        response.put("count", exceptions.size());
        response.put("data", exceptions);

        return ResponseEntity.ok(response);
    }

    /**
     * Get exceptions by order ID
     * GET /api/exceptions/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getExceptionsByOrderId(@PathVariable Long orderId) {
        List<DeliveryExceptionResponseDTO> exceptions = exceptionHandlingService.getExceptionsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("count", exceptions.size());
        response.put("data", exceptions);

        return ResponseEntity.ok(response);
    }

    /**
     * Get active exceptions
     * GET /api/exceptions/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveExceptions() {
        List<DeliveryExceptionResponseDTO> activeExceptions = exceptionHandlingService.getActiveExceptions();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Active delivery exceptions (OPEN, IN_PROGRESS, ESCALATED)");
        response.put("count", activeExceptions.size());
        response.put("data", activeExceptions);

        return ResponseEntity.ok(response);
    }

    /**
     * Assign exception to agent
     * PUT /api/exceptions/{exceptionId}/assign
     */
    @PutMapping("/{exceptionId}/assign")
    public ResponseEntity<Map<String, Object>> assignException(
            @PathVariable Long exceptionId,
            @RequestParam String assignedTo) {
        try {
            DeliveryExceptionResponseDTO response = exceptionHandlingService.assignException(exceptionId, assignedTo);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Exception assigned successfully");
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
     * Escalate exception
     * PUT /api/exceptions/{exceptionId}/escalate
     */
    @PutMapping("/{exceptionId}/escalate")
    public ResponseEntity<Map<String, Object>> escalateException(@PathVariable Long exceptionId) {
        try {
            DeliveryExceptionResponseDTO response = exceptionHandlingService.escalateException(exceptionId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Exception escalated successfully");
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
     * Delete delivery exception (for testing cleanup)
     * DELETE /api/exceptions/{exceptionId}
     */
    @DeleteMapping("/{exceptionId}")
    public ResponseEntity<Map<String, Object>> deleteDeliveryException(@PathVariable Long exceptionId) {
        try {
            // In real implementation, this would soft delete or archive
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Exception deletion would be implemented here");
            response.put("deletedExceptionId", exceptionId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}