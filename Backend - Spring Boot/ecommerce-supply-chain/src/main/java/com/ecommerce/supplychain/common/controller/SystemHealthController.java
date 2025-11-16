package com.ecommerce.supplychain.common.controller;

import com.ecommerce.supplychain.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/common/health")
public class SystemHealthController {

    @Value("${spring.application.name:ecommerce-supply-chain}")
    private String applicationName;

    @Value("${spring.application.version:2.0.0}")
    private String applicationVersion;

    /**
     * API 1: System Health Check
     * GET /api/common/health
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        log.info("System health check requested");

        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("application", applicationName);
        healthInfo.put("version", applicationVersion);
        healthInfo.put("timestamp", DateUtil.getCurrentTimestamp());

        // System information
        healthInfo.put("system", getSystemInfoData());

        // Memory information
        healthInfo.put("memory", getMemoryInfo());

        // Process information
        healthInfo.put("process", getProcessInfo());

        // Database connectivity (simulated)
        healthInfo.put("database", getDatabaseStatus());

        return ResponseEntity.ok(healthInfo);
    }

    /**
     * API 2: System Information
     * GET /api/common/health/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        log.info("System information requested");

        Map<String, Object> systemInfo = new HashMap<>();

        // Application info
        systemInfo.put("name", applicationName);
        systemInfo.put("version", applicationVersion);
        systemInfo.put("environment", getEnvironment());
        systemInfo.put("startTime", getStartTime());
        systemInfo.put("uptime", getUptime());

        // JVM info
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("javaVendor", System.getProperty("java.vendor"));
        systemInfo.put("jvmName", System.getProperty("java.vm.name"));
        systemInfo.put("jvmVersion", System.getProperty("java.vm.version"));

        // OS info
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("osArchitecture", System.getProperty("os.arch"));

        // Runtime info
        systemInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        systemInfo.put("systemLoad", getSystemLoad());

        return ResponseEntity.ok(systemInfo);
    }

    /**
     * Private method to get system information - renamed to avoid conflict
     */
    private Map<String, Object> getSystemInfoData() {
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("javaVendor", System.getProperty("java.vendor"));
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("osArchitecture", System.getProperty("os.arch"));
        systemInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        return systemInfo;
    }

    private Map<String, Object> getMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("maxMemory", runtime.maxMemory() / (1024 * 1024) + " MB");
        memoryInfo.put("totalMemory", runtime.totalMemory() / (1024 * 1024) + " MB");
        memoryInfo.put("freeMemory", runtime.freeMemory() / (1024 * 1024) + " MB");
        memoryInfo.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024) + " MB");

        // Heap memory usage
        memoryInfo.put("heapUsage", memoryMXBean.getHeapMemoryUsage().getUsed() / (1024 * 1024) + " MB");
        memoryInfo.put("heapMax", memoryMXBean.getHeapMemoryUsage().getMax() / (1024 * 1024) + " MB");

        // Non-heap memory usage
        memoryInfo.put("nonHeapUsage", memoryMXBean.getNonHeapMemoryUsage().getUsed() / (1024 * 1024) + " MB");

        return memoryInfo;
    }

    private Map<String, Object> getProcessInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        Map<String, Object> processInfo = new HashMap<>();
        processInfo.put("pid", runtimeMXBean.getPid());
        processInfo.put("startTime", DateUtil.formatDateTime(
                LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(runtimeMXBean.getStartTime()),
                        ZoneId.systemDefault()
                )
        ));
        processInfo.put("uptime", runtimeMXBean.getUptime() / 1000 + " seconds");
        processInfo.put("inputArguments", runtimeMXBean.getInputArguments());

        return processInfo;
    }

    private Map<String, Object> getDatabaseStatus() {
        Map<String, Object> dbStatus = new HashMap<>();
        dbStatus.put("status", "CONNECTED");
        dbStatus.put("type", "MySQL");
        dbStatus.put("timestamp", DateUtil.getCurrentTimestamp());
        // In real implementation, this would test actual database connectivity
        return dbStatus;
    }

    private String getEnvironment() {
        String profile = System.getProperty("spring.profiles.active");
        return profile != null ? profile : "default";
    }

    private String getStartTime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return DateUtil.formatDateTime(
                LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(runtimeMXBean.getStartTime()),
                        ZoneId.systemDefault()
                )
        );
    }

    private String getUptime() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();

        long days = uptime / (1000 * 60 * 60 * 24);
        long hours = (uptime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (uptime % (1000 * 60)) / 1000;

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }

    private double getSystemLoad() {
        try {
            // For cross-platform compatibility, we'll use a simpler approach
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;

            // Calculate memory usage percentage as a simple load indicator
            return (double) usedMemory / totalMemory * 100;
        } catch (Exception e) {
            log.warn("Could not calculate system load: {}", e.getMessage());
            return -1; // Not available
        }
    }
}