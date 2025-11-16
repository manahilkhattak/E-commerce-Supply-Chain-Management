package com.ecommerce.supplychain.supplier.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a Supplier in the supply chain system.
 * Stores supplier information including contact details, ratings, and status.
 */
@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Column(name = "business_license", length = 100)
    private String businessLicense;

    @Column(name = "rating")  // ← CHANGED: Removed precision and scale
    private Double rating;

    @Column(name = "status", length = 50)
    private String status; // PENDING, APPROVED, ACTIVE, SUSPENDED, REJECTED

    @Column(name = "payment_terms", length = 100)
    private String paymentTerms; // NET30, NET60, COD, etc.

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
        if (rating == null) {
            rating = 0.0;
        }
    }
}