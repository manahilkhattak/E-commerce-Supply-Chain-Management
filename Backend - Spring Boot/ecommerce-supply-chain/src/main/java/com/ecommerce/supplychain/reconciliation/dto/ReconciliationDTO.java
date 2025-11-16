package com.ecommerce.supplychain.reconciliation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReconciliationDTO {

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "CYCLE_COUNT|PHYSICAL_INVENTORY|SYSTEM_AUDIT|DISCREPANCY_ANALYSIS",
            message = "Report type must be CYCLE_COUNT, PHYSICAL_INVENTORY, SYSTEM_AUDIT, or DISCREPANCY_ANALYSIS")
    private String reportType;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private String warehouseName;

    @NotNull(message = "Report period start is required")
    private LocalDateTime reportPeriodStart;

    @NotNull(message = "Report period end is required")
    private LocalDateTime reportPeriodEnd;

    private String conductedBy;

    private String notes;
}