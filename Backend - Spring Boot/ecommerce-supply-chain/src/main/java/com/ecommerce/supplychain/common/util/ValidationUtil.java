package com.ecommerce.supplychain.common.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtil {

    // Regex patterns
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_PATTERN = "^[+]?[0-9]{10,15}$";
    private static final String SKU_PATTERN = "^[A-Z0-9-]{3,50}$";
    private static final String TRACKING_NUMBER_PATTERN = "^[A-Z0-9]{8,30}$";
    private static final String ZIP_CODE_PATTERN = "^[0-9]{5}(-[0-9]{4})?$";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PHONE_REGEX = Pattern.compile(PHONE_PATTERN);
    private static final Pattern SKU_REGEX = Pattern.compile(SKU_PATTERN);
    private static final Pattern TRACKING_NUMBER_REGEX = Pattern.compile(TRACKING_NUMBER_PATTERN);
    private static final Pattern ZIP_CODE_REGEX = Pattern.compile(ZIP_CODE_PATTERN);

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_REGEX.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return PHONE_REGEX.matcher(phone.trim()).matches();
    }

    /**
     * Validate SKU format
     */
    public static boolean isValidSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) return false;
        return SKU_REGEX.matcher(sku.trim()).matches();
    }

    /**
     * Validate tracking number format
     */
    public static boolean isValidTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) return false;
        return TRACKING_NUMBER_REGEX.matcher(trackingNumber.trim()).matches();
    }

    /**
     * Validate zip code format
     */
    public static boolean isValidZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) return false;
        return ZIP_CODE_REGEX.matcher(zipCode.trim()).matches();
    }

    /**
     * Validate quantity (positive integer)
     */
    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity > 0;
    }

    /**
     * Validate price (positive number)
     */
    public static boolean isValidPrice(Double price) {
        return price != null && price >= 0;
    }

    /**
     * Validate percentage (0-100)
     */
    public static boolean isValidPercentage(Double percentage) {
        return percentage != null && percentage >= 0 && percentage <= 100;
    }

    /**
     * Validate that string is not blank
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate that string has minimum length
     */
    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.trim().length() >= minLength;
    }

    /**
     * Validate that string has maximum length
     */
    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.trim().length() <= maxLength;
    }

    /**
     * Validate that number is within range
     */
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * Validate that number is within range
     */
    public static boolean isInRange(Double value, double min, double max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * Validate inventory level (not negative)
     */
    public static boolean isValidInventoryLevel(Integer quantity) {
        return quantity != null && quantity >= 0;
    }

    /**
     * Validate reorder level (positive)
     */
    public static boolean isValidReorderLevel(Integer reorderLevel) {
        return reorderLevel != null && reorderLevel > 0;
    }

    /**
     * Validate that reorder level is less than max capacity
     */
    public static boolean isValidInventorySettings(Integer reorderLevel, Integer maxCapacity) {
        if (reorderLevel == null || maxCapacity == null) return false;
        return reorderLevel > 0 && maxCapacity > 0 && reorderLevel < maxCapacity;
    }

    /**
     * Validate coordinates (latitude and longitude)
     */
    public static boolean isValidCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) return false;
        return latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }

    /**
     * Trim and validate string
     */
    public static String sanitizeString(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}