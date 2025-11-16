package com.ecommerce.supplychain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDTO {

    @NotBlank(message = "Order status is required")
    private String orderStatus;

    private String internalNotes;
}