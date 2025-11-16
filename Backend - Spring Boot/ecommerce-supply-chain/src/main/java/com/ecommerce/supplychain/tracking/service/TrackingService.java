package com.ecommerce.supplychain.tracking.service;

import com.ecommerce.supplychain.tracking.dto.*;
import com.ecommerce.supplychain.tracking.model.TrackingEvent;
import com.ecommerce.supplychain.tracking.model.DeliveryStatus;
import com.ecommerce.supplychain.tracking.repository.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackingService {

    @Autowired
    private TrackingEventRepository trackingEventRepository;

    /**
     * API 1: Add tracking event for a shipment
     */
    @Transactional
    public TrackingResponseDTO addTrackingEvent(TrackingDTO trackingDTO) {
        // Create tracking event
        TrackingEvent trackingEvent = new TrackingEvent();
        trackingEvent.setTrackingNumber(trackingDTO.getTrackingNumber());
        trackingEvent.setShipmentId(trackingDTO.getShipmentId());
        trackingEvent.setOrderId(trackingDTO.getOrderId());
        trackingEvent.setPackageId(trackingDTO.getPackageId());
        trackingEvent.setEventType(trackingDTO.getEventType());
        trackingEvent.setEventDescription(trackingDTO.getEventDescription());
        trackingEvent.setEventLocation(trackingDTO.getEventLocation());
        trackingEvent.setEventTimestamp(trackingDTO.getEventTimestamp() != null ? trackingDTO.getEventTimestamp() : LocalDateTime.now());
        trackingEvent.setCarrier(trackingDTO.getCarrier());
        trackingEvent.setCarrierStatusCode(trackingDTO.getCarrierStatusCode());
        trackingEvent.setCarrierStatusDescription(trackingDTO.getCarrierStatusDescription());
        trackingEvent.setLatitude(trackingDTO.getLatitude());
        trackingEvent.setLongitude(trackingDTO.getLongitude());
        trackingEvent.setEstimatedDelivery(trackingDTO.getEstimatedDelivery());
        trackingEvent.setSignedBy(trackingDTO.getSignedBy());
        trackingEvent.setDeliveryNotes(trackingDTO.getDeliveryNotes());
        trackingEvent.setIsMilestone(trackingDTO.getIsMilestone() != null ? trackingDTO.getIsMilestone() : isMilestoneEvent(trackingDTO.getEventType()));

        TrackingEvent savedEvent = trackingEventRepository.save(trackingEvent);

        // Update delivery status (this would be a separate entity in real implementation)
        updateDeliveryStatus(trackingDTO);

        return mapToTrackingResponseDTO(savedEvent);
    }

    /**
     * API 2: Get tracking history for a shipment
     */
    public List<TrackingResponseDTO> getTrackingHistory(String trackingNumber) {
        List<TrackingEvent> events = trackingEventRepository.findByTrackingNumber(trackingNumber);

        return events.stream()
                .map(this::mapToTrackingResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get delivery status by tracking number
     */
    public DeliveryStatusResponseDTO getDeliveryStatus(String trackingNumber) {
        // In a real implementation, this would query the DeliveryStatus entity
        // For now, we'll simulate it using tracking events

        List<TrackingEvent> events = trackingEventRepository.findByTrackingNumber(trackingNumber);
        if (events.isEmpty()) {
            throw new IllegalArgumentException("No tracking events found for tracking number: " + trackingNumber);
        }

        TrackingEvent latestEvent = events.stream()
                .max((e1, e2) -> e1.getEventTimestamp().compareTo(e2.getEventTimestamp()))
                .orElse(events.get(0));

        return createDeliveryStatusFromEvents(events, latestEvent);
    }

    /**
     * Get tracking events by order ID
     */
    public List<TrackingResponseDTO> getTrackingByOrderId(Long orderId) {
        List<TrackingEvent> events = trackingEventRepository.findByOrderId(orderId);

        return events.stream()
                .map(this::mapToTrackingResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get milestone events for tracking number
     */
    public List<TrackingResponseDTO> getMilestoneEvents(String trackingNumber) {
        List<TrackingEvent> milestoneEvents = trackingEventRepository.findMilestoneEventsByTrackingNumber(trackingNumber);

        return milestoneEvents.stream()
                .map(this::mapToTrackingResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update delivery status based on tracking event
     */
    private void updateDeliveryStatus(TrackingDTO trackingDTO) {
        // In a real implementation, this would update the DeliveryStatus entity
        // This ensures consistency between tracking events and current status

        System.out.println("Updating delivery status for tracking: " + trackingDTO.getTrackingNumber() +
                " to status: " + trackingDTO.getEventType());

        // Integration with Process 11 (Shipment) - Update shipment status
        updateShipmentStatus(trackingDTO.getShipmentId(), trackingDTO.getEventType());

        // Integration with Order system - Update order status if delivered
        if ("DELIVERED".equals(trackingDTO.getEventType())) {
            updateOrderStatus(trackingDTO.getOrderId(), "DELIVERED");
        }
    }

    /**
     * Integration with Process 11: Update shipment status
     */
    private void updateShipmentStatus(Long shipmentId, String status) {
        // This would call Process 11 (Shipment) service
        System.out.println("Updating shipment " + shipmentId + " to status: " + status);

        // Simulated integration
        // shipmentService.updateShipmentStatus(shipmentId, status);
    }

    /**
     * Integration with Order system: Update order status
     */
    private void updateOrderStatus(Long orderId, String status) {
        // This would call Order service
        System.out.println("Updating order " + orderId + " to status: " + status);

        // Simulated integration
        // orderService.updateOrderStatus(orderId, status);
    }

    /**
     * Check if event type is a milestone
     */
    private boolean isMilestoneEvent(String eventType) {
        return "SHIPPED".equals(eventType) ||
                "OUT_FOR_DELIVERY".equals(eventType) ||
                "DELIVERED".equals(eventType) ||
                "EXCEPTION".equals(eventType);
    }

    /**
     * Map TrackingEvent to TrackingResponseDTO
     */
    private TrackingResponseDTO mapToTrackingResponseDTO(TrackingEvent event) {
        boolean isDelivered = "DELIVERED".equals(event.getEventType());
        String statusColor = getStatusColor(event.getEventType());

        return TrackingResponseDTO.builder()
                .trackingEventId(event.getTrackingEventId())
                .trackingNumber(event.getTrackingNumber())
                .shipmentId(event.getShipmentId())
                .orderId(event.getOrderId())
                .packageId(event.getPackageId())
                .eventType(event.getEventType())
                .eventDescription(event.getEventDescription())
                .eventLocation(event.getEventLocation())
                .eventTimestamp(event.getEventTimestamp())
                .carrier(event.getCarrier())
                .carrierStatusCode(event.getCarrierStatusCode())
                .carrierStatusDescription(event.getCarrierStatusDescription())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .estimatedDelivery(event.getEstimatedDelivery())
                .signedBy(event.getSignedBy())
                .deliveryNotes(event.getDeliveryNotes())
                .isMilestone(event.getIsMilestone())
                .createdAt(event.getCreatedAt())
                .formattedEventTime(formatDateTime(event.getEventTimestamp()))
                .isDelivered(isDelivered)
                .statusColor(statusColor)
                .build();
    }

    /**
     * Create delivery status response from tracking events
     */
    private DeliveryStatusResponseDTO createDeliveryStatusFromEvents(List<TrackingEvent> events, TrackingEvent latestEvent) {
        long daysInTransit = calculateDaysInTransit(events);
        boolean isDelayed = isDeliveryDelayed(latestEvent);
        String nextAction = getNextExpectedAction(latestEvent.getEventType());

        return DeliveryStatusResponseDTO.builder()
                .deliveryStatusId(1L) // Simulated ID
                .trackingNumber(latestEvent.getTrackingNumber())
                .shipmentId(latestEvent.getShipmentId())
                .orderId(latestEvent.getOrderId())
                .packageId(latestEvent.getPackageId())
                .currentStatus(latestEvent.getEventType())
                .statusDescription(latestEvent.getEventDescription())
                .lastUpdated(latestEvent.getEventTimestamp())
                .lastLocation(latestEvent.getEventLocation())
                .carrier(latestEvent.getCarrier())
                .serviceType("STANDARD") // Default
                .estimatedDelivery(latestEvent.getEstimatedDelivery())
                .actualDelivery("DELIVERED".equals(latestEvent.getEventType()) ? latestEvent.getEventTimestamp() : null)
                .signedBy(latestEvent.getSignedBy())
                .deliveryAttempts(calculateDeliveryAttempts(events))
                .isDelivered("DELIVERED".equals(latestEvent.getEventType()))
                .isException("EXCEPTION".equals(latestEvent.getEventType()))
                .exceptionReason(latestEvent.getDeliveryNotes())
                .customerNotified(true) // Default
                .createdAt(events.get(0).getCreatedAt())
                .updatedAt(latestEvent.getEventTimestamp())
                .formattedLastUpdated(formatDateTime(latestEvent.getEventTimestamp()))
                .formattedEstimatedDelivery(formatDateTime(latestEvent.getEstimatedDelivery()))
                .daysInTransit(daysInTransit)
                .isDelayed(isDelayed)
                .nextExpectedAction(nextAction)
                .build();
    }

    /**
     * Calculate days in transit
     */
    private long calculateDaysInTransit(List<TrackingEvent> events) {
        Optional<TrackingEvent> shippedEvent = events.stream()
                .filter(e -> "SHIPPED".equals(e.getEventType()))
                .findFirst();

        if (shippedEvent.isPresent()) {
            LocalDateTime shippedDate = shippedEvent.get().getEventTimestamp();
            return ChronoUnit.DAYS.between(shippedDate, LocalDateTime.now());
        }
        return 0;
    }

    /**
     * Check if delivery is delayed
     */
    private boolean isDeliveryDelayed(TrackingEvent latestEvent) {
        if (latestEvent.getEstimatedDelivery() != null && !"DELIVERED".equals(latestEvent.getEventType())) {
            return LocalDateTime.now().isAfter(latestEvent.getEstimatedDelivery());
        }
        return false;
    }

    /**
     * Calculate delivery attempts
     */
    private Integer calculateDeliveryAttempts(List<TrackingEvent> events) {
        return (int) events.stream()
                .filter(e -> "OUT_FOR_DELIVERY".equals(e.getEventType()))
                .count();
    }

    /**
     * Get next expected action based on current status
     */
    private String getNextExpectedAction(String currentStatus) {
        switch (currentStatus) {
            case "SHIPPED":
                return "Package will arrive at distribution center";
            case "IN_TRANSIT":
                return "Package is moving through carrier network";
            case "OUT_FOR_DELIVERY":
                return "Delivery attempt today";
            case "DELIVERED":
                return "Delivery completed";
            case "EXCEPTION":
                return "Issue resolution in progress";
            default:
                return "Processing shipment";
        }
    }

    /**
     * Get status color for UI
     */
    private String getStatusColor(String eventType) {
        switch (eventType) {
            case "DELIVERED":
                return "green";
            case "EXCEPTION":
                return "red";
            case "OUT_FOR_DELIVERY":
                return "orange";
            case "IN_TRANSIT":
                return "blue";
            case "SHIPPED":
                return "purple";
            default:
                return "gray";
        }
    }

    /**
     * Format datetime for display
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.toString(); // In real app, use DateTimeFormatter
    }
}