package com.ecommerce.supplychain.returns.service;

import com.ecommerce.supplychain.returns.dto.*;
import com.ecommerce.supplychain.returns.model.ReturnOrder;
import com.ecommerce.supplychain.returns.model.ReturnItem;
import com.ecommerce.supplychain.returns.model.RestockRecord;
import com.ecommerce.supplychain.returns.repository.ReturnOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReturnsService {

    @Autowired
    private ReturnOrderRepository returnOrderRepository;

    /**
     * API 1: Create return order
     */
    @Transactional
    public ReturnOrderResponseDTO createReturnOrder(ReturnOrderDTO returnOrderDTO) {
        // Generate unique return number
        String returnNumber = generateReturnNumber();

        // Validate no active return exists for this order
        if (returnOrderRepository.existsByOrderIdAndReturnStatusIn(returnOrderDTO.getOrderId(),
                List.of("REQUESTED", "APPROVED", "RECEIVED", "INSPECTING"))) {
            throw new IllegalArgumentException("An active return already exists for order: " + returnOrderDTO.getOrderNumber());
        }

        // Create return order entity
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnNumber(returnNumber);
        returnOrder.setOrderId(returnOrderDTO.getOrderId());
        returnOrder.setOrderNumber(returnOrderDTO.getOrderNumber());
        returnOrder.setCustomerId(returnOrderDTO.getCustomerId());
        returnOrder.setCustomerName(returnOrderDTO.getCustomerName());
        returnOrder.setCustomerEmail(returnOrderDTO.getCustomerEmail());
        returnOrder.setReturnReason(returnOrderDTO.getReturnReason());
        returnOrder.setReturnDescription(returnOrderDTO.getReturnDescription());
        returnOrder.setReturnType(returnOrderDTO.getReturnType());
        returnOrder.setPickupRequired(returnOrderDTO.getPickupRequired());
        returnOrder.setWarehouseId(returnOrderDTO.getWarehouseId());
        returnOrder.setRefundMethod(returnOrderDTO.getRefundMethod());
        returnOrder.setRestockingFee(returnOrderDTO.getRestockingFee());
        returnOrder.setShippingCostRefund(returnOrderDTO.getShippingCostRefund());
        returnOrder.setCustomerComments(returnOrderDTO.getCustomerComments());
        returnOrder.setRequestDate(LocalDateTime.now());

        // Calculate refund amount
        double totalRefund = calculateTotalRefund(returnOrderDTO);
        returnOrder.setRefundAmount(totalRefund);
        returnOrder.setRefundStatus("PENDING");

        // Create return items
        for (ReturnOrderDTO.ReturnItemDTO itemDTO : returnOrderDTO.getItems()) {
            ReturnItem returnItem = new ReturnItem();
            returnItem.setReturnOrder(returnOrder);
            returnItem.setProductId(itemDTO.getProductId());
            returnItem.setProductName(itemDTO.getProductName());
            returnItem.setProductSku(itemDTO.getProductSku());
            returnItem.setOriginalOrderItemId(itemDTO.getOriginalOrderItemId());
            returnItem.setReturnQuantity(itemDTO.getReturnQuantity());
            returnItem.setOriginalQuantity(itemDTO.getOriginalQuantity());
            returnItem.setUnitPrice(itemDTO.getUnitPrice());
            returnItem.setReturnReason(itemDTO.getReturnReason());
            returnItem.setItemCondition(itemDTO.getItemCondition());

            returnOrder.getReturnItems().add(returnItem);
        }

        ReturnOrder savedReturn = returnOrderRepository.save(returnOrder);

        // Integrate with other processes
        handleReturnIntegration(savedReturn);

        return mapToReturnOrderResponseDTO(savedReturn);
    }

    /**
     * API 2: Process restocking for returned items
     */
    @Transactional
    public RestockResponseDTO processRestocking(RestockDTO restockDTO) {
        // Find the return order
        ReturnOrder returnOrder = returnOrderRepository.findById(restockDTO.getReturnOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + restockDTO.getReturnOrderId()));

        // Validate return order status
        if (!"RECEIVED".equals(returnOrder.getReturnStatus()) && !"INSPECTING".equals(returnOrder.getReturnStatus())) {
            throw new IllegalStateException("Return order must be in RECEIVED or INSPECTING status for restocking");
        }

        // Create restock record
        RestockRecord restockRecord = new RestockRecord();
        restockRecord.setReturnOrder(returnOrder);
        restockRecord.setProductId(restockDTO.getProductId());
        restockRecord.setProductName(restockDTO.getProductName());
        restockRecord.setProductSku(restockDTO.getProductSku());
        restockRecord.setWarehouseId(restockDTO.getWarehouseId());
        restockRecord.setShelfLocationId(restockDTO.getShelfLocationId());
        restockRecord.setLocationCode(restockDTO.getLocationCode());
        restockRecord.setRestockQuantity(restockDTO.getRestockQuantity());
        restockRecord.setRestockedBy(restockDTO.getRestockedBy() != null ? restockDTO.getRestockedBy() : "system_auto");
        restockRecord.setItemCondition(restockDTO.getItemCondition());
        restockRecord.setQualityGrade(restockDTO.getQualityGrade());
        restockRecord.setOriginalCost(restockDTO.getOriginalCost());
        restockRecord.setCurrentValue(restockDTO.getCurrentValue());
        restockRecord.setValueAdjustmentReason(restockDTO.getValueAdjustmentReason());
        restockRecord.setRequiresRepair(restockDTO.getRequiresRepair());
        restockRecord.setRepairNotes(restockDTO.getRepairNotes());
        restockRecord.setIsSellable(restockDTO.getIsSellable());
        restockRecord.setSellableQuantity(restockDTO.getSellableQuantity());
        restockRecord.setRestockNotes(restockDTO.getRestockNotes());

        // Add to return order
        returnOrder.getRestockRecords().add(restockRecord);

        // Update return order status if all items are processed
        updateReturnOrderStatus(returnOrder);

        ReturnOrder updatedReturn = returnOrderRepository.save(returnOrder);

        // Handle restocking integration
        handleRestockingIntegration(restockRecord);

        return mapToRestockResponseDTO(restockRecord);
    }

    /**
     * Approve return order
     */
    @Transactional
    public ReturnOrderResponseDTO approveReturn(Long returnOrderId, String approvedBy) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + returnOrderId));

        returnOrder.approveReturn(approvedBy);

        ReturnOrder updatedReturn = returnOrderRepository.save(returnOrder);

        // Handle approval integration
        handleApprovalIntegration(updatedReturn);

        return mapToReturnOrderResponseDTO(updatedReturn);
    }

    /**
     * Reject return order
     */
    @Transactional
    public ReturnOrderResponseDTO rejectReturn(Long returnOrderId, String rejectionReason) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + returnOrderId));

        returnOrder.rejectReturn(rejectionReason);

        ReturnOrder updatedReturn = returnOrderRepository.save(returnOrder);

        return mapToReturnOrderResponseDTO(updatedReturn);
    }

    /**
     * Mark return as received
     */
    @Transactional
    public ReturnOrderResponseDTO markAsReceived(Long returnOrderId) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + returnOrderId));

        returnOrder.markAsReceived();

        ReturnOrder updatedReturn = returnOrderRepository.save(returnOrder);

        // Handle received integration
        handleReceivedIntegration(updatedReturn);

        return mapToReturnOrderResponseDTO(updatedReturn);
    }

    /**
     * Complete return processing
     */
    @Transactional
    public ReturnOrderResponseDTO completeReturn(Long returnOrderId) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + returnOrderId));

        returnOrder.completeReturn();

        ReturnOrder updatedReturn = returnOrderRepository.save(returnOrder);

        // Handle completion integration
        handleCompletionIntegration(updatedReturn);

        return mapToReturnOrderResponseDTO(updatedReturn);
    }

    /**
     * Get all return orders
     */
    public List<ReturnOrderResponseDTO> getAllReturnOrders() {
        return returnOrderRepository.findAll().stream()
                .map(this::mapToReturnOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get return order by ID
     */
    public ReturnOrderResponseDTO getReturnOrderById(Long returnOrderId) {
        ReturnOrder returnOrder = returnOrderRepository.findById(returnOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Return order not found with ID: " + returnOrderId));
        return mapToReturnOrderResponseDTO(returnOrder);
    }

    /**
     * Get returns by order ID
     */
    public List<ReturnOrderResponseDTO> getReturnsByOrderId(Long orderId) {
        return returnOrderRepository.findByOrderId(orderId).stream()
                .map(this::mapToReturnOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active returns
     */
    public List<ReturnOrderResponseDTO> getActiveReturns() {
        return returnOrderRepository.findActiveReturns().stream()
                .map(this::mapToReturnOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get returns requiring pickup
     */
    public List<ReturnOrderResponseDTO> getReturnsRequiringPickup() {
        return returnOrderRepository.findReturnsRequiringPickup().stream()
                .map(this::mapToReturnOrderResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculate total refund amount
     */
    private double calculateTotalRefund(ReturnOrderDTO returnOrderDTO) {
        return returnOrderDTO.getItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getReturnQuantity())
                .sum();
    }

    /**
     * Update return order status based on restocking progress
     */
    private void updateReturnOrderStatus(ReturnOrder returnOrder) {
        // If all items have restock records, move to processing
        int totalItems = returnOrder.getReturnItems().size();
        int restockedItems = returnOrder.getRestockRecords().size();

        if (restockedItems >= totalItems) {
            returnOrder.setReturnStatus("PROCESSING");
        }
    }

    /**
     * Handle integration when return is created
     */
    private void handleReturnIntegration(ReturnOrder returnOrder) {
        // Integration with Order system - Update order status
        updateOrderStatus(returnOrder.getOrderId(), "RETURN_REQUESTED", "Return requested: " + returnOrder.getReturnNumber());

        // Integration with Process 13 (Exceptions) - Link if applicable
        linkWithDeliveryException(returnOrder);

        // Auto-approve for certain return reasons
        if ("DAMAGED".equals(returnOrder.getReturnReason()) || "WRONG_ITEM".equals(returnOrder.getReturnReason())) {
            returnOrder.approveReturn("system_auto");
            returnOrderRepository.save(returnOrder);
        }

        System.out.println("Integration: Return " + returnOrder.getReturnNumber() + " integrated with all processes");
    }

    /**
     * Handle integration when return is approved
     */
    private void handleApprovalIntegration(ReturnOrder returnOrder) {
        // Integration with Process 11 (Shipment) - Schedule pickup if required
        if (returnOrder.requiresPickup()) {
            schedulePickup(returnOrder);
        }

        // Integration with Customer system - Send approval notification
        notifyCustomer(returnOrder.getCustomerId(), "RETURN_APPROVED",
                "Your return request " + returnOrder.getReturnNumber() + " has been approved");

        System.out.println("Integration: Return approval processed for " + returnOrder.getReturnNumber());
    }

    /**
     * Handle integration when return is received
     */
    private void handleReceivedIntegration(ReturnOrder returnOrder) {
        // Integration with Process 6 (Inventory) - Prepare for restocking
        prepareInventoryForRestock(returnOrder);

        // Integration with Quality system - Initiate inspection
        initiateQualityInspection(returnOrder);

        System.out.println("Integration: Return received for " + returnOrder.getReturnNumber());
    }

    /**
     * Handle integration when restocking is processed
     */
    private void handleRestockingIntegration(RestockRecord restockRecord) {
        // Integration with Process 6 (Inventory) - Update stock levels
        updateInventoryStock(restockRecord);

        // Integration with Process 8 (Warehouse) - Update shelf locations
        updateShelfLocation(restockRecord);

        System.out.println("Integration: Restocking processed for product " + restockRecord.getProductName());
    }

    /**
     * Handle integration when return is completed
     */
    private void handleCompletionIntegration(ReturnOrder returnOrder) {
        // Integration with Finance system - Process refund
        processRefund(returnOrder);

        // Integration with Customer system - Send completion notification
        notifyCustomer(returnOrder.getCustomerId(), "RETURN_COMPLETED",
                "Your return " + returnOrder.getReturnNumber() + " has been processed");

        System.out.println("Integration: Return completion processed for " + returnOrder.getReturnNumber());
    }

    /**
     * Integration methods (would call actual services in real implementation)
     */
    private void updateOrderStatus(Long orderId, String status, String notes) {
        System.out.println("Integration: Updating Order " + orderId + " to: " + status);
    }

    private void linkWithDeliveryException(ReturnOrder returnOrder) {
        System.out.println("Integration: Linking return with Process 13 exceptions for Order " + returnOrder.getOrderId());
    }

    private void schedulePickup(ReturnOrder returnOrder) {
        System.out.println("Integration: Scheduling pickup for return " + returnOrder.getReturnNumber());
    }

    private void notifyCustomer(Long customerId, String type, String message) {
        System.out.println("Integration: Notifying customer " + customerId + " - " + message);
    }

    private void prepareInventoryForRestock(ReturnOrder returnOrder) {
        System.out.println("Integration: Preparing inventory for restock from return " + returnOrder.getReturnNumber());
    }

    private void initiateQualityInspection(ReturnOrder returnOrder) {
        System.out.println("Integration: Initiating quality inspection for return " + returnOrder.getReturnNumber());
    }

    private void updateInventoryStock(RestockRecord restockRecord) {
        System.out.println("Integration: Updating Process 6 Inventory for product " + restockRecord.getProductId() +
                " with quantity " + restockRecord.getRestockQuantity());
    }

    private void updateShelfLocation(RestockRecord restockRecord) {
        System.out.println("Integration: Updating Process 8 Shelf location for product " + restockRecord.getProductId());
    }

    private void processRefund(ReturnOrder returnOrder) {
        System.out.println("Integration: Processing refund of " + returnOrder.getTotalRefundAmount() +
                " for return " + returnOrder.getReturnNumber());
    }

    /**
     * Generate unique return number
     */
    private String generateReturnNumber() {
        return "RET-" + System.currentTimeMillis();
    }

    /**
     * Map ReturnOrder to ResponseDTO
     */
    private ReturnOrderResponseDTO mapToReturnOrderResponseDTO(ReturnOrder returnOrder) {
        boolean requiresPickup = returnOrder.requiresPickup();
        boolean isRefundable = returnOrder.isRefundable();
        long daysSinceRequest = calculateDaysSinceRequest(returnOrder);
        String statusColor = getStatusColor(returnOrder.getReturnStatus());

        List<ReturnItemResponseDTO> itemDTOs = returnOrder.getReturnItems().stream()
                .map(item -> ReturnItemResponseDTO.builder()
                        .returnItemId(item.getReturnItemId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productSku(item.getProductSku())
                        .originalOrderItemId(item.getOriginalOrderItemId())
                        .returnQuantity(item.getReturnQuantity())
                        .originalQuantity(item.getOriginalQuantity())
                        .unitPrice(item.getUnitPrice())
                        .returnReason(item.getReturnReason())
                        .itemCondition(item.getItemCondition())
                        .isRestockable(item.getIsRestockable())
                        .restockQuantity(item.getRestockQuantity())
                        .qualityNotes(item.getQualityNotes())
                        .lineTotal(item.getUnitPrice() * item.getReturnQuantity())
                        .build())
                .collect(Collectors.toList());

        List<RestockResponseDTO> restockDTOs = returnOrder.getRestockRecords().stream()
                .map(this::mapToRestockResponseDTO)
                .collect(Collectors.toList());

        return ReturnOrderResponseDTO.builder()
                .returnOrderId(returnOrder.getReturnOrderId())
                .returnNumber(returnOrder.getReturnNumber())
                .orderId(returnOrder.getOrderId())
                .orderNumber(returnOrder.getOrderNumber())
                .customerId(returnOrder.getCustomerId())
                .customerName(returnOrder.getCustomerName())
                .customerEmail(returnOrder.getCustomerEmail())
                .returnReason(returnOrder.getReturnReason())
                .returnDescription(returnOrder.getReturnDescription())
                .returnType(returnOrder.getReturnType())
                .returnStatus(returnOrder.getReturnStatus())
                .requestDate(returnOrder.getRequestDate())
                .approvalDate(returnOrder.getApprovalDate())
                .approvedBy(returnOrder.getApprovedBy())
                .rejectionReason(returnOrder.getRejectionReason())
                .pickupRequired(returnOrder.getPickupRequired())
                .pickupScheduledDate(returnOrder.getPickupScheduledDate())
                .pickupCompletedDate(returnOrder.getPickupCompletedDate())
                .carrierForReturn(returnOrder.getCarrierForReturn())
                .returnTrackingNumber(returnOrder.getReturnTrackingNumber())
                .warehouseId(returnOrder.getWarehouseId())
                .refundAmount(returnOrder.getRefundAmount())
                .refundMethod(returnOrder.getRefundMethod())
                .refundStatus(returnOrder.getRefundStatus())
                .exchangeOrderId(returnOrder.getExchangeOrderId())
                .storeCreditAmount(returnOrder.getStoreCreditAmount())
                .restockingFee(returnOrder.getRestockingFee())
                .shippingCostRefund(returnOrder.getShippingCostRefund())
                .totalRefundAmount(returnOrder.getTotalRefundAmount())
                .qualityCheckNotes(returnOrder.getQualityCheckNotes())
                .qualityGrade(returnOrder.getQualityGrade())
                .isRestockable(returnOrder.getIsRestockable())
                .customerComments(returnOrder.getCustomerComments())
                .resolutionNotes(returnOrder.getResolutionNotes())
                .createdAt(returnOrder.getCreatedAt())
                .updatedAt(returnOrder.getUpdatedAt())
                .requiresPickup(requiresPickup)
                .isRefundable(isRefundable)
                .daysSinceRequest(daysSinceRequest)
                .statusColor(statusColor)
                .returnItems(itemDTOs)
                .restockRecords(restockDTOs)
                .build();
    }

    /**
     * Map RestockRecord to ResponseDTO
     */
    private RestockResponseDTO mapToRestockResponseDTO(RestockRecord restockRecord) {
        boolean isFullySellable = restockRecord.isFullySellable();
        double valueRetentionRate = calculateValueRetention(restockRecord);
        String restockEfficiency = calculateRestockEfficiency(restockRecord);

        return RestockResponseDTO.builder()
                .restockId(restockRecord.getRestockId())
                .returnOrderId(restockRecord.getReturnOrder().getReturnOrderId())
                .productId(restockRecord.getProductId())
                .productName(restockRecord.getProductName())
                .productSku(restockRecord.getProductSku())
                .warehouseId(restockRecord.getWarehouseId())
                .shelfLocationId(restockRecord.getShelfLocationId())
                .locationCode(restockRecord.getLocationCode())
                .restockQuantity(restockRecord.getRestockQuantity())
                .restockDate(restockRecord.getRestockDate())
                .restockedBy(restockRecord.getRestockedBy())
                .itemCondition(restockRecord.getItemCondition())
                .qualityGrade(restockRecord.getQualityGrade())
                .originalCost(restockRecord.getOriginalCost())
                .currentValue(restockRecord.getCurrentValue())
                .valueAdjustmentReason(restockRecord.getValueAdjustmentReason())
                .requiresRepair(restockRecord.getRequiresRepair())
                .repairNotes(restockRecord.getRepairNotes())
                .isSellable(restockRecord.getIsSellable())
                .sellableQuantity(restockRecord.getSellableQuantity())
                .restockNotes(restockRecord.getRestockNotes())
                .createdAt(restockRecord.getCreatedAt())
                .updatedAt(restockRecord.getUpdatedAt())
                .isFullySellable(isFullySellable)
                .valueRetentionRate(valueRetentionRate)
                .restockEfficiency(restockEfficiency)
                .build();
    }

    /**
     * Calculate days since return request
     */
    private long calculateDaysSinceRequest(ReturnOrder returnOrder) {
        return ChronoUnit.DAYS.between(returnOrder.getRequestDate(), LocalDateTime.now());
    }

    /**
     * Calculate value retention rate
     */
    private double calculateValueRetention(RestockRecord restockRecord) {
        if (restockRecord.getOriginalCost() != null && restockRecord.getCurrentValue() != null &&
                restockRecord.getOriginalCost() > 0) {
            return (restockRecord.getCurrentValue() / restockRecord.getOriginalCost()) * 100;
        }
        return 100.0; // Default to 100% if no cost data
    }

    /**
     * Calculate restocking efficiency
     */
    private String calculateRestockEfficiency(RestockRecord restockRecord) {
        if (restockRecord.getSellableQuantity() != null && restockRecord.getRestockQuantity() != null) {
            double efficiency = (double) restockRecord.getSellableQuantity() / restockRecord.getRestockQuantity() * 100;
            if (efficiency >= 90) return "EXCELLENT";
            if (efficiency >= 75) return "GOOD";
            if (efficiency >= 50) return "FAIR";
            return "POOR";
        }
        return "N/A";
    }

    /**
     * Get status color for UI
     */
    private String getStatusColor(String status) {
        switch (status) {
            case "REQUESTED": return "blue";
            case "APPROVED": return "orange";
            case "RECEIVED": return "purple";
            case "INSPECTING": return "yellow";
            case "PROCESSING": return "teal";
            case "COMPLETED": return "green";
            case "REJECTED": return "red";
            case "CANCELLED": return "gray";
            default: return "black";
        }
    }
}