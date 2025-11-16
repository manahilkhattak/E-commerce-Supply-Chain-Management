package com.ecommerce.supplychain.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for stock alert information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAlertResponseDTO {

    private Long alertId;
    private Long inventoryId;
    private Long productId;
    private String productName;
    private String productSku;
    private String alertType;
    private String alertLevel;
    private Integer currentStock;
    private Integer thresholdStock;
    private String message;
    private Boolean isResolved;
    private String resolvedBy;
    private String resolutionNotes;
    private LocalDateTime resolvedAt;
    private String suggestedAction;
    private Boolean notificationSent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long daysOpen; // How long the alert has been open
}
