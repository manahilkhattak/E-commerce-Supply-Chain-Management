package com.ecommerce.supplychain.quality.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting quality recheck.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecheckRequestDTO {

    @NotNull(message = "Check ID is required")
    private Long checkId;

    @NotBlank(message = "Recheck notes are required")
    private String recheckNotes;

    @NotBlank(message = "Requested by is required")
    private String requestedBy;
}
