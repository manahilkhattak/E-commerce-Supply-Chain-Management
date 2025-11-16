package com.ecommerce.supplychain.picking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for creating packages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotNull(message = "Pick list ID is required")
    private Long pickListId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @Pattern(regexp = "BOX|ENVELOPE|PALLET|CARTON",
            message = "Package type must be BOX, ENVELOPE, PALLET, or CARTON")
    private String packageType;

    @Pattern(regexp = "SMALL|MEDIUM|LARGE|EXTRA_LARGE",
            message = "Package size must be SMALL, MEDIUM, LARGE, or EXTRA_LARGE")
    private String packageSize;

    private String dimensions;
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

    @NotEmpty(message = "Package must contain items")
    @Valid
    private List<PackageItemDTO> items;

    /**
     * DTO for package items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageItemDTO {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        @NotBlank(message = "Product SKU is required")
        private String productSku;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Weight is required")
        @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
        private BigDecimal weightKg;

        private BigDecimal unitPrice;
        private Boolean isFragile;
        private Boolean requiresSpecialHandling;
        private String itemNotes;
    }
}
