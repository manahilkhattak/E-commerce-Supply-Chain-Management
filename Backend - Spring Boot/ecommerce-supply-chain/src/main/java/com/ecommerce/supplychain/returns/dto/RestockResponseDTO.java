package com.ecommerce.supplychain.returns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestockResponseDTO {

    private Long restockId;
    private Long returnOrderId;
    private Long productId;
    private String productName;
    private String productSku;
    private Long warehouseId;
    private Long shelfLocationId;
    private String locationCode;
    private Integer restockQuantity;
    private LocalDateTime restockDate;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional calculated fields
    private Boolean isFullySellable;
    private Double valueRetentionRate;
    private String restockEfficiency;
}