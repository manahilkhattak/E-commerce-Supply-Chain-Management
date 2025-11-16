package com.ecommerce.supplychain.reconciliation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    @NotNull(message = "Report ID is required")
    private Long reportId;

    @NotBlank(message = "Summary findings are required")
    private String summaryFindings;

    private String correctiveActions;

    private String preventiveMeasures;

    private String reviewedBy;

    private String approvalStatus;

    private String approvedBy;
}