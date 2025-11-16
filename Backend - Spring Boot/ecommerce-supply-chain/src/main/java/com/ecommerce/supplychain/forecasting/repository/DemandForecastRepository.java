package com.ecommerce.supplychain.forecasting.repository;

import com.ecommerce.supplychain.forecasting.model.DemandForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DemandForecast entity.
 */
@Repository
public interface DemandForecastRepository extends JpaRepository<DemandForecast, Long> {

    List<DemandForecast> findByProductId(Long productId);

    List<DemandForecast> findByForecastPeriod(String forecastPeriod);

    List<DemandForecast> findByForecastStatus(String forecastStatus);

    List<DemandForecast> findByForecastMethod(String forecastMethod);

    Optional<DemandForecast> findByProductIdAndForecastPeriodAndStartDate(
            Long productId, String forecastPeriod, LocalDate startDate);

    @Query("SELECT df FROM DemandForecast df WHERE df.startDate BETWEEN :startDate AND :endDate")
    List<DemandForecast> findForecastsByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT df FROM DemandForecast df WHERE df.actualDemand IS NOT NULL AND df.predictedDemand IS NOT NULL")
    List<DemandForecast> findForecastsWithActualDemand();

    @Query("SELECT df FROM DemandForecast df WHERE df.confidenceLevel >= :minConfidence")
    List<DemandForecast> findHighConfidenceForecasts(@Param("minConfidence") Double minConfidence);

    @Query("SELECT df FROM DemandForecast df WHERE df.productId = :productId AND df.forecastStatus = 'ACTIVE'")
    List<DemandForecast> findActiveForecastsByProduct(@Param("productId") Long productId);

    @Query("SELECT df FROM DemandForecast df WHERE df.forecastDate >= :date")
    List<DemandForecast> findRecentForecasts(@Param("date") LocalDate date);
}