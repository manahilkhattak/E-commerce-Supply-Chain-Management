package com.ecommerce.supplychain.shipment.controller;

import com.ecommerce.supplychain.shipment.dto.*;
import com.ecommerce.supplychain.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "*")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    /**
     * API 1: Create Shipment
     * POST /api/shipments
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createShipment(@Valid @RequestBody ShipmentDTO shipmentDTO) {
        try {
            ShipmentResponseDTO response = shipmentService.createShipment(shipmentDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Shipment created successfully");
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
     * API 2: Schedule Dispatch
     * POST /api/shipments/dispatch
     */
    @PostMapping("/dispatch")
    public ResponseEntity<Map<String, Object>> scheduleDispatch(@Valid @RequestBody DispatchDTO dispatchDTO) {
        try {
            ShipmentResponseDTO response = shipmentService.scheduleDispatch(dispatchDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Dispatch scheduled successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all shipments
     * GET /api/shipments
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllShipments() {
        List<ShipmentResponseDTO> shipments = shipmentService.getAllShipments();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", shipments.size());
        response.put("data", shipments);

        return ResponseEntity.ok(response);
    }

    /**
     * Get shipment by ID
     * GET /api/shipments/{shipmentId}
     */
    @GetMapping("/{shipmentId}")
    public ResponseEntity<Map<String, Object>> getShipmentById(@PathVariable Long shipmentId) {
        try {
            ShipmentResponseDTO shipment = shipmentService.getShipmentById(shipmentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", shipment);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get shipment by tracking number
     * GET /api/shipments/tracking/{trackingNumber}
     */
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        try {
            ShipmentResponseDTO shipment = shipmentService.getShipmentByTrackingNumber(trackingNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", shipment);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get shipments by order ID
     * GET /api/shipments/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getShipmentsByOrderId(@PathVariable Long orderId) {
        List<ShipmentResponseDTO> shipments = shipmentService.getShipmentsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("count", shipments.size());
        response.put("data", shipments);

        return ResponseEntity.ok(response);
    }

    /**
     * Get shipments by status
     * GET /api/shipments/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getShipmentsByStatus(@PathVariable String status) {
        List<ShipmentResponseDTO> shipments = shipmentService.getShipmentsByStatus(status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status);
        response.put("count", shipments.size());
        response.put("data", shipments);

        return ResponseEntity.ok(response);
    }

    /**
     * Get ready for dispatch shipments
     * GET /api/shipments/ready-for-dispatch
     */
    @GetMapping("/ready-for-dispatch")
    public ResponseEntity<Map<String, Object>> getReadyForDispatch() {
        List<ShipmentResponseDTO> shipments = shipmentService.getReadyForDispatch();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Shipments ready for dispatch");
        response.put("count", shipments.size());
        response.put("data", shipments);

        return ResponseEntity.ok(response);
    }

    /**
     * Update shipment status
     * PUT /api/shipments/{shipmentId}/status
     */
    @PutMapping("/{shipmentId}/status")
    public ResponseEntity<Map<String, Object>> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @Valid @RequestBody ShipmentUpdateDTO updateDTO) {
        try {
            // Ensure the shipment ID in path matches the DTO
            updateDTO.setShipmentId(shipmentId);

            ShipmentResponseDTO response = shipmentService.updateShipmentStatus(updateDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Shipment status updated successfully");
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
     * Mark dispatch as completed
     * PUT /api/shipments/{shipmentId}/dispatch/{scheduleId}/complete
     */
    @PutMapping("/{shipmentId}/dispatch/{scheduleId}/complete")
    public ResponseEntity<Map<String, Object>> markDispatchAsCompleted(
            @PathVariable Long shipmentId,
            @PathVariable Long scheduleId) {
        try {
            ShipmentResponseDTO response = shipmentService.markDispatchAsCompleted(shipmentId, scheduleId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Dispatch marked as completed successfully");
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
     * Delete shipment
     * DELETE /api/shipments/{shipmentId}
     */
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Map<String, Object>> deleteShipment(@PathVariable Long shipmentId) {
        try {
            shipmentService.deleteShipment(shipmentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Shipment deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}