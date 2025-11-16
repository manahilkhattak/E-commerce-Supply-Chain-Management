package com.ecommerce.supplychain.common.constants;

public final class ReturnStatus {

    private ReturnStatus() {} // Utility class

    // Return Process Statuses
    public static final String REQUESTED = "REQUESTED";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
    public static final String PICKUP_SCHEDULED = "PICKUP_SCHEDULED";
    public static final String PICKUP_COMPLETED = "PICKUP_COMPLETED";
    public static final String IN_TRANSIT = "IN_TRANSIT";
    public static final String RECEIVED = "RECEIVED";
    public static final String INSPECTING = "INSPECTING";
    public static final String PROCESSING = "PROCESSING";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELLED = "CANCELLED";

    // Return Reasons
    public static final String DAMAGED = "DAMAGED";
    public static final String WRONG_ITEM = "WRONG_ITEM";
    public static final String NOT_AS_DESCRIBED = "NOT_AS_DESCRIBED";
    public static final String SIZE_ISSUE = "SIZE_ISSUE";
    public static final String CHANGE_MIND = "CHANGE_MIND";
    public static final String DEFECTIVE = "DEFECTIVE";
    public static final String LATE_DELIVERY = "LATE_DELIVERY";

    // Return Types
    public static final String REFUND = "REFUND";
    public static final String EXCHANGE = "EXCHANGE";
    public static final String STORE_CREDIT = "STORE_CREDIT";

    // Item Conditions
    public static final String NEW = "NEW";
    public static final String LIKE_NEW = "LIKE_NEW";
    public static final String USED = "USED";
    public static final String DAMAGED_CONDITION = "DAMAGED";
    public static final String OPEN_BOX = "OPEN_BOX";
    public static final String REFURBISHED = "REFURBISHED";
}