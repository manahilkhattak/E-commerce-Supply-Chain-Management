package com.ecommerce.supplychain.picking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating pick item status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickItemUpdateDTO {

    @NotNull(message = "Pick item ID is required")
    private Long pickItemId;

    @NotNull(message = "Picked quantity is required")
    private Integer pickedQuantity;

    private String pickNotes;
    private String pickedBy;
}
