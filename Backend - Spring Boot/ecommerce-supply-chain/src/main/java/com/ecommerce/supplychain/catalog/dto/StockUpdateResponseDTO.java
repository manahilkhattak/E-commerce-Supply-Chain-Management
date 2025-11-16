package com.ecommerce.supplychain.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for stock update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateResponseDTO {

    private Long productId;
    private String productName;
    private String productSku;
    private Integer previousStock;
    private Integer newStock;
    private Integer quantityChanged;
    private String adjustmentType;
    private String reason;
    private String referenceNumber;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String status;
    private Boolean stockAlert;
    private String stockLevel;
}