package com.ecommerce.supplychain.shipment.service;

import com.ecommerce.supplychain.shipment.dto.*;
import com.ecommerce.supplychain.shipment.model.Shipment;
import com.ecommerce.supplychain.shipment.model.DispatchSchedule;
import com.ecommerce.supplychain.shipment.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    /**
     * API 1: Create Shipment
     */
    @Transactional
    public ShipmentResponseDTO createShipment(ShipmentDTO shipmentDTO) {
        // Validate tracking number uniqueness
        if (shipmentRepository.existsByTrackingNumber(shipmentDTO.getTrackingNumber())) {
            throw new IllegalArgumentException("Shipment with tracking number " + shipmentDTO.getTrackingNumber() + " already exists");
        }

        // Create shipment entity
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber(shipmentDTO.getTrackingNumber());
        shipment.setOrderId(shipmentDTO.getOrderId());
        shipment.setOrderNumber(shipmentDTO.getOrderNumber());
        shipment.setPackageId(shipmentDTO.getPackageId());
        shipment.setCarrier(shipmentDTO.getCarrier());
        shipment.setServiceType(shipmentDTO.getServiceType());
        shipment.setShipmentDate(shipmentDTO.getShipmentDate());
        shipment.setEstimatedDeliveryDate(shipmentDTO.getEstimatedDeliveryDate());
        shipment.setShippingCost(shipmentDTO.getShippingCost());
        shipment.setInsuranceAmount(shipmentDTO.getInsuranceAmount());
        shipment.setPackageWeightKg(shipmentDTO.getPackageWeightKg());
        shipment.setPackageDimensions(shipmentDTO.getPackageDimensions());
        shipment.setDestinationAddress(shipmentDTO.getDestinationAddress());
        shipment.setRecipientName(shipmentDTO.getRecipientName());
        shipment.setRecipientPhone(shipmentDTO.getRecipientPhone());
        shipment.setRecipientEmail(shipmentDTO.getRecipientEmail());
        shipment.setRequiresSignature(shipmentDTO.getRequiresSignature());
        shipment.setIsInsured(shipmentDTO.getIsInsured());
        shipment.setIsFragile(shipmentDTO.getIsFragile());
        shipment.setSpecialInstructions(shipmentDTO.getSpecialInstructions());
        shipment.setNotes(shipmentDTO.getNotes());
        shipment.setShipmentStatus("SCHEDULED");
        shipment.setCreatedAt(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());

        // Set default warehouse address as origin
        shipment.setOriginAddress("Warehouse Address - 123 Logistics Drive, Industrial Park, New York, NY 10001");

        Shipment savedShipment = shipmentRepository.save(shipment);

        return mapToShipmentResponseDTO(savedShipment);
    }

    /**
     * API 2: Schedule Dispatch
     */
    @Transactional
    public ShipmentResponseDTO scheduleDispatch(DispatchDTO dispatchDTO) {
        Shipment shipment = shipmentRepository.findById(dispatchDTO.getShipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with ID: " + dispatchDTO.getShipmentId()));

        if (!"SCHEDULED".equals(shipment.getShipmentStatus())) {
            throw new IllegalStateException("Only SCHEDULED shipments can be dispatched. Current status: " + shipment.getShipmentStatus());
        }

        // Create dispatch schedule
        DispatchSchedule dispatchSchedule = new DispatchSchedule();
        dispatchSchedule.setShipment(shipment);
        dispatchSchedule.setScheduleType(dispatchDTO.getScheduleType());
        dispatchSchedule.setScheduledDateTime(dispatchDTO.getScheduledDateTime());
        dispatchSchedule.setDriverName(dispatchDTO.getDriverName());
        dispatchSchedule.setVehicleNumber(dispatchDTO.getVehicleNumber());
        dispatchSchedule.setDispatchStatus("SCHEDULED");
        dispatchSchedule.setNotes(dispatchDTO.getNotes());
        dispatchSchedule.setCreatedAt(LocalDateTime.now());
        dispatchSchedule.setUpdatedAt(LocalDateTime.now());

        shipment.addDispatchSchedule(dispatchSchedule);
        shipment.setShipmentStatus("DISPATCH_SCHEDULED");
        shipment.setUpdatedAt(LocalDateTime.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        return mapToShipmentResponseDTO(updatedShipment);
    }

    /**
     * Get all shipments
     */
    public List<ShipmentResponseDTO> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(this::mapToShipmentResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get shipment by ID
     */
    public ShipmentResponseDTO getShipmentById(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with ID: " + shipmentId));
        return mapToShipmentResponseDTO(shipment);
    }

    /**
     * Get shipment by tracking number
     */
    public ShipmentResponseDTO getShipmentByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with tracking number: " + trackingNumber));
        return mapToShipmentResponseDTO(shipment);
    }

    /**
     * Get shipments by order ID
     */
    public List<ShipmentResponseDTO> getShipmentsByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId).stream()
                .map(this::mapToShipmentResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get shipments by status
     */
    public List<ShipmentResponseDTO> getShipmentsByStatus(String status) {
        return shipmentRepository.findByShipmentStatus(status).stream()
                .map(this::mapToShipmentResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get ready for dispatch shipments
     */
    public List<ShipmentResponseDTO> getReadyForDispatch() {
        return shipmentRepository.findReadyForDispatch(LocalDate.now()).stream()
                .map(this::mapToShipmentResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update shipment status
     */
    @Transactional
    public ShipmentResponseDTO updateShipmentStatus(ShipmentUpdateDTO updateDTO) {
        Shipment shipment = shipmentRepository.findById(updateDTO.getShipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with ID: " + updateDTO.getShipmentId()));

        shipment.setShipmentStatus(updateDTO.getStatus());

        if ("SHIPPED".equals(updateDTO.getStatus())) {
            shipment.markAsShipped();
        } else if ("DELIVERED".equals(updateDTO.getStatus())) {
            shipment.markAsDelivered();
        } else if ("IN_TRANSIT".equals(updateDTO.getStatus())) {
            shipment.markAsInTransit();
        }

        if (updateDTO.getNotes() != null && !updateDTO.getNotes().isEmpty()) {
            String existingNotes = shipment.getNotes() != null ? shipment.getNotes() + "\n\n" : "";
            shipment.setNotes(existingNotes + "Status Update: " + updateDTO.getNotes());
        }

        shipment.setUpdatedAt(LocalDateTime.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        return mapToShipmentResponseDTO(updatedShipment);
    }

    /**
     * Mark dispatch as completed
     */
    @Transactional
    public ShipmentResponseDTO markDispatchAsCompleted(Long shipmentId, Long scheduleId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with ID: " + shipmentId));

        DispatchSchedule schedule = shipment.getDispatchSchedules().stream()
                .filter(s -> s.getScheduleId().equals(scheduleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dispatch schedule not found with ID: " + scheduleId));

        schedule.markAsCompleted();
        schedule.setUpdatedAt(LocalDateTime.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        return mapToShipmentResponseDTO(updatedShipment);
    }

    /**
     * Delete shipment
     */
    @Transactional
    public void deleteShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found with ID: " + shipmentId));

        if (!"SCHEDULED".equals(shipment.getShipmentStatus()) && !"CANCELLED".equals(shipment.getShipmentStatus())) {
            throw new IllegalStateException("Only SCHEDULED or CANCELLED shipments can be deleted");
        }

        shipmentRepository.delete(shipment);
    }

    /**
     * Helper method to map Shipment to ResponseDTO
     */
    private ShipmentResponseDTO mapToShipmentResponseDTO(Shipment shipment) {
        boolean canBeDispatched = "SCHEDULED".equals(shipment.getShipmentStatus()) &&
                shipment.getShipmentDate() != null &&
                !shipment.getShipmentDate().isAfter(LocalDate.now());

        String nextAction = determineNextAction(shipment);

        List<DispatchScheduleResponseDTO> scheduleDTOs = shipment.getDispatchSchedules().stream()
                .map(schedule -> {
                    boolean isOverdue = schedule.getScheduledDateTime().isBefore(LocalDateTime.now()) &&
                            !"COMPLETED".equals(schedule.getDispatchStatus());

                    return DispatchScheduleResponseDTO.builder()
                            .scheduleId(schedule.getScheduleId())
                            .shipmentId(schedule.getShipment().getShipmentId())
                            .scheduleType(schedule.getScheduleType())
                            .scheduledDateTime(schedule.getScheduledDateTime())
                            .actualDateTime(schedule.getActualDateTime())
                            .driverName(schedule.getDriverName())
                            .vehicleNumber(schedule.getVehicleNumber())
                            .dispatchStatus(schedule.getDispatchStatus())
                            .notes(schedule.getNotes())
                            .createdAt(schedule.getCreatedAt())
                            .updatedAt(schedule.getUpdatedAt())
                            .isOverdue(isOverdue)
                            .build();
                })
                .collect(Collectors.toList());

        return ShipmentResponseDTO.builder()
                .shipmentId(shipment.getShipmentId())
                .trackingNumber(shipment.getTrackingNumber())
                .orderId(shipment.getOrderId())
                .orderNumber(shipment.getOrderNumber())
                .packageId(shipment.getPackageId())
                .carrier(shipment.getCarrier())
                .serviceType(shipment.getServiceType())
                .shipmentStatus(shipment.getShipmentStatus())
                .shipmentDate(shipment.getShipmentDate())
                .estimatedDeliveryDate(shipment.getEstimatedDeliveryDate())
                .actualDeliveryDate(shipment.getActualDeliveryDate())
                .pickupDate(shipment.getPickupDate())
                .shippingCost(shipment.getShippingCost())
                .insuranceAmount(shipment.getInsuranceAmount())
                .packageWeightKg(shipment.getPackageWeightKg())
                .packageDimensions(shipment.getPackageDimensions())
                .originAddress(shipment.getOriginAddress())
                .destinationAddress(shipment.getDestinationAddress())
                .recipientName(shipment.getRecipientName())
                .recipientPhone(shipment.getRecipientPhone())
                .recipientEmail(shipment.getRecipientEmail())
                .requiresSignature(shipment.getRequiresSignature())
                .isInsured(shipment.getIsInsured())
                .isFragile(shipment.getIsFragile())
                .specialInstructions(shipment.getSpecialInstructions())
                .carrierTrackingUrl(shipment.getCarrierTrackingUrl())
                .notes(shipment.getNotes())
                .createdAt(shipment.getCreatedAt())
                .updatedAt(shipment.getUpdatedAt())
                .dispatchSchedules(scheduleDTOs)
                .canBeDispatched(canBeDispatched)
                .nextAction(nextAction)
                .build();
    }

    /**
     * Determine next action for shipment
     */
    private String determineNextAction(Shipment shipment) {
        switch (shipment.getShipmentStatus()) {
            case "SCHEDULED":
                return "SCHEDULE_DISPATCH";
            case "DISPATCH_SCHEDULED":
                return "DISPATCH_NOW";
            case "SHIPPED":
                return "TRACK_SHIPMENT";
            case "IN_TRANSIT":
                return "UPDATE_DELIVERY";
            case "DELIVERED":
                return "COMPLETE_ORDER";
            default:
                return "REVIEW";
        }
    }
}