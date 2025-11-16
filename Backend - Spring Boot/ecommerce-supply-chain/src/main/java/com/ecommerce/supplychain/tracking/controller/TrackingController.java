package com.ecommerce.supplychain.tracking.controller;

import com.ecommerce.supplychain.tracking.dto.*;
import com.ecommerce.supplychain.tracking.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    /**
     * API 1: Add tracking event
     * POST /api/tracking/events
     */
    @PostMapping("/events")
    public ResponseEntity<Map<String, Object>> addTrackingEvent(@Valid @RequestBody TrackingDTO trackingDTO) {
        try {
            TrackingResponseDTO response = trackingService.addTrackingEvent(trackingDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Tracking event added successfully");
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
     * API 2: Get tracking history
     * GET /api/tracking/history/{trackingNumber}
     */
    @GetMapping("/history/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> getTrackingHistory(@PathVariable String trackingNumber) {
        try {
            List<TrackingResponseDTO> trackingHistory = trackingService.getTrackingHistory(trackingNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("trackingNumber", trackingNumber);
            response.put("count", trackingHistory.size());
            response.put("data", trackingHistory);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get delivery status by tracking number
     * GET /api/tracking/status/{trackingNumber}
     */
    @GetMapping("/status/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> getDeliveryStatus(@PathVariable String trackingNumber) {
        try {
            DeliveryStatusResponseDTO status = trackingService.getDeliveryStatus(trackingNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("trackingNumber", trackingNumber);
            response.put("data", status);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get tracking events by order ID
     * GET /api/tracking/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getTrackingByOrderId(@PathVariable Long orderId) {
        try {
            List<TrackingResponseDTO> trackingEvents = trackingService.getTrackingByOrderId(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("count", trackingEvents.size());
            response.put("data", trackingEvents);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get milestone events for tracking
     * GET /api/tracking/milestones/{trackingNumber}
     */
    @GetMapping("/milestones/{trackingNumber}")
    public ResponseEntity<Map<String, Object>> getMilestoneEvents(@PathVariable String trackingNumber) {
        try {
            List<TrackingResponseDTO> milestoneEvents = trackingService.getMilestoneEvents(trackingNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("trackingNumber", trackingNumber);
            response.put("count", milestoneEvents.size());
            response.put("data", milestoneEvents);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get all tracking events (for testing)
     * GET /api/tracking/events
     */
    @GetMapping("/events")
    public ResponseEntity<Map<String, Object>> getAllTrackingEvents() {
        // This would normally require pagination, but for testing:
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Use specific endpoints for tracking data");
        response.put("availableEndpoints", List.of(
                "POST /api/tracking/events - Add tracking event",
                "GET /api/tracking/history/{trackingNumber} - Get tracking history",
                "GET /api/tracking/status/{trackingNumber} - Get delivery status",
                "GET /api/tracking/order/{orderId} - Get tracking by order",
                "GET /api/tracking/milestones/{trackingNumber} - Get milestone events"
        ));

        return ResponseEntity.ok(response);
    }
}