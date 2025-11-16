package com.ecommerce.supplychain.supplier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for approving or rejecting supplier applications.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierApprovalDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "APPROVED|REJECTED", message = "Status must be either APPROVED or REJECTED")
    private String status;

    private String notes; // Admin notes for approval/rejection
}