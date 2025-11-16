package com.ecommerce.supplychain.receiving.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating Goods Receipt entries.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptDTO {

    @NotBlank(message = "Receipt number is required")
    @Size(max = 50, message = "Receipt number must not exceed 50 characters")
    private String receiptNumber;

    @NotNull(message = "Purchase Order ID is required")
    private Long poId;

    private String poNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    private LocalDate receiptDate;

    @NotBlank(message = "Received by is required")
    private String receivedBy;

    @NotBlank(message = "Warehouse location is required")
    private String warehouseLocation;

    private String deliveryNoteNumber;
    private String vehicleNumber;
    private String notes;

    @NotEmpty(message = "Receipt must contain at least one item for inspection")
    @Valid
    private List<InspectionItemDTO> items;

    /**
     * Nested DTO for items being received
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectionItemDTO {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        private String productSku;

        @NotNull(message = "Ordered quantity is required")
        @Min(value = 1, message = "Ordered quantity must be at least 1")
        private Integer orderedQuantity;

        @NotNull(message = "Received quantity is required")
        @Min(value = 0, message = "Received quantity cannot be negative")
        private Integer receivedQuantity;

        private String batchNumber;
        private LocalDate expiryDate;
        private String notes;
    }
}