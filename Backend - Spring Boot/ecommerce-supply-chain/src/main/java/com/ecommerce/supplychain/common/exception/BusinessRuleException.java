package com.ecommerce.supplychain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessRuleException extends RuntimeException {

    private String ruleCode;
    private String entityType;
    private Long entityId;

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, String ruleCode) {
        super(message);
        this.ruleCode = ruleCode;
    }

    public BusinessRuleException(String message, String ruleCode, String entityType, Long entityId) {
        super(message);
        this.ruleCode = ruleCode;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Getters
    public String getRuleCode() { return ruleCode; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }
}