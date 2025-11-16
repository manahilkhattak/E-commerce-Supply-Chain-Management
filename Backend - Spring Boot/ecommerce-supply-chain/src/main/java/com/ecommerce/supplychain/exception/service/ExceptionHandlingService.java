package com.ecommerce.supplychain.exception.service;

import com.ecommerce.supplychain.exception.dto.*;
import com.ecommerce.supplychain.exception.model.DeliveryException;
import com.ecommerce.supplychain.exception.model.ExceptionResolution;
import com.ecommerce.supplychain.exception.repository.DeliveryExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExceptionHandlingService {

    @Autowired
    private DeliveryExceptionRepository deliveryExceptionRepository;

    /**
     * API 1: Create delivery exception
     */
    @Transactional
    public DeliveryExceptionResponseDTO createDeliveryException(DeliveryExceptionDTO exceptionDTO) {
        // Generate unique exception number
        String exceptionNumber = generateExceptionNumber();

        // Validate no open exception exists for this tracking number
        if (deliveryExceptionRepository.existsByTrackingNumberAndExceptionStatus(exceptionDTO.getTrackingNumber(), "OPEN")) {
            throw new IllegalArgumentException("An open exception already exists for tracking number: " + exceptionDTO.getTrackingNumber());
        }

        // Create delivery exception entity
        DeliveryException exception = new DeliveryException();
        exception.setExceptionNumber(exceptionNumber);
        exception.setTrackingNumber(exceptionDTO.getTrackingNumber());
        exception.setShipmentId(exceptionDTO.getShipmentId());
        exception.setOrderId(exceptionDTO.getOrderId());
        exception.setPackageId(exceptionDTO.getPackageId());
        exception.setExceptionType(exceptionDTO.getExceptionType());
        exception.setExceptionSeverity(exceptionDTO.getExceptionSeverity());
        exception.setExceptionDescription(exceptionDTO.getExceptionDescription());
        exception.setExceptionLocation(exceptionDTO.getExceptionLocation());
        exception.setExceptionDate(exceptionDTO.getExceptionDate() != null ? exceptionDTO.getExceptionDate() : LocalDateTime.now());
        exception.setReportedBy(exceptionDTO.getReportedBy() != null ? exceptionDTO.getReportedBy() : "SYSTEM");
        exception.setCarrier(exceptionDTO.getCarrier());
        exception.setCarrierContact(exceptionDTO.getCarrierContact());
        exception.setAssignedTo(exceptionDTO.getAssignedTo());
        exception.setPriorityLevel(exceptionDTO.getPriorityLevel());
        exception.setRequiresInsuranceClaim(exceptionDTO.getRequiresInsuranceClaim());
        exception.setEstimatedCompensationAmount(exceptionDTO.getEstimatedCompensationAmount());
        exception.setCustomerContacted(exceptionDTO.getCustomerContacted());
        exception.setEstimatedResolutionDate(exceptionDTO.getEstimatedResolutionDate());

        // Auto-assign based on severity
        if (exceptionDTO.getAssignedTo() == null && exception.requiresImmediateAttention()) {
            exception.setAssignedTo("support_urgent");
            exception.setExceptionStatus("IN_PROGRESS");
        }

        DeliveryException savedException = deliveryExceptionRepository.save(exception);

        // Integrate with other processes
        handleExceptionIntegration(savedException);

        return mapToDeliveryExceptionResponseDTO(savedException);
    }

    /**
     * API 2: Resolve delivery exception
     */
    @Transactional
    public ResolutionResponseDTO resolveException(ResolutionDTO resolutionDTO) {
        // Find the exception
        DeliveryException exception = deliveryExceptionRepository.findById(resolutionDTO.getExceptionId())
                .orElseThrow(() -> new IllegalArgumentException("Delivery exception not found with ID: " + resolutionDTO.getExceptionId()));

        // Create resolution
        ExceptionResolution resolution = new ExceptionResolution();
        resolution.setDeliveryException(exception);
        resolution.setResolutionType(resolutionDTO.getResolutionType());
        resolution.setResolutionDescription(resolutionDTO.getResolutionDescription());
        resolution.setActionTaken(resolutionDTO.getActionTaken());
        resolution.setResolutionDate(resolutionDTO.getResolutionDate() != null ? resolutionDTO.getResolutionDate() : LocalDateTime.now());
        resolution.setResolvedBy(resolutionDTO.getResolvedBy() != null ? resolutionDTO.getResolvedBy() : "system_auto");
        resolution.setCustomerSatisfactionRating(resolutionDTO.getCustomerSatisfactionRating());
        resolution.setCompensationAmount(resolutionDTO.getCompensationAmount());
        resolution.setCompensationApprovedBy(resolutionDTO.getCompensationApprovedBy());
        resolution.setReshipmentTrackingNumber(resolutionDTO.getReshipmentTrackingNumber());
        resolution.setReshipmentDate(resolutionDTO.getReshipmentDate());
        resolution.setAdditionalNotes(resolutionDTO.getAdditionalNotes());
        resolution.setCostIncurred(resolutionDTO.getCostIncurred());
        resolution.setRootCauseAnalysis(resolutionDTO.getRootCauseAnalysis());
        resolution.setPreventiveMeasures(resolutionDTO.getPreventiveMeasures());

        // Calculate resolution duration
        resolution.calculateResolutionDuration();

        // Set resolution to exception
        exception.setResolution(resolution);
        exception.markAsResolved();

        DeliveryException updatedException = deliveryExceptionRepository.save(exception);

        // Handle resolution integration
        handleResolutionIntegration(updatedException, resolution);

        return mapToResolutionResponseDTO(resolution);
    }

    /**
     * Get all delivery exceptions
     */
    public List<DeliveryExceptionResponseDTO> getAllDeliveryExceptions() {
        return deliveryExceptionRepository.findAll().stream()
                .map(this::mapToDeliveryExceptionResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get delivery exception by ID
     */
    public DeliveryExceptionResponseDTO getDeliveryExceptionById(Long exceptionId) {
        DeliveryException exception = deliveryExceptionRepository.findById(exceptionId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery exception not found with ID: " + exceptionId));
        return mapToDeliveryExceptionResponseDTO(exception);
    }

    /**
     * Get exceptions by tracking number
     */
    public List<DeliveryExceptionResponseDTO> getExceptionsByTrackingNumber(String trackingNumber) {
        return deliveryExceptionRepository.findByTrackingNumber(trackingNumber).stream()
                .map(this::mapToDeliveryExceptionResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get exceptions by order ID
     */
    public List<DeliveryExceptionResponseDTO> getExceptionsByOrderId(Long orderId) {
        return deliveryExceptionRepository.findByOrderId(orderId).stream()
                .map(this::mapToDeliveryExceptionResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active exceptions
     */
    public List<DeliveryExceptionResponseDTO> getActiveExceptions() {
        return deliveryExceptionRepository.findActiveExceptions().stream()
                .map(this::mapToDeliveryExceptionResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Assign exception to agent
     */
    @Transactional
    public DeliveryExceptionResponseDTO assignException(Long exceptionId, String assignedTo) {
        DeliveryException exception = deliveryExceptionRepository.findById(exceptionId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery exception not found with ID: " + exceptionId));

        exception.assignToAgent(assignedTo);

        DeliveryException updatedException = deliveryExceptionRepository.save(exception);

        return mapToDeliveryExceptionResponseDTO(updatedException);
    }

    /**
     * Escalate exception
     */
    @Transactional
    public DeliveryExceptionResponseDTO escalateException(Long exceptionId) {
        DeliveryException exception = deliveryExceptionRepository.findById(exceptionId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery exception not found with ID: " + exceptionId));

        exception.escalateException();

        DeliveryException updatedException = deliveryExceptionRepository.save(exception);

        return mapToDeliveryExceptionResponseDTO(updatedException);
    }

    /**
     * Handle integration with other processes when exception is created
     */
    private void handleExceptionIntegration(DeliveryException exception) {
        // Integration with Process 12 (Tracking) - Update tracking status
        updateTrackingStatus(exception.getTrackingNumber(), "EXCEPTION", exception.getExceptionDescription());

        // Integration with Process 11 (Shipment) - Update shipment status
        updateShipmentStatus(exception.getShipmentId(), "EXCEPTION", exception.getExceptionDescription());

        // Integration with Order system - Update order status
        updateOrderStatus(exception.getOrderId(), "DELIVERY_EXCEPTION", exception.getExceptionDescription());

        // Auto-notify customer for critical exceptions
        if (exception.requiresImmediateAttention()) {
            notifyCustomer(exception.getOrderId(), exception.getExceptionType(), exception.getExceptionDescription());
        }

        System.out.println("Integration: Exception " + exception.getExceptionNumber() + " integrated with all processes");
    }

    /**
     * Handle integration with other processes when exception is resolved
     */
    private void handleResolutionIntegration(DeliveryException exception, ExceptionResolution resolution) {
        // Integration with Process 9 (Picking) - Create reshipment if needed
        if ("RESHIP".equals(resolution.getResolutionType()) && resolution.getReshipmentTrackingNumber() != null) {
            createReshipment(exception.getOrderId(), resolution.getReshipmentTrackingNumber());
        }

        // Integration with Finance system - Process compensation
        if (resolution.isCompensationRequired()) {
            processCompensation(exception.getOrderId(), resolution.getCompensationAmount(), resolution.getCompensationApprovedBy());
        }

        // Integration with Process 12 (Tracking) - Update tracking for reshipment
        if (resolution.getReshipmentTrackingNumber() != null) {
            updateTrackingStatus(resolution.getReshipmentTrackingNumber(), "SHIPPED", "Reshipment for exception: " + exception.getExceptionNumber());
        }

        System.out.println("Integration: Resolution for exception " + exception.getExceptionNumber() + " integrated with all processes");
    }

    /**
     * Integration with Process 12: Update tracking status
     */
    private void updateTrackingStatus(String trackingNumber, String status, String description) {
        System.out.println("Integration: Updating Process 12 Tracking " + trackingNumber + " to: " + status);
        // Real implementation would call TrackingService
    }

    /**
     * Integration with Process 11: Update shipment status
     */
    private void updateShipmentStatus(Long shipmentId, String status, String description) {
        System.out.println("Integration: Updating Process 11 Shipment " + shipmentId + " to: " + status);
        // Real implementation would call ShipmentService
    }

    /**
     * Integration with Order system: Update order status
     */
    private void updateOrderStatus(Long orderId, String status, String description) {
        System.out.println("Integration: Updating Order " + orderId + " to: " + status);
        // Real implementation would call OrderService
    }

    /**
     * Integration with Customer system: Notify customer
     */
    private void notifyCustomer(Long orderId, String exceptionType, String description) {
        System.out.println("Integration: Notifying customer for Order " + orderId + " about: " + exceptionType);
        // Real implementation would call NotificationService
    }

    /**
     * Integration with Process 9: Create reshipment
     */
    private void createReshipment(Long orderId, String newTrackingNumber) {
        System.out.println("Integration: Creating reshipment for Order " + orderId + " with tracking: " + newTrackingNumber);
        // Real implementation would call PickingService
    }

    /**
     * Integration with Finance: Process compensation
     */
    private void processCompensation(Long orderId, Double amount, String approvedBy) {
        System.out.println("Integration: Processing compensation for Order " + orderId + " amount: " + amount + " approved by: " + approvedBy);
        // Real implementation would call FinanceService
    }

    /**
     * Generate unique exception number
     */
    private String generateExceptionNumber() {
        return "EXC-" + System.currentTimeMillis();
    }

    /**
     * Map DeliveryException to ResponseDTO
     */
    private DeliveryExceptionResponseDTO mapToDeliveryExceptionResponseDTO(DeliveryException exception) {
        boolean requiresImmediateAttention = exception.requiresImmediateAttention();
        long daysOpen = calculateDaysOpen(exception);
        String statusColor = getStatusColor(exception.getExceptionStatus());
        boolean hasResolution = exception.getResolution() != null;

        ResolutionResponseDTO resolutionDTO = null;
        if (hasResolution) {
            resolutionDTO = mapToResolutionResponseDTO(exception.getResolution());
        }

        return DeliveryExceptionResponseDTO.builder()
                .exceptionId(exception.getExceptionId())
                .exceptionNumber(exception.getExceptionNumber())
                .trackingNumber(exception.getTrackingNumber())
                .shipmentId(exception.getShipmentId())
                .orderId(exception.getOrderId())
                .packageId(exception.getPackageId())
                .exceptionType(exception.getExceptionType())
                .exceptionSeverity(exception.getExceptionSeverity())
                .exceptionDescription(exception.getExceptionDescription())
                .exceptionLocation(exception.getExceptionLocation())
                .exceptionDate(exception.getExceptionDate())
                .reportedBy(exception.getReportedBy())
                .carrier(exception.getCarrier())
                .carrierContact(exception.getCarrierContact())
                .customerContacted(exception.getCustomerContacted())
                .customerContactDate(exception.getCustomerContactDate())
                .customerResponse(exception.getCustomerResponse())
                .estimatedResolutionDate(exception.getEstimatedResolutionDate())
                .assignedTo(exception.getAssignedTo())
                .exceptionStatus(exception.getExceptionStatus())
                .priorityLevel(exception.getPriorityLevel())
                .requiresInsuranceClaim(exception.getRequiresInsuranceClaim())
                .insuranceClaimFiled(exception.getInsuranceClaimFiled())
                .claimReference(exception.getClaimReference())
                .estimatedCompensationAmount(exception.getEstimatedCompensationAmount())
                .createdAt(exception.getCreatedAt())
                .updatedAt(exception.getUpdatedAt())
                .requiresImmediateAttention(requiresImmediateAttention)
                .daysOpen(daysOpen)
                .statusColor(statusColor)
                .hasResolution(hasResolution)
                .resolution(resolutionDTO)
                .build();
    }

    /**
     * Map ExceptionResolution to ResponseDTO
     */
    private ResolutionResponseDTO mapToResolutionResponseDTO(ExceptionResolution resolution) {
        boolean isCompensationRequired = resolution.isCompensationRequired();
        String resolutionEfficiency = calculateResolutionEfficiency(resolution);

        return ResolutionResponseDTO.builder()
                .resolutionId(resolution.getResolutionId())
                .exceptionId(resolution.getDeliveryException().getExceptionId())
                .resolutionType(resolution.getResolutionType())
                .resolutionDescription(resolution.getResolutionDescription())
                .actionTaken(resolution.getActionTaken())
                .resolutionDate(resolution.getResolutionDate())
                .resolvedBy(resolution.getResolvedBy())
                .customerSatisfactionRating(resolution.getCustomerSatisfactionRating())
                .compensationAmount(resolution.getCompensationAmount())
                .compensationApprovedBy(resolution.getCompensationApprovedBy())
                .reshipmentTrackingNumber(resolution.getReshipmentTrackingNumber())
                .reshipmentDate(resolution.getReshipmentDate())
                .additionalNotes(resolution.getAdditionalNotes())
                .resolutionDurationHours(resolution.getResolutionDurationHours())
                .costIncurred(resolution.getCostIncurred())
                .rootCauseAnalysis(resolution.getRootCauseAnalysis())
                .preventiveMeasures(resolution.getPreventiveMeasures())
                .createdAt(resolution.getCreatedAt())
                .updatedAt(resolution.getUpdatedAt())
                .isCompensationRequired(isCompensationRequired)
                .resolutionEfficiency(resolutionEfficiency)
                .build();
    }

    /**
     * Calculate days exception has been open
     */
    private long calculateDaysOpen(DeliveryException exception) {
        if ("RESOLVED".equals(exception.getExceptionStatus()) && exception.getResolution() != null) {
            return ChronoUnit.DAYS.between(exception.getExceptionDate(), exception.getResolution().getResolutionDate());
        }
        return ChronoUnit.DAYS.between(exception.getExceptionDate(), LocalDateTime.now());
    }

    /**
     * Calculate resolution efficiency
     */
    private String calculateResolutionEfficiency(ExceptionResolution resolution) {
        if (resolution.getResolutionDurationHours() == null) return "N/A";

        if (resolution.getResolutionDurationHours() <= 24) return "EXCELLENT";
        if (resolution.getResolutionDurationHours() <= 72) return "GOOD";
        if (resolution.getResolutionDurationHours() <= 168) return "AVERAGE";
        return "POOR";
    }

    /**
     * Get status color for UI
     */
    private String getStatusColor(String status) {
        switch (status) {
            case "OPEN": return "red";
            case "IN_PROGRESS": return "orange";
            case "RESOLVED": return "green";
            case "ESCALATED": return "purple";
            case "CLOSED": return "gray";
            default: return "blue";
        }
    }
}