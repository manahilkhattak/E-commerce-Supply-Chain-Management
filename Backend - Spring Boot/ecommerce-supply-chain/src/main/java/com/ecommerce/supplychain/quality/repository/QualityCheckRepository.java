package com.ecommerce.supplychain.quality.repository;

import com.ecommerce.supplychain.quality.model.QualityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QualityCheck entity.
 */
@Repository
public interface QualityCheckRepository extends JpaRepository<QualityCheck, Long> {

    Optional<QualityCheck> findByCheckNumber(String checkNumber);

    List<QualityCheck> findByPackageId(Long packageId);

    List<QualityCheck> findByOrderId(Long orderId);

    List<QualityCheck> findByWarehouseId(Long warehouseId);

    List<QualityCheck> findByCheckStatus(String checkStatus);

    List<QualityCheck> findByOverallResult(String overallResult);

    List<QualityCheck> findByInspectorName(String inspectorName);

    List<QualityCheck> findByApprovedForShipment(Boolean approvedForShipment);

    boolean existsByCheckNumber(String checkNumber);

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.createdAt BETWEEN :startDate AND :endDate")
    List<QualityCheck> findQualityChecksByDateRange(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.scorePercentage >= :minScore")
    List<QualityCheck> findHighQualityChecks(@Param("minScore") Double minScore);

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.recheckRequired = true")
    List<QualityCheck> findChecksRequiringRecheck();

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.approvedForShipment = true AND qc.completedAt IS NOT NULL")
    List<QualityCheck> findApprovedChecks();

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.packageId = :packageId AND qc.checkStatus = 'PASSED'")
    List<QualityCheck> findPassedChecksByPackage(@Param("packageId") Long packageId);

    @Query("SELECT qc FROM QualityCheck qc WHERE qc.checkType = :checkType AND qc.overallResult = 'PASS'")
    List<QualityCheck> findPassedChecksByType(@Param("checkType") String checkType);
}