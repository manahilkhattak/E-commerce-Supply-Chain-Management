package com.ecommerce.supplychain.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for supplier registration requests.
 * Contains validation rules for supplier onboarding.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegistrationDTO {

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 255, message = "Company name must be between 3 and 255 characters")
    private String companyName;

    @NotBlank(message = "Contact person name is required")
    @Size(max = 100, message = "Contact person name must not exceed 100 characters")
    private String contactPerson;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @NotBlank(message = "Business license is required")
    private String businessLicense;

    private String paymentTerms; // Optional, defaults to NET30

    private String notes;
}