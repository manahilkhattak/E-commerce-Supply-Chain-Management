package com.ecommerce.supplychain.contract.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for creating/updating contracts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {

    @NotBlank(message = "Contract number is required")
    @Size(max = 50, message = "Contract number must not exceed 50 characters")
    private String contractNumber;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Contract title is required")
    @Size(min = 5, max = 255, message = "Contract title must be between 5 and 255 characters")
    private String contractTitle;

    @NotBlank(message = "Contract type is required")
    @Pattern(regexp = "SUPPLY|SERVICE|DISTRIBUTION|MAINTENANCE|CONSULTING",
            message = "Contract type must be SUPPLY, SERVICE, DISTRIBUTION, MAINTENANCE, or CONSULTING")
    private String contractType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Contract value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Contract value must be greater than 0")
    private BigDecimal contractValue;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "USD|EUR|GBP|PKR", message = "Currency must be USD, EUR, GBP, or PKR")
    private String currency;

    @NotBlank(message = "Payment terms are required")
    private String paymentTerms;

    private String renewalTerms;
    private String notes;
}