package com.ecommerce.supplychain.procurement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for Purchase Order information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponseDTO {

    private Long poId;
    private String poNumber;
    private Long supplierId;
    private Long contractId;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String status;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String currency;
    private String paymentTerms;
    private String deliveryAddress;
    private String requestedBy;
    private String approvedBy;
    private LocalDate approvalDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalItems;
    private List<PurchaseOrderItemResponseDTO> items;
    private Integer daysUntilDelivery; // Calculated field

    /**
     * Nested Response DTO for Purchase Order Items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseOrderItemResponseDTO {
        private Long itemId;
        private Long productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private Integer receivedQuantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
        private BigDecimal taxRate;
        private BigDecimal discountRate;
        private String unitOfMeasurement;
        private String notes;
        private Boolean fullyReceived;
    }
}