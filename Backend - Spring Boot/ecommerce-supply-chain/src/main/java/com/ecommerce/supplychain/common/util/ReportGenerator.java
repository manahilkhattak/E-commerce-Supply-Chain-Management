package com.ecommerce.supplychain.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ReportGenerator {

    /**
     * Generate inventory summary report
     */
    public static String generateInventorySummary(Map<String, Object> inventoryData) {
        StringBuilder report = new StringBuilder();

        report.append("INVENTORY SUMMARY REPORT\n");
        report.append("Generated: ").append(DateUtil.getCurrentTimestamp()).append("\n");
        report.append("=" .repeat(50)).append("\n\n");

        // Add inventory statistics
        if (inventoryData.containsKey("totalProducts")) {
            report.append("Total Products: ").append(inventoryData.get("totalProducts")).append("\n");
        }
        if (inventoryData.containsKey("totalValue")) {
            report.append("Total Inventory Value: $").append(inventoryData.get("totalValue")).append("\n");
        }
        if (inventoryData.containsKey("outOfStock")) {
            report.append("Out of Stock Items: ").append(inventoryData.get("outOfStock")).append("\n");
        }
        if (inventoryData.containsKey("lowStock")) {
            report.append("Low Stock Items: ").append(inventoryData.get("lowStock")).append("\n");
        }

        report.append("\nRECOMMENDATIONS:\n");
        report.append("- Review low stock items for reordering\n");
        report.append("- Analyze slow-moving inventory\n");
        report.append("- Optimize storage for high-value items\n");

        return report.toString();
    }

    /**
     * Generate supply chain performance report
     */
    public static String generateSupplyChainPerformance(Map<String, Object> performanceData) {
        StringBuilder report = new StringBuilder();

        report.append("SUPPLY CHAIN PERFORMANCE REPORT\n");
        report.append("Generated: ").append(DateUtil.getCurrentTimestamp()).append("\n");
        report.append("=" .repeat(60)).append("\n\n");

        // Add performance metrics
        if (performanceData.containsKey("onTimeDelivery")) {
            double onTime = (Double) performanceData.get("onTimeDelivery");
            report.append(String.format("On-Time Delivery Rate: %.2f%%\n", onTime));
        }
        if (performanceData.containsKey("orderAccuracy")) {
            double accuracy = (Double) performanceData.get("orderAccuracy");
            report.append(String.format("Order Accuracy: %.2f%%\n", accuracy));
        }
        if (performanceData.containsKey("inventoryTurnover")) {
            double turnover = (Double) performanceData.get("inventoryTurnover");
            report.append(String.format("Inventory Turnover: %.2f\n", turnover));
        }
        if (performanceData.containsKey("returnRate")) {
            double returnRate = (Double) performanceData.get("returnRate");
            report.append(String.format("Return Rate: %.2f%%\n", returnRate));
        }

        report.append("\nPERFORMANCE ANALYSIS:\n");
        report.append("- Monitor delivery performance trends\n");
        report.append("- Improve inventory management practices\n");
        report.append("- Address root causes of returns\n");

        return report.toString();
    }

    /**
     * Generate discrepancy analysis report
     */
    public static String generateDiscrepancyAnalysis(List<Map<String, Object>> discrepancies) {
        StringBuilder report = new StringBuilder();

        report.append("INVENTORY DISCREPANCY ANALYSIS REPORT\n");
        report.append("Generated: ").append(DateUtil.getCurrentTimestamp()).append("\n");
        report.append("=" .repeat(65)).append("\n\n");

        if (discrepancies.isEmpty()) {
            report.append("No discrepancies found.\n");
            return report.toString();
        }

        report.append(String.format("%-20s %-15s %-15s %-15s %-10s\n",
                "Product", "Expected", "Actual", "Variance", "Value"));
        report.append("-".repeat(75)).append("\n");

        double totalValue = 0;
        for (Map<String, Object> discrepancy : discrepancies) {
            String productName = (String) discrepancy.get("productName");
            Integer expected = (Integer) discrepancy.get("expectedQuantity");
            Integer actual = (Integer) discrepancy.get("actualQuantity");
            Double value = (Double) discrepancy.get("varianceValue");

            report.append(String.format("%-20s %-15d %-15d %-15d $%-9.2f\n",
                    productName, expected, actual, actual - expected, value));

            totalValue += value != null ? value : 0;
        }

        report.append("\n").append("-".repeat(75)).append("\n");
        report.append(String.format("Total Discrepancy Value: $%.2f\n", totalValue));
        report.append("Total Items with Discrepancies: ").append(discrepancies.size()).append("\n");

        return report.toString();
    }

    /**
     * Generate CSV format for data export
     */
    public static String generateCSV(List<Map<String, Object>> data, String[] headers) {
        if (data.isEmpty()) return "";

        StringBuilder csv = new StringBuilder();

        // Add headers
        csv.append(String.join(",", headers)).append("\n");

        // Add data rows
        for (Map<String, Object> row : data) {
            for (int i = 0; i < headers.length; i++) {
                if (i > 0) csv.append(",");
                Object value = row.get(headers[i]);
                if (value != null) {
                    // Escape commas and quotes in CSV
                    String stringValue = value.toString().replace("\"", "\"\"");
                    if (stringValue.contains(",") || stringValue.contains("\"")) {
                        csv.append("\"").append(stringValue).append("\"");
                    } else {
                        csv.append(stringValue);
                    }
                }
            }
            csv.append("\n");
        }

        return csv.toString();
    }
}