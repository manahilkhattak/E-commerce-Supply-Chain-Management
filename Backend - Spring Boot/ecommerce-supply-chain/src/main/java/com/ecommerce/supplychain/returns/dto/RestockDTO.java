package com.ecommerce.supplychain.returns.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestockDTO {

    @NotNull(message = "Return order ID is required")
    private Long returnOrderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private Long shelfLocationId;

    private String locationCode;

    @NotNull(message = "Restock quantity is required")
    @Min(value = 1, message = "Restock quantity must be at least 1")
    private Integer restockQuantity;

    private String restockedBy;

    private String itemCondition;

    private String qualityGrade;

    private Double originalCost;

    private Double currentValue;

    private String valueAdjustmentReason;

    private Boolean requiresRepair;

    private String repairNotes;

    private Boolean isSellable;

    private Integer sellableQuantity;

    private String restockNotes;
}