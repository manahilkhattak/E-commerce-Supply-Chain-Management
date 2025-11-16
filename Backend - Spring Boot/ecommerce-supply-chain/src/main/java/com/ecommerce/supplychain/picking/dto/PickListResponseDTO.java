package com.ecommerce.supplychain.picking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for pick list information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickListResponseDTO {

    private Long pickListId;
    private String pickListNumber;
    private Long orderId;
    private String orderNumber;
    private Long warehouseId;
    private String warehouseName;
    private String assignedTo;
    private String priorityLevel;
    private String pickStatus;
    private Integer totalItems;
    private Integer pickedItems;
    private Integer remainingItems;
    private Integer estimatedPickTimeMinutes;
    private Integer actualPickTimeMinutes;
    private Boolean pickRouteOptimized;
    private String zoneSequence;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String pickNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double pickEfficiency;
    private Boolean isCompleted;
    private List<PickListItemResponseDTO> items;

    /**
     * Response DTO for pick list items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PickListItemResponseDTO {
        private Long pickItemId;
        private Long productId;
        private String productName;
        private String productSku;
        private Long shelfLocationId;
        private String locationCode;
        private Integer requiredQuantity;
        private Integer pickedQuantity;
        private Boolean isPicked;
        private Integer pickSequence;
        private String zoneCode;
        private String aisleNumber;
        private Double weightPerUnitKg;
        private String pickNotes;
        private LocalDateTime pickedAt;
        private String fullLocationPath;
    }
}
