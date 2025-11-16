package com.ecommerce.supplychain.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for supplier response data.
 * Used to send supplier information back to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponseDTO {

    private Long supplierId;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String taxId;
    private String businessLicense;
    private Double rating;
    private String status;
    private String paymentTerms;
    private LocalDateTime registeredAt;
    private LocalDateTime approvedAt;
    private String notes;
}