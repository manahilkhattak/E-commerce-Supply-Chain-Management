package com.ecommerce.supplychain.contract.repository;

import com.ecommerce.supplychain.contract.model.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SLA entity.
 */
@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {

    List<SLA> findByContractId(Long contractId);

    List<SLA> findByStatus(String status);

    List<SLA> findByContractIdAndStatus(Long contractId, String status);

    List<SLA> findByCompliancePercentageLessThan(Double percentage);
}