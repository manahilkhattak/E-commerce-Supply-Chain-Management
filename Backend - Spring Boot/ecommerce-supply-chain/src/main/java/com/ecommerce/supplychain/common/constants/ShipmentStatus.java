package com.ecommerce.supplychain.common.constants;

public final class ShipmentStatus {

    private ShipmentStatus() {} // Utility class

    // Shipment Lifecycle Statuses
    public static final String CREATED = "CREATED";
    public static final String LABEL_CREATED = "LABEL_CREATED";
    public static final String PICKUP_SCHEDULED = "PICKUP_SCHEDULED";
    public static final String PICKED_UP = "PICKED_UP";
    public static final String IN_TRANSIT = "IN_TRANSIT";
    public static final String AT_SORT_FACILITY = "AT_SORT_FACILITY";
    public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String DELIVERED = "DELIVERED";
    public static final String FAILED_ATTEMPT = "FAILED_ATTEMPT";
    public static final String RETURN_TO_SENDER = "RETURN_TO_SENDER";
    public static final String EXCEPTION = "EXCEPTION";
    public static final String CANCELLED = "CANCELLED";
    public static final String LOST = "LOST";
    public static final String DAMAGED = "DAMAGED";

    // Carrier Status Codes
    public static final String CARRIER_PICKUP = "CARRIER_PICKUP";
    public static final String CARRIER_DELIVERY = "CARRIER_DELIVERY";
    public static final String CARRIER_EXCEPTION = "CARRIER_EXCEPTION";

    // Status groups
    public static final String[] IN_PROGRESS_STATUSES = {
            CREATED, LABEL_CREATED, PICKUP_SCHEDULED, PICKED_UP,
            IN_TRANSIT, AT_SORT_FACILITY, OUT_FOR_DELIVERY
    };

    public static final String[] PROBLEM_STATUSES = {
            FAILED_ATTEMPT, RETURN_TO_SENDER, EXCEPTION, LOST, DAMAGED
    };
}