package com.ecommerce.supplychain.returns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnOrderResponseDTO {

    private Long returnOrderId;
    private String returnNumber;
    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String returnReason;
    private String returnDescription;
    private String returnType;
    private String returnStatus;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private String approvedBy;
    private String rejectionReason;
    private Boolean pickupRequired;
    private LocalDateTime pickupScheduledDate;
    private LocalDateTime pickupCompletedDate;
    private String carrierForReturn;
    private String returnTrackingNumber;
    private Long warehouseId;
    private Double refundAmount;
    private String refundMethod;
    private String refundStatus;
    private Long exchangeOrderId;
    private Double storeCreditAmount;
    private Double restockingFee;
    private Double shippingCostRefund;
    private Double totalRefundAmount;
    private String qualityCheckNotes;
    private String qualityGrade;
    private Boolean isRestockable;
    private String customerComments;
    private String resolutionNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean requiresPickup;
    private Boolean isRefundable;
    private Long daysSinceRequest;
    private String statusColor;
    private List<ReturnItemResponseDTO> returnItems;
    private List<RestockResponseDTO> restockRecords;
}

