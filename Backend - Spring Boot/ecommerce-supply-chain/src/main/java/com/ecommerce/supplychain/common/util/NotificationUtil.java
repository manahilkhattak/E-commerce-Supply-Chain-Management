package com.ecommerce.supplychain.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationUtil {

    /**
     * Generate low stock alert message
     */
    public static String generateLowStockAlert(String productName, String sku, int currentStock, int reorderLevel) {
        return String.format(
                "LOW STOCK ALERT: Product '%s' (SKU: %s) is below reorder level. " +
                        "Current stock: %d, Reorder level: %d. Please consider reordering.",
                productName, sku, currentStock, reorderLevel
        );
    }

    /**
     * Generate out of stock alert message
     */
    public static String generateOutOfStockAlert(String productName, String sku) {
        return String.format(
                "OUT OF STOCK ALERT: Product '%s' (SKU: %s) is out of stock. " +
                        "Urgent action required to restock.",
                productName, sku
        );
    }

    /**
     * Generate order status update message
     */
    public static String generateOrderStatusUpdate(String orderNumber, String oldStatus, String newStatus) {
        return String.format(
                "Order %s status updated from '%s' to '%s'",
                orderNumber, oldStatus, newStatus
        );
    }

    /**
     * Generate shipment notification message
     */
    public static String generateShipmentNotification(String orderNumber, String trackingNumber, String carrier) {
        return String.format(
                "Order %s has been shipped. Tracking number: %s, Carrier: %s",
                orderNumber, trackingNumber, carrier
        );
    }

    /**
     * Generate delivery exception alert
     */
    public static String generateDeliveryExceptionAlert(String trackingNumber, String exceptionType, String description) {
        return String.format(
                "DELIVERY EXCEPTION: Tracking %s - %s: %s",
                trackingNumber, exceptionType, description
        );
    }

    /**
     * Generate return request notification
     */
    public static String generateReturnRequestNotification(String returnNumber, String orderNumber, String reason) {
        return String.format(
                "RETURN REQUEST: Return %s for Order %s. Reason: %s",
                returnNumber, orderNumber, reason
        );
    }

    /**
     * Generate inventory discrepancy alert
     */
    public static String generateInventoryDiscrepancyAlert(String productName, int expected, int actual, double value) {
        return String.format(
                "INVENTORY DISCREPANCY: Product '%s'. Expected: %d, Actual: %d, Variance: %d, Value Impact: $%.2f",
                productName, expected, actual, actual - expected, value
        );
    }

    /**
     * Generate system health alert
     */
    public static String generateSystemHealthAlert(String component, String issue, String severity) {
        return String.format(
                "SYSTEM HEALTH ALERT [%s]: %s - %s. Immediate attention required.",
                severity, component, issue
        );
    }

    /**
     * Generate performance metric alert
     */
    public static String generatePerformanceAlert(String metric, double currentValue, double threshold, String trend) {
        return String.format(
                "PERFORMANCE ALERT: %s is %s (Current: %.2f, Threshold: %.2f). Review recommended.",
                metric, trend, currentValue, threshold
        );
    }
}