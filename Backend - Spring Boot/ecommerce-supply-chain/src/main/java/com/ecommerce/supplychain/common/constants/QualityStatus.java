package com.ecommerce.supplychain.common.constants;

public final class QualityStatus {

    private QualityStatus() {} // Utility class

    // Quality Check Statuses
    public static final String PENDING = "PENDING";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String PASSED = "PASSED";
    public static final String FAILED = "FAILED";
    public static final String CONDITIONAL_PASS = "CONDITIONAL_PASS";
    public static final String REQUIRES_RETEST = "REQUIRES_RETEST";
    public static final String CANCELLED = "CANCELLED";

    // Quality Grades
    public static final String GRADE_A_PLUS = "A+";
    public static final String GRADE_A = "A";
    public static final String GRADE_B = "B";
    public static final String GRADE_C = "C";
    public static final String GRADE_D = "D";
    public static final String GRADE_F = "F";

    // Inspection Types
    public static final String INCOMING = "INCOMING";
    public static final String IN_PROCESS = "IN_PROCESS";
    public static final String FINAL = "FINAL";
    public static final String RANDOM = "RANDOM";
    public static final String COMPLAINT = "COMPLAINT";

    // Defect Severity Levels
    public static final String MINOR = "MINOR";
    public static final String MAJOR = "MAJOR";
    public static final String CRITICAL = "CRITICAL";
}