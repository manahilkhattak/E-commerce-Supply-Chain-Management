package com.ecommerce.supplychain.contract.repository;

import com.ecommerce.supplychain.contract.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Contract entity.
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByContractNumber(String contractNumber);

    List<Contract> findBySupplierId(Long supplierId);

    List<Contract> findByStatus(String status);

    boolean existsByContractNumber(String contractNumber);

    List<Contract> findByEndDateBefore(LocalDate date);

    @Query("SELECT c FROM Contract c WHERE c.endDate BETWEEN :startDate AND :endDate")
    List<Contract> findContractsExpiringBetween(LocalDate startDate, LocalDate endDate);

    List<Contract> findByContractType(String contractType);
}