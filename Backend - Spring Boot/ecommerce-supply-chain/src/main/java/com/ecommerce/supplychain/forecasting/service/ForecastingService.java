package com.ecommerce.supplychain.forecasting.service;

import com.ecommerce.supplychain.forecasting.dto.*;
import com.ecommerce.supplychain.forecasting.model.DemandForecast;
import com.ecommerce.supplychain.forecasting.model.ReorderPlan;
import com.ecommerce.supplychain.forecasting.repository.DemandForecastRepository;
import com.ecommerce.supplychain.forecasting.repository.ReorderPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling demand forecasting and reorder planning business logic.
 */
@Service
public class ForecastingService {

    @Autowired
    private DemandForecastRepository demandForecastRepository;

    @Autowired
    private ReorderPlanRepository reorderPlanRepository;

    /**
     * API 1: Generate demand forecast
     */
    @Transactional
    public ForecastResponseDTO generateDemandForecast(ForecastDTO forecastDTO) {
        // Check if forecast already exists for this period
        if (demandForecastRepository.findByProductIdAndForecastPeriodAndStartDate(
                forecastDTO.getProductId(), forecastDTO.getForecastPeriod(), forecastDTO.getStartDate()).isPresent()) {
            throw new IllegalArgumentException("Forecast already exists for this product and period");
        }

        // Create demand forecast entity
        DemandForecast forecast = new DemandForecast();
        forecast.setProductId(forecastDTO.getProductId());
        forecast.setProductName(forecastDTO.getProductName());
        forecast.setProductSku(forecastDTO.getProductSku());
        forecast.setForecastPeriod(forecastDTO.getForecastPeriod());
        forecast.setForecastDate(LocalDate.now());
        forecast.setStartDate(forecastDTO.getStartDate());
        forecast.setEndDate(forecastDTO.getEndDate());
        forecast.setBaseDemand(forecastDTO.getBaseDemand());
        forecast.setConfidenceLevel(forecastDTO.getConfidenceLevel());
        forecast.setSeasonalityFactor(forecastDTO.getSeasonalityFactor());
        forecast.setTrendFactor(forecastDTO.getTrendFactor());
        forecast.setPromotionImpact(forecastDTO.getPromotionImpact());
        forecast.setForecastMethod(forecastDTO.getForecastMethod());
        forecast.setNotes(forecastDTO.getNotes());
        forecast.setCreatedBy(forecastDTO.getCreatedBy());
        forecast.setForecastStatus("ACTIVE");
        forecast.setCreatedAt(LocalDateTime.now());
        forecast.setUpdatedAt(LocalDateTime.now());

        // Calculate adjusted demand automatically
        forecast.calculateAdjustedDemand();

        DemandForecast savedForecast = demandForecastRepository.save(forecast);

        return mapToForecastResponseDTO(savedForecast);
    }

