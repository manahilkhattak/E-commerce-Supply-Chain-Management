package com.ecommerce.supplychain.inventory.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for inventory stock updates from sales or adjustments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Update type is required")
    @Pattern(regexp = "SALE|RETURN|ADJUSTMENT|RESERVE|RELEASE",
            message = "Update type must be SALE, RETURN, ADJUSTMENT, RESERVE, or RELEASE")
    private String updateType;

    @NotBlank(message = "Reference number is required")
    private String referenceNumber; // Order number, return number, etc.

    private String notes;
}
