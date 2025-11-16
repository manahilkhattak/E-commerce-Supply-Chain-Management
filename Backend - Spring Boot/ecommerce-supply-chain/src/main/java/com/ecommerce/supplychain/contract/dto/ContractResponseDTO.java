package com.ecommerce.supplychain.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for contract information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponseDTO {

    private Long contractId;
    private String contractNumber;
    private Long supplierId;
    private String contractTitle;
    private String contractType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractValue;
    private String currency;
    private String paymentTerms;
    private String renewalTerms;
    private String status;
    private LocalDate signedDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer daysUntilExpiry; // Calculated field
}