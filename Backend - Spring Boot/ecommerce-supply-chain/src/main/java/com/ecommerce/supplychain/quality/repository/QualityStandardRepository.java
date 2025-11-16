package com.ecommerce.supplychain.quality.repository;

import com.ecommerce.supplychain.quality.model.QualityStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QualityStandard entity.
 */
@Repository
public interface QualityStandardRepository extends JpaRepository<QualityStandard, Long> {

    Optional<QualityStandard> findByStandardCode(String standardCode);

    List<QualityStandard> findByCheckType(String checkType);

    List<QualityStandard> findByProductCategory(String productCategory);

    List<QualityStandard> findByIsActive(Boolean isActive);

    boolean existsByStandardCode(String standardCode);

    @Query("SELECT qs FROM QualityStandard qs WHERE qs.checkType = :checkType AND qs.isActive = true")
    List<QualityStandard> findActiveStandardsByType(@Param("checkType") String checkType);

    @Query("SELECT qs FROM QualityStandard qs WHERE qs.productCategory = :category AND qs.isActive = true")
    List<QualityStandard> findActiveStandardsByCategory(@Param("category") String category);

    @Query("SELECT qs FROM QualityStandard qs WHERE qs.isActive = true")
    List<QualityStandard> findAllActiveStandards();

    @Query("SELECT qs FROM QualityStandard qs WHERE qs.checkType = :checkType AND qs.productCategory = :category AND qs.isActive = true")
    List<QualityStandard> findStandardsByTypeAndCategory(@Param("checkType") String checkType,
                                                         @Param("category") String category);
}