package com.ecommerce.supplychain.reconciliation.repository;

import com.ecommerce.supplychain.reconciliation.model.ReconciliationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReconciliationRepository extends JpaRepository<ReconciliationReport, Long> {

    Optional<ReconciliationReport> findByReportNumber(String reportNumber);

    List<ReconciliationReport> findByWarehouseId(Long warehouseId);

    List<ReconciliationReport> findByReportType(String reportType);

    List<ReconciliationReport> findByReportStatus(String reportStatus);

    List<ReconciliationReport> findByApprovalStatus(String approvalStatus);

    List<ReconciliationReport> findByConductedBy(String conductedBy);

    @Query("SELECT rr FROM ReconciliationReport rr WHERE rr.conductedDate BETWEEN :startDate AND :endDate")
    List<ReconciliationReport> findReportsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT rr FROM ReconciliationReport rr WHERE rr.reportStatus IN ('IN_PROGRESS', 'UNDER_REVIEW')")
    List<ReconciliationReport> findActiveReports();

    @Query("SELECT rr FROM ReconciliationReport rr WHERE rr.accuracyRate < :threshold")
    List<ReconciliationReport> findLowAccuracyReports(@Param("threshold") Double threshold);

    @Query("SELECT rr FROM ReconciliationReport rr WHERE rr.varianceRate > :threshold")
    List<ReconciliationReport> findHighVarianceReports(@Param("threshold") Double threshold);

    @Query("SELECT COUNT(rr) FROM ReconciliationReport rr WHERE rr.reportStatus = 'COMPLETED' AND rr.approvalStatus = 'PENDING'")
    Long countPendingApprovalReports();

    @Query("SELECT rr FROM ReconciliationReport rr WHERE rr.warehouseId = :warehouseId AND rr.reportType = 'CYCLE_COUNT' ORDER BY rr.conductedDate DESC LIMIT 1")
    Optional<ReconciliationReport> findLatestCycleCountByWarehouse(@Param("warehouseId") Long warehouseId);

    @Query("SELECT AVG(rr.accuracyRate) FROM ReconciliationReport rr WHERE rr.reportStatus = 'APPROVED' AND rr.warehouseId = :warehouseId")
    Double findAverageAccuracyByWarehouse(@Param("warehouseId") Long warehouseId);
}