package com.ecommerce.supplychain.forecasting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing demand forecasts for products.
 * Uses historical data and trends to predict future demand.
 */
@Entity
@Table(name = "demand_forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forecast_id")
    private Long forecastId;

    @Column(name = "product_id", nullable = false)
    private Long productId; // Reference to Product from Process 5

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "forecast_period", nullable = false, length = 20)
    private String forecastPeriod; // WEEKLY, MONTHLY, QUARTERLY

    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "predicted_demand", nullable = false)
    private Integer predictedDemand;

    @Column(name = "confidence_level", precision = 5, scale = 2)
    private BigDecimal confidenceLevel; // 0.00 to 1.00 (0% to 100%)

    @Column(name = "historical_accuracy", precision = 5, scale = 2)
    private BigDecimal historicalAccuracy; // Previous forecast accuracy

    @Column(name = "seasonality_factor", precision = 5, scale = 2)
    private BigDecimal seasonalityFactor;

    @Column(name = "trend_factor", precision = 5, scale = 2)
    private BigDecimal trendFactor;

    @Column(name = "promotion_impact", precision = 5, scale = 2)
    private BigDecimal promotionImpact;

    @Column(name = "base_demand", nullable = false)
    private Integer baseDemand;

    @Column(name = "adjusted_demand")
    private Integer adjustedDemand;

    @Column(name = "forecast_method", length = 50)
    private String forecastMethod; // MOVING_AVERAGE, EXPONENTIAL_SMOOTHING, ARIMA, MACHINE_LEARNING

    @Column(name = "forecast_status", length = 50)
    private String forecastStatus; // DRAFT, ACTIVE, ARCHIVED, SUPERSEDED

    @Column(name = "actual_demand")
    private Integer actualDemand; // Filled after period ends

    @Column(name = "forecast_error")
    private Integer forecastError; // Difference between predicted and actual

    @Column(name = "mean_absolute_error", precision = 10, scale = 2)
    private BigDecimal meanAbsoluteError;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (forecastStatus == null) {
            forecastStatus = "DRAFT";
        }
        if (confidenceLevel == null) {
            confidenceLevel = new BigDecimal("0.80");
        }
        if (historicalAccuracy == null) {
            historicalAccuracy = new BigDecimal("0.85");
        }
        if (seasonalityFactor == null) {
            seasonalityFactor = new BigDecimal("1.00");
        }
        if (trendFactor == null) {
            trendFactor = new BigDecimal("1.00");
        }
        if (promotionImpact == null) {
            promotionImpact = new BigDecimal("1.00");
        }
        if (forecastMethod == null) {
            forecastMethod = "MOVING_AVERAGE";
        }

        // Calculate adjusted demand if not provided
        if (adjustedDemand == null && baseDemand != null) {
            calculateAdjustedDemand();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Recalculate adjusted demand if factors changed
        if (baseDemand != null) {
            calculateAdjustedDemand();
        }

        // Calculate forecast error if actual demand is available
        if (actualDemand != null && predictedDemand != null) {
            forecastError = actualDemand - predictedDemand;
        }
    }

    /**
     * Calculate adjusted demand based on various factors
     */
    public void calculateAdjustedDemand() {
        BigDecimal adjustment = seasonalityFactor
                .multiply(trendFactor)
                .multiply(promotionImpact);

        double adjustedValue = baseDemand * adjustment.doubleValue();
        this.adjustedDemand = (int) Math.round(adjustedValue);
        this.predictedDemand = this.adjustedDemand;
    }

    /**
     * Update with actual demand and calculate accuracy
     */
    public void updateWithActualDemand(Integer actual) {
        this.actualDemand = actual;
        if (this.predictedDemand != null) {
            this.forecastError = actual - this.predictedDemand;
            this.meanAbsoluteError = new BigDecimal(Math.abs(this.forecastError));
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate forecast accuracy percentage
     */
    public BigDecimal calculateAccuracy() {
        if (actualDemand == null || actualDemand == 0) {
            return BigDecimal.ZERO;
        }
        double errorPercentage = (double) Math.abs(forecastError) / actualDemand;
        return new BigDecimal(1 - errorPercentage).max(BigDecimal.ZERO).min(new BigDecimal("1.00"));
    }
}