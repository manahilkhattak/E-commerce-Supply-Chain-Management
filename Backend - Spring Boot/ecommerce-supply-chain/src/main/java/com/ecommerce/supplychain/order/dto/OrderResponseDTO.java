package com.ecommerce.supplychain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private String billingAddress;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String currency;
    private Long warehouseId;
    private String priorityLevel;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String orderNotes;
    private String internalNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Links to other processes
    private Long pickListId;
    private Long shipmentId;
    private Long qualityCheckId;

    private Boolean canBeCancelled;
    private Boolean requiresQualityCheck;
    private Integer totalItems;

    private List<OrderItemResponseDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponseDTO {
        private Long orderItemId;
        private Long productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private BigDecimal weightKg;
        private Boolean isFragile;
        private Boolean requiresQualityCheck;
        private String category;
        private String brand;
        private String itemNotes;
        private Long inventoryId;
        private Long shelfLocationId;
    }
}