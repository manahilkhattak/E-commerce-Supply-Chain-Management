package com.ecommerce.supplychain.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class InventoryCalculator {

    /**
     * Calculate Economic Order Quantity (EOQ)
     * EOQ = √((2 * D * S) / H)
     * Where: D = Annual demand, S = Ordering cost, H = Holding cost per unit
     */
    public static double calculateEOQ(double annualDemand, double orderingCost, double holdingCostPerUnit) {
        if (annualDemand <= 0 || orderingCost <= 0 || holdingCostPerUnit <= 0) {
            return 0;
        }
        return Math.sqrt((2 * annualDemand * orderingCost) / holdingCostPerUnit);
    }

    /**
     * Calculate Reorder Point
     * ROP = (Lead Time Demand) + Safety Stock
     */
    public static double calculateReorderPoint(double leadTimeDemand, double safetyStock) {
        return leadTimeDemand + safetyStock;
    }

    /**
     * Calculate Safety Stock
     * Safety Stock = (Z-score * σ * √Lead Time)
     */
    public static double calculateSafetyStock(double zScore, double demandStdDev, double leadTime) {
        return zScore * demandStdDev * Math.sqrt(leadTime);
    }

    /**
     * Calculate Inventory Turnover Ratio
     * Turnover = Cost of Goods Sold / Average Inventory
     */
    public static double calculateInventoryTurnover(double costOfGoodsSold, double averageInventory) {
        if (averageInventory == 0) return 0;
        return costOfGoodsSold / averageInventory;
    }

    /**
     * Calculate Days of Inventory
     * Days = 365 / Inventory Turnover
     */
    public static double calculateDaysOfInventory(double inventoryTurnover) {
        if (inventoryTurnover == 0) return 0;
        return 365 / inventoryTurnover;
    }

    /**
     * Calculate Stockout Probability
     */
    public static double calculateStockoutProbability(double currentStock, double averageDemand, double demandStdDev) {
        if (demandStdDev == 0) return 0;
        double z = (currentStock - averageDemand) / demandStdDev;
        // Using standard normal distribution approximation
        return 1 - (1 / (1 + Math.exp(-1.7 * z)));
    }

    /**
     * Calculate Inventory Carrying Cost
     * Carrying Cost = Average Inventory * Carrying Cost Percentage * Unit Cost
     */
    public static double calculateCarryingCost(double averageInventory, double carryingCostPercentage, double unitCost) {
        return averageInventory * carryingCostPercentage * unitCost;
    }

    /**
     * Calculate Service Level based on available stock
     */
    public static double calculateServiceLevel(int availableStock, int demand, int leadTimeDemand) {
        if (demand == 0) return 100.0;
        double serviceLevel = ((double) availableStock / demand) * 100;
        return Math.min(serviceLevel, 100.0);
    }

    /**
     * Calculate ABC Classification
     * A: Top 80% of value, B: Next 15%, C: Last 5%
     */
    public static String calculateABCClassification(double itemValue, double totalValue, double cumulativePercentage) {
        if (cumulativePercentage <= 80) return "A";
        if (cumulativePercentage <= 95) return "B";
        return "C";
    }

    /**
     * Calculate optimal batch size for production/purchasing
     */
    public static int calculateOptimalBatchSize(int annualDemand, int setupCost, double holdingCost) {
        if (holdingCost == 0) return 0;
        return (int) Math.sqrt((2 * annualDemand * setupCost) / holdingCost);
    }

    /**
     * Calculate inventory value
     */
    public static double calculateInventoryValue(int quantity, double unitCost) {
        return quantity * unitCost;
    }

    /**
     * Calculate total inventory value from list of items
     */
    public static double calculateTotalInventoryValue(List<InventoryItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitCost())
                .sum();
    }

    /**
     * Helper class for inventory calculations
     */
    public static class InventoryItem {
        private int quantity;
        private double unitCost;

        public InventoryItem(int quantity, double unitCost) {
            this.quantity = quantity;
            this.unitCost = unitCost;
        }

        public int getQuantity() { return quantity; }
        public double getUnitCost() { return unitCost; }
    }

    /**
     * Calculate fill rate based on fulfilled orders
     */
    public static double calculateFillRate(int fulfilledOrders, int totalOrders) {
        if (totalOrders == 0) return 100.0;
        return ((double) fulfilledOrders / totalOrders) * 100;
    }

    /**
     * Calculate stock coverage in days
     */
    public static double calculateStockCoverageDays(int currentStock, double averageDailyDemand) {
        if (averageDailyDemand == 0) return 0;
        return currentStock / averageDailyDemand;
    }
}