    /**
     * API 2: Generate reorder plan from forecast
     */
    @Transactional
    public ReorderPlanResponseDTO generateReorderPlan(ReorderPlanDTO reorderPlanDTO) {
        // Verify forecast exists
        DemandForecast forecast = demandForecastRepository.findById(reorderPlanDTO.getForecastId())
                .orElseThrow(() -> new IllegalArgumentException("Forecast not found with ID: " + reorderPlanDTO.getForecastId()));

        // Check if reorder plan already exists for this forecast
        if (reorderPlanRepository.findByForecastId(reorderPlanDTO.getForecastId()).isPresent()) {
            throw new IllegalArgumentException("Reorder plan already exists for this forecast");
        }

        // Create reorder plan entity
        ReorderPlan reorderPlan = new ReorderPlan();
        reorderPlan.setProductId(reorderPlanDTO.getProductId());
        reorderPlan.setProductName(forecast.getProductName());
        reorderPlan.setProductSku(forecast.getProductSku());
        reorderPlan.setForecastId(reorderPlanDTO.getForecastId());
        reorderPlan.setCurrentStock(reorderPlanDTO.getCurrentStock());
        reorderPlan.setSafetyStock(reorderPlanDTO.getSafetyStock());
        reorderPlan.setLeadTimeDays(reorderPlanDTO.getLeadTimeDays());
        reorderPlan.setDailyDemandRate(reorderPlanDTO.getDailyDemandRate());
        reorderPlan.setReorderPoint(reorderPlanDTO.getReorderPoint());
        reorderPlan.setEstimatedCost(reorderPlanDTO.getEstimatedCost());
        reorderPlan.setSupplierId(reorderPlanDTO.getSupplierId());
        reorderPlan.setSupplierName(reorderPlanDTO.getSupplierName());
        reorderPlan.setServiceLevelTarget(reorderPlanDTO.getServiceLevelTarget());
        reorderPlan.setNotes(reorderPlanDTO.getNotes());
        reorderPlan.setCreatedBy(reorderPlanDTO.getCreatedBy());
        reorderPlan.setPlanStatus("DRAFT");
        reorderPlan.setConvertedToPo(false);
        reorderPlan.setCreatedAt(LocalDateTime.now());
        reorderPlan.setUpdatedAt(LocalDateTime.now());

        // Calculate recommended order quantity
        calculateRecommendedOrderQuantity(reorderPlan);

        // Calculate expected stockout date
        calculateExpectedStockoutDate(reorderPlan);

        ReorderPlan savedPlan = reorderPlanRepository.save(reorderPlan);

        return mapToReorderPlanResponseDTO(savedPlan);
    }

    /**
     * Convert reorder plan to purchase order
     */
    @Transactional
    public ReorderPlanResponseDTO convertToPurchaseOrder(ConvertToPODTO convertDTO) {
        ReorderPlan reorderPlan = reorderPlanRepository.findById(convertDTO.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("Reorder plan not found with ID: " + convertDTO.getPlanId()));

        if (reorderPlan.getConvertedToPo()) {
            throw new IllegalStateException("Reorder plan is already converted to purchase order");
        }

        reorderPlan.markAsConvertedToPO(convertDTO.getPurchaseOrderId());

        if (convertDTO.getNotes() != null && !convertDTO.getNotes().isEmpty()) {
            String existingNotes = reorderPlan.getNotes() != null ? reorderPlan.getNotes() + "\n\n" : "";
            reorderPlan.setNotes(existingNotes + "PO Conversion: " + convertDTO.getNotes());
        }

        ReorderPlan updatedPlan = reorderPlanRepository.save(reorderPlan);

        return mapToReorderPlanResponseDTO(updatedPlan);
    }

