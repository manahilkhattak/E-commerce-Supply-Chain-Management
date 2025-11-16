package com.ecommerce.supplychain.contract.service;

import com.ecommerce.supplychain.contract.dto.ContractDTO;
import com.ecommerce.supplychain.contract.dto.ContractResponseDTO;
import com.ecommerce.supplychain.contract.dto.SLACreationDTO;
import com.ecommerce.supplychain.contract.dto.SLAResponseDTO;
import com.ecommerce.supplychain.contract.model.Contract;
import com.ecommerce.supplychain.contract.model.SLA;
import com.ecommerce.supplychain.contract.repository.ContractRepository;
import com.ecommerce.supplychain.contract.repository.SLARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling contract and SLA management business logic.
 */
@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private SLARepository slaRepository;

    /**
     * Create a new contract.
     */
    @Transactional
    public ContractResponseDTO createContract(ContractDTO contractDTO) {
        // Check if contract number already exists
        if (contractRepository.existsByContractNumber(contractDTO.getContractNumber())) {
            throw new IllegalArgumentException("Contract with number " + contractDTO.getContractNumber() + " already exists");
        }

        // Validate dates
        if (contractDTO.getEndDate().isBefore(contractDTO.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Create contract entity
        Contract contract = new Contract();
        contract.setContractNumber(contractDTO.getContractNumber());
        contract.setSupplierId(contractDTO.getSupplierId());
        contract.setContractTitle(contractDTO.getContractTitle());
        contract.setContractType(contractDTO.getContractType());
        contract.setStartDate(contractDTO.getStartDate());
        contract.setEndDate(contractDTO.getEndDate());
        contract.setContractValue(contractDTO.getContractValue());
        contract.setCurrency(contractDTO.getCurrency());
        contract.setPaymentTerms(contractDTO.getPaymentTerms());
        contract.setRenewalTerms(contractDTO.getRenewalTerms());
        contract.setNotes(contractDTO.getNotes());
        contract.setStatus("DRAFT");
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());

        Contract savedContract = contractRepository.save(contract);

        return mapToResponseDTO(savedContract);
    }

    /**
     * Add SLA to a contract.
     */
    @Transactional
    public SLAResponseDTO addSLAToContract(SLACreationDTO slaDTO) {
        // Verify contract exists
        Contract contract = contractRepository.findById(slaDTO.getContractId())
                .orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + slaDTO.getContractId()));

        // Create SLA entity
        SLA sla = new SLA();
        sla.setContractId(slaDTO.getContractId());
        sla.setMetricName(slaDTO.getMetricName());
        sla.setMetricDescription(slaDTO.getMetricDescription());
        sla.setTargetValue(slaDTO.getTargetValue());
        sla.setMeasurementUnit(slaDTO.getMeasurementUnit());
        sla.setMinimumAcceptable(slaDTO.getMinimumAcceptable());
        sla.setPenaltyClause(slaDTO.getPenaltyClause());
        sla.setMonitoringFrequency(slaDTO.getMonitoringFrequency());
        sla.setStatus("ACTIVE");
        sla.setCompliancePercentage(0.0);
        sla.setCreatedAt(LocalDateTime.now());
        sla.setUpdatedAt(LocalDateTime.now());

        SLA savedSLA = slaRepository.save(sla);

        return mapToSLAResponseDTO(savedSLA);
    }

    /**
     * Get all contracts.
     */
    public List<ContractResponseDTO> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get contract by ID.
     */
    public ContractResponseDTO getContractById(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + contractId));
        return mapToResponseDTO(contract);
    }

    /**
     * Get contracts by supplier.
     */
    public List<ContractResponseDTO> getContractsBySupplierId(Long supplierId) {
        return contractRepository.findBySupplierId(supplierId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get contracts by status.
     */
    public List<ContractResponseDTO> getContractsByStatus(String status) {
        return contractRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get SLAs for a contract.
     */
    public List<SLAResponseDTO> getSLAsByContractId(Long contractId) {
        return slaRepository.findByContractId(contractId).stream()
                .map(this::mapToSLAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Activate a contract (change from DRAFT to ACTIVE).
     */
    @Transactional
    public ContractResponseDTO activateContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + contractId));

        if (!"DRAFT".equals(contract.getStatus())) {
            throw new IllegalStateException("Only DRAFT contracts can be activated");
        }

        contract.setStatus("ACTIVE");
        contract.setSignedDate(LocalDate.now());
        contract.setUpdatedAt(LocalDateTime.now());

        Contract updatedContract = contractRepository.save(contract);

        return mapToResponseDTO(updatedContract);
    }

    /**
     * Get expiring contracts (within next 30 days).
     */
    public List<ContractResponseDTO> getExpiringContracts() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);

        return contractRepository.findContractsExpiringBetween(today, thirtyDaysFromNow).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update SLA compliance percentage.
     */
    @Transactional
    public SLAResponseDTO updateSLACompliance(Long slaId, Double compliancePercentage) {
        SLA sla = slaRepository.findById(slaId)
                .orElseThrow(() -> new IllegalArgumentException("SLA not found with ID: " + slaId));

        if (compliancePercentage < 0.0 || compliancePercentage > 100.0) {
            throw new IllegalArgumentException("Compliance percentage must be between 0 and 100");
        }

        sla.setCompliancePercentage(compliancePercentage);

        // Update status based on compliance
        if (compliancePercentage >= 95.0) {
            sla.setStatus("MET");
        } else if (compliancePercentage < 70.0) {
            sla.setStatus("VIOLATED");
        } else {
            sla.setStatus("ACTIVE");
        }

        sla.setUpdatedAt(LocalDateTime.now());

        SLA updatedSLA = slaRepository.save(sla);

        return mapToSLAResponseDTO(updatedSLA);
    }

    /**
     * Delete contract.
     */
    @Transactional
    public void deleteContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found with ID: " + contractId));

        contract.setStatus("TERMINATED");
        contract.setUpdatedAt(LocalDateTime.now());
        contractRepository.save(contract);
    }

    /**
     * Helper method to convert Contract to ResponseDTO.
     */
    private ContractResponseDTO mapToResponseDTO(Contract contract) {
        Integer daysUntilExpiry = null;
        if (contract.getEndDate() != null) {
            daysUntilExpiry = (int) ChronoUnit.DAYS.between(LocalDate.now(), contract.getEndDate());
        }

        return ContractResponseDTO.builder()
                .contractId(contract.getContractId())
                .contractNumber(contract.getContractNumber())
                .supplierId(contract.getSupplierId())
                .contractTitle(contract.getContractTitle())
                .contractType(contract.getContractType())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .contractValue(contract.getContractValue())
                .currency(contract.getCurrency())
                .paymentTerms(contract.getPaymentTerms())
                .renewalTerms(contract.getRenewalTerms())
                .status(contract.getStatus())
                .signedDate(contract.getSignedDate())
                .notes(contract.getNotes())
                .createdAt(contract.getCreatedAt())
                .updatedAt(contract.getUpdatedAt())
                .daysUntilExpiry(daysUntilExpiry)
                .build();
    }

    /**
     * Helper method to convert SLA to ResponseDTO.
     */
    private SLAResponseDTO mapToSLAResponseDTO(SLA sla) {
        return SLAResponseDTO.builder()
                .slaId(sla.getSlaId())
                .contractId(sla.getContractId())
                .metricName(sla.getMetricName())
                .metricDescription(sla.getMetricDescription())
                .targetValue(sla.getTargetValue())
                .measurementUnit(sla.getMeasurementUnit())
                .minimumAcceptable(sla.getMinimumAcceptable())
                .penaltyClause(sla.getPenaltyClause())
                .monitoringFrequency(sla.getMonitoringFrequency())
                .status(sla.getStatus())
                .compliancePercentage(sla.getCompliancePercentage())
                .createdAt(sla.getCreatedAt())
                .updatedAt(sla.getUpdatedAt())
                .build();
    }
}