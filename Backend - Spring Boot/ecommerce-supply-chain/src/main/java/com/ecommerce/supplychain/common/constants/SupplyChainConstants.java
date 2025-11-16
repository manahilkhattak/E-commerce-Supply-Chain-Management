package com.ecommerce.supplychain.common.constants;

public final class SupplyChainConstants {

    private SupplyChainConstants() {} // Utility class

    // System Configuration
    public static final String SYSTEM_NAME = "E-Commerce Supply Chain Management";
    public static final String SYSTEM_VERSION = "2.0.0";
    public static final String DEFAULT_TIMEZONE = "UTC";

    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    // Inventory Configuration
    public static final int MIN_REORDER_LEVEL = 5;
    public static final int MAX_REORDER_LEVEL = 1000;
    public static final double DEFAULT_CARRYING_COST_PERCENTAGE = 0.25; // 25%
    public static final double DEFAULT_SERVICE_LEVEL = 0.95; // 95%
    public static final double HIGH_VARIANCE_THRESHOLD = 5.0; // 5%

    // Performance Thresholds
    public static final double TARGET_ON_TIME_DELIVERY = 95.0; // 95%
    public static final double TARGET_ORDER_ACCURACY = 99.5; // 99.5%
    public static final double TARGET_INVENTORY_ACCURACY = 99.0; // 99%
    public static final double ACCEPTABLE_RETURN_RATE = 3.0; // 3%

    // Financial Constants
    public static final double DEFAULT_TAX_RATE = 0.08; // 8%
    public static final double DEFAULT_SHIPPING_COST = 5.99;
    public static final double MAX_INSURANCE_AMOUNT = 5000.00;

    // Validation Limits
    public static final int MAX_PRODUCT_NAME_LENGTH = 255;
    public static final int MAX_SKU_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_ADDRESS_LENGTH = 500;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 20;

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // File Upload Limits
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_FILE_TYPES = {
            "image/jpeg", "image/png", "image/gif", "application/pdf",
            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    };

    // API Configuration
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;
    public static final int DEFAULT_API_TIMEOUT = 30; // seconds

    // Security Constants
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 128;
    public static final int SESSION_TIMEOUT = 30; // minutes

    // Notification Constants
    public static final int ALERT_RETENTION_DAYS = 90;
    public static final int NOTIFICATION_BATCH_SIZE = 50;

    // Cache Configuration
    public static final int CACHE_TTL_MINUTES = 30;
    public static final int CACHE_MAX_SIZE = 1000;

    // Performance Monitoring
    public static final int SLOW_QUERY_THRESHOLD_MS = 1000;
    public static final int HIGH_MEMORY_USAGE_PERCENT = 80;

    // Business Rules
    public static final int MAX_RETURN_PERIOD_DAYS = 30;
    public static final int MAX_ORDER_QUANTITY = 1000;
    public static final double MAX_DISCOUNT_PERCENTAGE = 50.0;

    // Error Codes
    public static final String ERROR_VALIDATION_FAILED = "VALIDATION_FAILED";
    public static final String ERROR_RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String ERROR_INSUFFICIENT_STOCK = "INSUFFICIENT_STOCK";
    public static final String ERROR_DUPLICATE_ENTRY = "DUPLICATE_ENTRY";
    public static final String ERROR_UNAUTHORIZED = "UNAUTHORIZED";
    public static final String ERROR_SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String ERROR_BUSINESS_RULE_VIOLATION = "BUSINESS_RULE_VIOLATION";
}