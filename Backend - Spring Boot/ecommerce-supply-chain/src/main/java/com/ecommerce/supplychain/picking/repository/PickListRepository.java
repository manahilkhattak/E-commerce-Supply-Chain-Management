package com.ecommerce.supplychain.picking.repository;

import com.ecommerce.supplychain.picking.model.PickList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PickList entity.
 */
@Repository
public interface PickListRepository extends JpaRepository<PickList, Long> {

    Optional<PickList> findByPickListNumber(String pickListNumber);

    List<PickList> findByOrderId(Long orderId);

    List<PickList> findByWarehouseId(Long warehouseId);

    List<PickList> findByPickStatus(String pickStatus);

    List<PickList> findByAssignedTo(String assignedTo);

    List<PickList> findByPriorityLevel(String priorityLevel);

    boolean existsByPickListNumber(String pickListNumber);

    @Query("SELECT pl FROM PickList pl WHERE pl.createdAt BETWEEN :startDate AND :endDate")
    List<PickList> findPickListsByDateRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT pl FROM PickList pl WHERE pl.pickStatus IN ('PENDING', 'IN_PROGRESS')")
    List<PickList> findActivePickLists();

    @Query("SELECT pl FROM PickList pl WHERE pl.warehouseId = :warehouseId AND pl.pickStatus = 'PENDING'")
    List<PickList> findPendingPickListsByWarehouse(@Param("warehouseId") Long warehouseId);

    @Query("SELECT pl FROM PickList pl WHERE pl.pickStatus = 'COMPLETED' AND pl.completedAt BETWEEN :startDate AND :endDate")
    List<PickList> findCompletedPickListsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);
}