    /**
     * Get all demand forecasts
     */
    public List<ForecastResponseDTO> getAllDemandForecasts() {
        return demandForecastRepository.findAll().stream()
                .map(this::mapToForecastResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get forecast by ID
     */
    public ForecastResponseDTO getForecastById(Long forecastId) {
        DemandForecast forecast = demandForecastRepository.findById(forecastId)
                .orElseThrow(() -> new IllegalArgumentException("Forecast not found with ID: " + forecastId));
        return mapToForecastResponseDTO(forecast);
    }

    /**
     * Get forecasts by product
     */
    public List<ForecastResponseDTO> getForecastsByProductId(Long productId) {
        return demandForecastRepository.findByProductId(productId).stream()
                .map(this::mapToForecastResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active forecasts
     */
    public List<ForecastResponseDTO> getActiveForecasts() {
        return demandForecastRepository.findByForecastStatus("ACTIVE").stream()
                .map(this::mapToForecastResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all reorder plans
     */
    public List<ReorderPlanResponseDTO> getAllReorderPlans() {
        return reorderPlanRepository.findAll().stream()
                .map(this::mapToReorderPlanResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get reorder plan by ID
     */
    public ReorderPlanResponseDTO getReorderPlanById(Long planId) {
        ReorderPlan plan = reorderPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Reorder plan not found with ID: " + planId));
        return mapToReorderPlanResponseDTO(plan);
    }

    /**
     * Get pending reorder plans (not converted to PO)
     */
    public List<ReorderPlanResponseDTO> getPendingReorderPlans() {
        return reorderPlanRepository.findPendingReorderPlans(LocalDate.now()).stream()
                .map(this::mapToReorderPlanResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get critical reorder plans
     */
    public List<ReorderPlanResponseDTO> getCriticalReorderPlans() {
        return reorderPlanRepository.findCriticalReorderPlans().stream()
                .map(this::mapToReorderPlanResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update forecast with actual demand
     */
    @Transactional
    public ForecastResponseDTO updateForecastWithActualDemand(Long forecastId, Integer actualDemand) {
        DemandForecast forecast = demandForecastRepository.findById(forecastId)
                .orElseThrow(() -> new IllegalArgumentException("Forecast not found with ID: " + forecastId));

        forecast.updateWithActualDemand(actualDemand);

        DemandForecast updatedForecast = demandForecastRepository.save(forecast);

        return mapToForecastResponseDTO(updatedForecast);
    }

    /**
     * Run automated forecasting for all monitored products
     */
    @Transactional
    public List<ForecastResponseDTO> runAutomatedForecasting() {
        // In a real implementation, this would:
        // 1. Fetch historical sales data
        // 2. Apply forecasting algorithms
        // 3. Generate forecasts for all products
        // For now, return empty list as placeholder

        return List.of();
    }

    /**
     * Calculate recommended order quantity for reorder plan
     */
    private void calculateRecommendedOrderQuantity(ReorderPlan plan) {
        // Simple calculation: cover lead time demand + safety stock - current stock
        int leadTimeDemand = plan.getDailyDemandRate() * plan.getLeadTimeDays();
        int recommendedQty = leadTimeDemand + plan.getSafetyStock() - plan.getCurrentStock();

        // Ensure minimum order quantity
        plan.setRecommendedOrderQuantity(Math.max(50, recommendedQty));

        // Set EOQ if same as recommended (simplified)
        plan.setEconomicOrderQuantity(plan.getRecommendedOrderQuantity());
    }

    /**
     * Calculate expected stockout date
     */
    private void calculateExpectedStockoutDate(ReorderPlan plan) {
        if (plan.getDailyDemandRate() > 0) {
            int daysUntilStockout = plan.getCurrentStock() / plan.getDailyDemandRate();
            plan.setExpectedStockoutDate(LocalDate.now().plusDays(daysUntilStockout));
        }
    }

    /**
     * Helper method to convert DemandForecast to ResponseDTO
     */
    private ForecastResponseDTO mapToForecastResponseDTO(DemandForecast forecast) {
        BigDecimal accuracyPercentage = forecast.calculateAccuracy();
        boolean isAccurate = accuracyPercentage.compareTo(new BigDecimal("0.80")) >= 0;
        String accuracyLevel = getAccuracyLevel(accuracyPercentage);

        return ForecastResponseDTO.builder()
                .forecastId(forecast.getForecastId())
                .productId(forecast.getProductId())
                .productName(forecast.getProductName())
                .productSku(forecast.getProductSku())
                .forecastPeriod(forecast.getForecastPeriod())
                .forecastDate(forecast.getForecastDate())
                .startDate(forecast.getStartDate())
                .endDate(forecast.getEndDate())
                .predictedDemand(forecast.getPredictedDemand())
                .confidenceLevel(forecast.getConfidenceLevel())
                .historicalAccuracy(forecast.getHistoricalAccuracy())
                .seasonalityFactor(forecast.getSeasonalityFactor())
                .trendFactor(forecast.getTrendFactor())
                .promotionImpact(forecast.getPromotionImpact())
                .baseDemand(forecast.getBaseDemand())
                .adjustedDemand(forecast.getAdjustedDemand())
                .forecastMethod(forecast.getForecastMethod())
                .forecastStatus(forecast.getForecastStatus())
                .actualDemand(forecast.getActualDemand())
                .forecastError(forecast.getForecastError())
                .meanAbsoluteError(forecast.getMeanAbsoluteError())
                .accuracyPercentage(accuracyPercentage)
                .notes(forecast.getNotes())
                .createdBy(forecast.getCreatedBy())
                .createdAt(forecast.getCreatedAt())
                .updatedAt(forecast.getUpdatedAt())
                .isAccurate(isAccurate)
                .accuracyLevel(accuracyLevel)
                .build();
    }

    /**
     * Helper method to convert ReorderPlan to ResponseDTO
     */
    private ReorderPlanResponseDTO mapToReorderPlanResponseDTO(ReorderPlan plan) {
        Integer daysUntilStockout = null;
        if (plan.getExpectedStockoutDate() != null) {
            daysUntilStockout = (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), plan.getExpectedStockoutDate());
        }

        boolean needsImmediateAction = "CRITICAL".equals(plan.getOrderUrgency()) || "HIGH".equals(plan.getOrderUrgency());
        String actionPriority = getActionPriority(plan.getOrderUrgency(), plan.getStockoutRiskLevel());

        return ReorderPlanResponseDTO.builder()
                .planId(plan.getPlanId())
                .productId(plan.getProductId())
                .productName(plan.getProductName())
                .productSku(plan.getProductSku())
                .forecastId(plan.getForecastId())
                .currentStock(plan.getCurrentStock())
                .safetyStock(plan.getSafetyStock())
                .leadTimeDays(plan.getLeadTimeDays())
                .dailyDemandRate(plan.getDailyDemandRate())
                .reorderPoint(plan.getReorderPoint())
                .economicOrderQuantity(plan.getEconomicOrderQuantity())
                .recommendedOrderQuantity(plan.getRecommendedOrderQuantity())
                .orderUrgency(plan.getOrderUrgency())
                .expectedStockoutDate(plan.getExpectedStockoutDate())
                .suggestedOrderDate(plan.getSuggestedOrderDate())
                .expectedDeliveryDate(plan.getExpectedDeliveryDate())
                .estimatedCost(plan.getEstimatedCost())
                .supplierId(plan.getSupplierId())
                .supplierName(plan.getSupplierName())
                .planStatus(plan.getPlanStatus())
                .convertedToPo(plan.getConvertedToPo())
                .purchaseOrderId(plan.getPurchaseOrderId())
                .conversionDate(plan.getConversionDate())
                .stockoutRiskLevel(plan.getStockoutRiskLevel())
                .serviceLevelTarget(plan.getServiceLevelTarget())
                .calculatedServiceLevel(plan.getCalculatedServiceLevel())
                .notes(plan.getNotes())
                .createdBy(plan.getCreatedBy())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .daysUntilStockout(daysUntilStockout)
                .needsImmediateAction(needsImmediateAction)
                .actionPriority(actionPriority)
                .build();
    }

    /**
     * Determine accuracy level based on percentage
     */
    private String getAccuracyLevel(BigDecimal accuracy) {
        if (accuracy == null) return "UNKNOWN";

        if (accuracy.compareTo(new BigDecimal("0.90")) >= 0) return "HIGH";
        if (accuracy.compareTo(new BigDecimal("0.75")) >= 0) return "MEDIUM";
        return "LOW";
    }

    /**
     * Determine action priority based on urgency and risk
     */
    private String getActionPriority(String urgency, String risk) {
        if ("CRITICAL".equals(urgency) || "CRITICAL".equals(risk)) return "HIGHEST";
        if ("HIGH".equals(urgency) || "HIGH".equals(risk)) return "HIGH";
        if ("MEDIUM".equals(urgency) || "MEDIUM".equals(risk)) return "MEDIUM";
        return "LOW";
    }
}