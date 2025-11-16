package com.ecommerce.supplychain.contract.controller;

import com.ecommerce.supplychain.contract.dto.ContractDTO;
import com.ecommerce.supplychain.contract.dto.ContractResponseDTO;
import com.ecommerce.supplychain.contract.dto.SLACreationDTO;
import com.ecommerce.supplychain.contract.dto.SLAResponseDTO;
import com.ecommerce.supplychain.contract.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Contract and SLA Management APIs.
 */
@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * API 1: Create a new contract
     * POST /api/contracts
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createContract(@Valid @RequestBody ContractDTO contractDTO) {
        try {
            ContractResponseDTO response = contractService.createContract(contractDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Contract created successfully");
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
     * API 2: Add SLA to a contract
     * POST /api/contracts/sla
     */
    @PostMapping("/sla")
    public ResponseEntity<Map<String, Object>> addSLA(@Valid @RequestBody SLACreationDTO slaDTO) {
        try {
            SLAResponseDTO response = contractService.addSLAToContract(slaDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "SLA added to contract successfully");
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
     * Get all contracts
     * GET /api/contracts
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllContracts() {
        List<ContractResponseDTO> contracts = contractService.getAllContracts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", contracts.size());
        response.put("data", contracts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get contract by ID
     * GET /api/contracts/{contractId}
     */
    @GetMapping("/{contractId}")
    public ResponseEntity<Map<String, Object>> getContractById(@PathVariable Long contractId) {
        try {
            ContractResponseDTO contract = contractService.getContractById(contractId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", contract);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get contracts by supplier ID
     * GET /api/contracts/supplier/{supplierId}
     */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<Map<String, Object>> getContractsBySupplierId(@PathVariable Long supplierId) {
        List<ContractResponseDTO> contracts = contractService.getContractsBySupplierId(supplierId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("supplierId", supplierId);
        response.put("count", contracts.size());
        response.put("data", contracts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get contracts by status
     * GET /api/contracts/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getContractsByStatus(@PathVariable String status) {
        List<ContractResponseDTO> contracts = contractService.getContractsByStatus(status.toUpperCase());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", status.toUpperCase());
        response.put("count", contracts.size());
        response.put("data", contracts);

        return ResponseEntity.ok(response);
    }

    /**
     * Get SLAs for a contract
     * GET /api/contracts/{contractId}/slas
     */
    @GetMapping("/{contractId}/slas")
    public ResponseEntity<Map<String, Object>> getSLAsByContractId(@PathVariable Long contractId) {
        List<SLAResponseDTO> slas = contractService.getSLAsByContractId(contractId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("contractId", contractId);
        response.put("count", slas.size());
        response.put("data", slas);

        return ResponseEntity.ok(response);
    }

    /**
     * Activate contract
     * PUT /api/contracts/{contractId}/activate
     */
    @PutMapping("/{contractId}/activate")
    public ResponseEntity<Map<String, Object>> activateContract(@PathVariable Long contractId) {
        try {
            ContractResponseDTO response = contractService.activateContract(contractId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Contract activated successfully");
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
     * Get expiring contracts
     * GET /api/contracts/expiring
     */
    @GetMapping("/expiring")
    public ResponseEntity<Map<String, Object>> getExpiringContracts() {
        List<ContractResponseDTO> contracts = contractService.getExpiringContracts();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Contracts expiring within 30 days");
        response.put("count", contracts.size());
        response.put("data", contracts);

        return ResponseEntity.ok(response);
    }

    /**
     * Update SLA compliance
     * PUT /api/contracts/sla/{slaId}/compliance
     */
    @PutMapping("/sla/{slaId}/compliance")
    public ResponseEntity<Map<String, Object>> updateSLACompliance(
            @PathVariable Long slaId,
            @RequestParam Double compliancePercentage) {
        try {
            SLAResponseDTO response = contractService.updateSLACompliance(slaId, compliancePercentage);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "SLA compliance updated successfully");
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
     * Delete contract (soft delete - changes status to TERMINATED)
     * DELETE /api/contracts/{contractId}
     */
    @DeleteMapping("/{contractId}")
    public ResponseEntity<Map<String, Object>> deleteContract(@PathVariable Long contractId) {
        try {
            contractService.deleteContract(contractId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Contract terminated successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}