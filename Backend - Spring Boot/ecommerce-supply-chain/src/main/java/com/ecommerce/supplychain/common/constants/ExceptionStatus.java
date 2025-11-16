package com.ecommerce.supplychain.common.constants;

public final class ExceptionStatus {

    private ExceptionStatus() {} // Utility class

    // Exception Types
    public static final String DAMAGED = "DAMAGED";
    public static final String LOST = "LOST";
    public static final String DELAYED = "DELAYED";
    public static final String REFUSED = "REFUSED";
    public static final String WRONG_ADDRESS = "WRONG_ADDRESS";
    public static final String CUSTOMER_NOT_AVAILABLE = "CUSTOMER_NOT_AVAILABLE";
    public static final String WEATHER_DELAY = "WEATHER_DELAY";
    public static final String MECHANICAL_ISSUE = "MECHANICAL_ISSUE";

    // Exception Severity Levels
    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";
    public static final String CRITICAL = "CRITICAL";

    // Exception Status
    public static final String OPEN = "OPEN";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String RESOLVED = "RESOLVED";
    public static final String ESCALATED = "ESCALATED";
    public static final String CLOSED = "CLOSED";

    // Resolution Types
    public static final String RESHIP = "RESHIP";
    public static final String REFUND = "REFUND";
    public static final String COMPENSATION = "COMPENSATION";
    public static final String EXCHANGE = "EXCHANGE";
    public static final String DELIVERY_RETRY = "DELIVERY_RETRY";
    public static final String CANCELLATION = "CANCELLATION";

    // Priority Levels
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";
    public static final String PRIORITY_URGENT = "URGENT";
}