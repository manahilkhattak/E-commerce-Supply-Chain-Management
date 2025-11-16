package com.ecommerce.supplychain.catalog.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Data Transfer Object for creating/updating products in catalog.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String productSku;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private String brand;

    private Long supplierId;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.01", message = "Selling price must be greater than 0")
    private BigDecimal sellingPrice;

    @DecimalMin(value = "0.00", message = "Cost price cannot be negative")
    private BigDecimal costPrice;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock;

    @NotNull(message = "Minimum stock level is required")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minimumStockLevel;

    @Min(value = 1, message = "Maximum stock level must be at least 1")
    private Integer maximumStockLevel;

    @Min(value = 0, message = "Reorder point cannot be negative")
    private Integer reorderPoint;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private Double weight;

    private String dimensions;

    @Pattern(regexp = "PIECE|KG|LITER|BOX|METER|SET",
            message = "Unit must be PIECE, KG, LITER, BOX, METER, or SET")
    private String unitOfMeasurement;

    private String barcode;
    private String imageUrl;
    private Boolean isActive;
}
