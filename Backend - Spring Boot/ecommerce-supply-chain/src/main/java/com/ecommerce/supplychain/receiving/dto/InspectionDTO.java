package com.ecommerce.supplychain.receiving.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for performing quality inspection on received items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionDTO {

    @NotNull(message = "Receipt ID is required")
    private Long receiptId;

    @NotNull(message = "Inspection record ID is required")
    private Long inspectionId;

    @NotNull(message = "Accepted quantity is required")
    @Min(value = 0, message = "Accepted quantity cannot be negative")
    private Integer acceptedQuantity;

    @Min(value = 0, message = "Rejected quantity cannot be negative")
    private Integer rejectedQuantity;

    @Min(value = 0, message = "Damaged quantity cannot be negative")
    private Integer damagedQuantity;

    @NotBlank(message = "Inspector name is required")
    private String inspectorName;

    @Min(value = 1, message = "Quality rating must be between 1 and 5")
    @Max(value = 5, message = "Quality rating must be between 1 and 5")
    private Integer qualityRating;

    @Pattern(regexp = "DAMAGED|EXPIRED|WRONG_ITEM|POOR_QUALITY|PACKAGING_ISSUE|NONE",
            message = "Invalid defect type")
    private String defectType;

    @NotBlank(message = "Action taken is required")
    @Pattern(regexp = "ACCEPT_ALL|PARTIAL_ACCEPT|REJECT_ALL|RETURN_TO_SUPPLIER",
            message = "Action must be ACCEPT_ALL, PARTIAL_ACCEPT, REJECT_ALL, or RETURN_TO_SUPPLIER")
    private String actionTaken;

    private String inspectionNotes;
    private String photoUrl;
}