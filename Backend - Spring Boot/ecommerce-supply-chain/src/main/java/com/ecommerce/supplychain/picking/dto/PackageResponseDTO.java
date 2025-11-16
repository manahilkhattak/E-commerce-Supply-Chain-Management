package com.ecommerce.supplychain.picking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for package information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageResponseDTO {

    private Long packageId;
    private String trackingNumber;
    private Long orderId;
    private String orderNumber;
    private Long pickListId;
    private Long warehouseId;
    private String packageType;
    private String packageSize;
    private BigDecimal weightKg;
    private String dimensions;
    private String packageStatus;
    private String packedBy;
    private LocalDateTime packedAt;
    private String carrier;
    private String serviceType;
    private BigDecimal shippingCost;
    private BigDecimal insuranceAmount;
    private Boolean requiresSignature;
    private Boolean isFragile;
    private Boolean isHazardous;
    private String temperatureControl;
    private Boolean customsDeclarationRequired;
    private String packageNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totalItems;
    private Boolean isReadyForShipment;
    private List<PackageItemResponseDTO> items;

    /**
     * Response DTO for package items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PackageItemResponseDTO {
        private Long packageItemId;
        private Long productId;
        private String productName;
        private String productSku;
        private Integer quantity;
        private BigDecimal weightKg;
        private BigDecimal unitPrice;
        private Boolean isFragile;
        private Boolean requiresSpecialHandling;
        private String itemNotes;
        private BigDecimal totalWeight;
    }
}
