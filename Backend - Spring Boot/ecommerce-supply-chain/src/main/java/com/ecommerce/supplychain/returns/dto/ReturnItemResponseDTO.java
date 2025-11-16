package com.ecommerce.supplychain.returns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnItemResponseDTO {
    private Long returnItemId;
    private Long productId;
    private String productName;
    private String productSku;
    private Long originalOrderItemId;
    private Integer returnQuantity;
    private Integer originalQuantity;
    private Double unitPrice;
    private String returnReason;
    private String itemCondition;
    private Boolean isRestockable;
    private Integer restockQuantity;
    private String qualityNotes;
    private Double lineTotal;
}
