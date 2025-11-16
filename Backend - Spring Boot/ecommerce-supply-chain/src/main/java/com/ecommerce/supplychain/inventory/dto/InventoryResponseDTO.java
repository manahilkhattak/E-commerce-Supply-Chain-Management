package com.ecommerce.supplychain.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for inventory monitoring information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {

    private Long inventoryId;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer currentStock;
    private Integer reservedStock;
    private Integer availableStock;
    private Integer minimumStockLevel;
    private Integer maximumStockLevel;
    private Integer reorderPoint;
    private Double stockValue;
    private Double stockTurnoverRate;
    private Integer daysOfSupply;
    private String stockStatus;
    private LocalDateTime lastRestockedDate;
    private LocalDateTime lastSoldDate;
    private String movementFrequency;
    private Boolean isMonitored;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean needsAttention;
    private Integer stockDeficit;
    private Double stockCoverage; // days
}
