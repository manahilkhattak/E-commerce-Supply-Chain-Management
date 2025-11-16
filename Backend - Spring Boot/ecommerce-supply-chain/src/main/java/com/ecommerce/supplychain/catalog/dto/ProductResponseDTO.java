package com.ecommerce.supplychain.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for product information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long productId;
    private String productName;
    private String productSku;
    private String description;
    private String category;
    private String brand;
    private Long supplierId;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer currentStock;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private Integer reorderPoint;
    private Double weight;
    private String dimensions;
    private String unitOfMeasurement;
    private String barcode;
    private String imageUrl;
    private Boolean isActive;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastStockUpdate;
    private Boolean needsReorder;
    private Integer stockDeficit;
}