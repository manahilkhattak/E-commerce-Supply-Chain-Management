package com.ecommerce.supplychain.contract.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a Contract with a supplier.
 * Manages contract terms, pricing, and validity periods.
 */
@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "contract_number", unique = true, nullable = false, length = 50)
    private String contractNumber;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId; // Foreign key reference to Supplier

    @Column(name = "contract_title", nullable = false, length = 255)
    private String contractTitle;

    @Column(name = "contract_type", length = 50)
    private String contractType; // SUPPLY, SERVICE, DISTRIBUTION, etc.

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "contract_value", precision = 15, scale = 2)
    private BigDecimal contractValue;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "payment_terms", length = 100)
    private String paymentTerms;

    @Column(name = "renewal_terms", columnDefinition = "TEXT")
    private String renewalTerms;

    @Column(name = "status", length = 50)
    private String status; // DRAFT, ACTIVE, EXPIRED, TERMINATED, RENEWED

    @Column(name = "signed_date")
    private LocalDate signedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "DRAFT";
        }
        if (currency == null) {
            currency = "USD";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}