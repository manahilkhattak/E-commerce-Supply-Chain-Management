package com.ecommerce.supplychain.supplier.service;

import com.ecommerce.supplychain.supplier.dto.SupplierApprovalDTO;
import com.ecommerce.supplychain.supplier.dto.SupplierRegistrationDTO;
import com.ecommerce.supplychain.supplier.dto.SupplierResponseDTO;
import com.ecommerce.supplychain.supplier.model.Supplier;
import com.ecommerce.supplychain.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling supplier registration and onboarding business logic.
 * Manages supplier lifecycle from registration to approval.
 */
@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Register a new supplier in the system.
     * Validates email uniqueness and sets initial status to PENDING.
     */
    @Transactional
    public SupplierResponseDTO registerSupplier(SupplierRegistrationDTO registrationDTO) {
        // Check if email already exists
        if (supplierRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Supplier with email " + registrationDTO.getEmail() + " already exists");
        }

        // Create new supplier entity
        Supplier supplier = new Supplier();
        supplier.setCompanyName(registrationDTO.getCompanyName());
        supplier.setContactPerson(registrationDTO.getContactPerson());
        supplier.setEmail(registrationDTO.getEmail());
        supplier.setPhone(registrationDTO.getPhone());
        supplier.setAddress(registrationDTO.getAddress());
        supplier.setCity(registrationDTO.getCity());
        supplier.setCountry(registrationDTO.getCountry());
        supplier.setTaxId(registrationDTO.getTaxId());
        supplier.setBusinessLicense(registrationDTO.getBusinessLicense());
        supplier.setPaymentTerms(registrationDTO.getPaymentTerms() != null ?
                registrationDTO.getPaymentTerms() : "NET30");
        supplier.setNotes(registrationDTO.getNotes());
        supplier.setStatus("PENDING");
        supplier.setRating(0.0);
        supplier.setRegisteredAt(LocalDateTime.now());

        // Save to database
        Supplier savedSupplier = supplierRepository.save(supplier);

        return mapToResponseDTO(savedSupplier);
    }

    /**
     * Approve or reject a supplier application.
     * Changes supplier status and records approval timestamp.
     */
    @Transactional
    public SupplierResponseDTO approveOrRejectSupplier(Long supplierId, SupplierApprovalDTO approvalDTO) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + supplierId));

        // Check if supplier is in PENDING status
        if (!"PENDING".equals(supplier.getStatus())) {
            throw new IllegalStateException("Only PENDING suppliers can be approved/rejected. Current status: " + supplier.getStatus());
        }

        // Update status
        supplier.setStatus(approvalDTO.getStatus());

        if ("APPROVED".equals(approvalDTO.getStatus())) {
            supplier.setApprovedAt(LocalDateTime.now());
            supplier.setStatus("ACTIVE"); // APPROVED suppliers become ACTIVE
        }

        // Add admin notes
        if (approvalDTO.getNotes() != null && !approvalDTO.getNotes().isEmpty()) {
            String existingNotes = supplier.getNotes() != null ? supplier.getNotes() + "\n\n" : "";
            supplier.setNotes(existingNotes + "Admin Review: " + approvalDTO.getNotes());
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return mapToResponseDTO(updatedSupplier);
    }

    /**
     * Get all suppliers in the system.
     */
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get supplier by ID.
     */
    public SupplierResponseDTO getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + supplierId));
        return mapToResponseDTO(supplier);
    }

    /**
     * Get suppliers by status (PENDING, ACTIVE, REJECTED, SUSPENDED).
     */
    public List<SupplierResponseDTO> getSuppliersByStatus(String status) {
        return supplierRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update supplier rating.
     */
    @Transactional
    public SupplierResponseDTO updateSupplierRating(Long supplierId, Double rating) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + supplierId));

        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }

        supplier.setRating(rating);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return mapToResponseDTO(updatedSupplier);
    }

    /**
     * Delete a supplier (soft delete by changing status).
     */
    @Transactional
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with ID: " + supplierId));

        supplier.setStatus("DELETED");
        supplierRepository.save(supplier);
    }

    /**
     * Helper method to convert Supplier entity to DTO.
     */
    private SupplierResponseDTO mapToResponseDTO(Supplier supplier) {
        return SupplierResponseDTO.builder()
                .supplierId(supplier.getSupplierId())
                .companyName(supplier.getCompanyName())
                .contactPerson(supplier.getContactPerson())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .country(supplier.getCountry())
                .taxId(supplier.getTaxId())
                .businessLicense(supplier.getBusinessLicense())
                .rating(supplier.getRating())
                .status(supplier.getStatus())
                .paymentTerms(supplier.getPaymentTerms())
                .registeredAt(supplier.getRegisteredAt())
                .approvedAt(supplier.getApprovedAt())
                .notes(supplier.getNotes())
                .build();
    }
}