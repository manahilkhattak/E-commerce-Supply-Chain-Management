package com.ecommerce.supplychain.procurement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for creating/updating Purchase Orders.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {

    @NotBlank(message = "PO number is required")
    @Size(max = 50, message = "PO number must not exceed 50 characters")
    private String poNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    private Long contractId; // Optional

    @FutureOrPresent(message = "Expected delivery date must be today or in the future")
    private LocalDate expectedDeliveryDate;

    @DecimalMin(value = "0.0", message = "Tax amount must be positive")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Discount amount must be positive")
    private BigDecimal discountAmount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "USD|EUR|GBP|PKR", message = "Currency must be USD, EUR, GBP, or PKR")
    private String currency;

    @NotBlank(message = "Payment terms are required")
    private String paymentTerms;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "Requested by is required")
    private String requestedBy;

    private String notes;

    @NotEmpty(message = "Purchase order must contain at least one item")
    @Valid
    private List<PurchaseOrderItemDTO> items;

    /**
     * Nested DTO for Purchase Order Items
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseOrderItemDTO {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotBlank(message = "Product name is required")
        private String productName;

        private String productSku;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
        private BigDecimal unitPrice;

        @DecimalMin(value = "0.0", message = "Tax rate must be positive")
        private BigDecimal taxRate;

        @DecimalMin(value = "0.0", message = "Discount rate must be positive")
        private BigDecimal discountRate;

        @NotBlank(message = "Unit of measurement is required")
        @Pattern(regexp = "PIECE|KG|LITER|BOX|METER|SET",
                message = "Unit must be PIECE, KG, LITER, BOX, METER, or SET")
        private String unitOfMeasurement;

        private String notes;
    }
}