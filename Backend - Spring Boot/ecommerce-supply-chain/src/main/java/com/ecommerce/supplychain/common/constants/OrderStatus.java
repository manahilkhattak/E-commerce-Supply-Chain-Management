package com.ecommerce.supplychain.common.constants;

public final class OrderStatus {

    private OrderStatus() {} // Utility class

    // Order Lifecycle Statuses
    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PROCESSING = "PROCESSING";
    public static final String READY_FOR_PICKING = "READY_FOR_PICKING";
    public static final String PICKING_IN_PROGRESS = "PICKING_IN_PROGRESS";
    public static final String PICKED = "PICKED";
    public static final String PACKED = "PACKED";
    public static final String READY_FOR_SHIPMENT = "READY_FOR_SHIPMENT";
    public static final String SHIPPED = "SHIPPED";
    public static final String IN_TRANSIT = "IN_TRANSIT";
    public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";
    public static final String REFUNDED = "REFUNDED";
    public static final String ON_HOLD = "ON_HOLD";
    public static final String DELIVERY_EXCEPTION = "DELIVERY_EXCEPTION";
    public static final String RETURN_REQUESTED = "RETURN_REQUESTED";
    public static final String RETURNED = "RETURNED";

    // Status groups for business logic
    public static final String[] ACTIVE_STATUSES = {
            PENDING, CONFIRMED, PROCESSING, READY_FOR_PICKING,
            PICKING_IN_PROGRESS, PICKED, PACKED, READY_FOR_SHIPMENT,
            SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY
    };

    public static final String[] COMPLETED_STATUSES = {
            DELIVERED, CANCELLED, REFUNDED, RETURNED
    };

    public static final String[] PROBLEM_STATUSES = {
            DELIVERY_EXCEPTION, ON_HOLD, RETURN_REQUESTED
    };
}