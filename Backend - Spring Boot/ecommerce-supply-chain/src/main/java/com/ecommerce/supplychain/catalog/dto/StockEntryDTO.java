package com.ecommerce.supplychain.catalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for stock updates and entries.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockEntryDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Adjustment type is required")
    @Pattern(regexp = "ADD|REMOVE|SET",
            message = "Adjustment type must be ADD, REMOVE, or SET")
    private String adjustmentType;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String referenceNumber; // PO number, Receipt number, etc.

    private String notes;

    @NotBlank(message = "Updated by is required")
    private String updatedBy;
}