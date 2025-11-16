package com.ecommerce.supplychain.supplier.controller;

import com.ecommerce.supplychain.supplier.dto.SupplierApprovalDTO;
import com.ecommerce.supplychain.supplier.dto.SupplierRegistrationDTO;
import com.ecommerce.supplychain.supplier.dto.SupplierResponseDTO;
import com.ecommerce.supplychain.supplier.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Supplier Registration & Onboarding APIs.
 * Provides endpoints for supplier management operations.
 */
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * API 1: Register a new supplier
     * POST /api/suppliers/register
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerSupplier(
            @Valid @RequestBody SupplierRegistrationDTO registrationDTO) {
        try {
            SupplierResponseDTO response = supplierService.registerSupplier(registrationDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Supplier registered successfully. Application is pending approval.");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * API 2: Approve or reject supplier application
     * PUT /api/suppliers/{supplierId}/approval
     */
    @PutMapping("/{supplierId}/approval")
    public ResponseEntity<Map<String, Object>> approveOrRejectSupplier(
            @PathVariable Long supplierId,
            @Valid @RequestBody SupplierApprovalDTO approvalDTO) {
        try {
            SupplierResponseDTO response = supplierService.approveOrRejectSupplier(supplierId, approvalDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Supplier application " + approvalDTO.getStatus().toLowerCase() + " successfully.");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all suppliers
     * GET /api/suppliers
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSuppliers() {
        List<SupplierResponseDTO> suppliers = supplierService.getAllSuppliers();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", suppliers.size());
        response.put("data", suppliers);

        return ResponseEntity.ok(response);
    }

    /**
     * Get supplier by ID
     * GET /api/suppliers/{supplierId}
     */
    @GetMapping("/{supplierId}")
    public ResponseEntity<Map<String, Object>> getSupplierById(@PathVariable Long supplierId) {
        try {
            SupplierResponseDTO supplier = supplierService.getSupplierById(supplierId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", supplier);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get suppliers by status
     * GET /api/suppliers/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getSuppliersByStatus(@PathVariable String status) {
        List<SupplierResponseDTO> suppliers = supplierService.getSuppliersByStatus(status.toUpperCase());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status.toUpperCase());
        response.put("count", suppliers.size());
        response.put("data", suppliers);

        return ResponseEntity.ok(response);
    }

    /**
     * Update supplier rating
     * PUT /api/suppliers/{supplierId}/rating
     */
    @PutMapping("/{supplierId}/rating")
    public ResponseEntity<Map<String, Object>> updateSupplierRating(
            @PathVariable Long supplierId,
            @RequestParam Double rating) {
        try {
            SupplierResponseDTO response = supplierService.updateSupplierRating(supplierId, rating);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Supplier rating updated successfully.");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Delete supplier
     * DELETE /api/suppliers/{supplierId}
     */
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Map<String, Object>> deleteSupplier(@PathVariable Long supplierId) {
        try {
            supplierService.deleteSupplier(supplierId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Supplier deleted successfully.");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}