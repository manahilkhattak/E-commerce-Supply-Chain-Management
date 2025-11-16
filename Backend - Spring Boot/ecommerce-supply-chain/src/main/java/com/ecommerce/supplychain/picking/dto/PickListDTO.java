package com.ecommerce.supplychain.picking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for creating pick lists.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickListDTO {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private String assignedTo;
    private String priorityLevel;

    @NotEmpty(message = "Pick list must contain items")
    @Valid
    private List<PickListItemDTO> items;

    /**
     * DTO for pick list items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PickListItemDTO {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        @NotBlank(message = "Product SKU is required")
        private String productSku;

        @NotNull(message = "Shelf location ID is required")
        private Long shelfLocationId;

        @NotBlank(message = "Location code is required")
        private String locationCode;

        @NotNull(message = "Required quantity is required")
        @Min(value = 1, message = "Required quantity must be at least 1")
        private Integer requiredQuantity;

        private Integer pickSequence;
        private Double weightPerUnitKg;
        private String zoneCode;
        private String aisleNumber;
    }
}
