package com.ecommerce.supplychain.returns.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String customerName;

    private String customerEmail;

    @NotBlank(message = "Return reason is required")
    @Pattern(regexp = "DAMAGED|WRONG_ITEM|NOT_AS_DESCRIBED|SIZE_ISSUE|CHANGE_MIND",
            message = "Return reason must be DAMAGED, WRONG_ITEM, NOT_AS_DESCRIBED, SIZE_ISSUE, or CHANGE_MIND")
    private String returnReason;

    private String returnDescription;

    @NotBlank(message = "Return type is required")
    @Pattern(regexp = "REFUND|EXCHANGE|STORE_CREDIT",
            message = "Return type must be REFUND, EXCHANGE, or STORE_CREDIT")
    private String returnType;

    private Boolean pickupRequired;

    private Long warehouseId;

    private String refundMethod;

    private Double restockingFee;

    private Double shippingCostRefund;

    private String customerComments;

    @NotEmpty(message = "Return items are required")
    @Valid
    private List<ReturnItemDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnItemDTO {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        @NotBlank(message = "Product SKU is required")
        private String productSku;

        @NotNull(message = "Original order item ID is required")
        private Long originalOrderItemId;

        @NotNull(message = "Return quantity is required")
        @Min(value = 1, message = "Return quantity must be at least 1")
        private Integer returnQuantity;

        @NotNull(message = "Original quantity is required")
        @Min(value = 1, message = "Original quantity must be at least 1")
        private Integer originalQuantity;

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.0", message = "Unit price must be positive")
        private Double unitPrice;

        private String returnReason;

        private String itemCondition;
    }
}