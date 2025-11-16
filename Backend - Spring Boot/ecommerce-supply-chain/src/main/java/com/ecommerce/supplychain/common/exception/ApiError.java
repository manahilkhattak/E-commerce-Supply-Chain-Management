package com.ecommerce.supplychain.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private HttpStatus status;
    private int statusCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;
    private String debugMessage;
    private String path;
    private String errorCode;

    private Map<String, String> validationErrors;
    private List<ApiSubError> subErrors;

    public ApiError(HttpStatus status) {
        this.status = status;
        this.statusCode = status.value();
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String debugMessage) {
        this(status, message);
        this.debugMessage = debugMessage;
    }

    public ApiError(HttpStatus status, String message, String debugMessage, String path) {
        this(status, message, debugMessage);
        this.path = path;
    }

    // Sub error class for detailed error information
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;
        private String code;

        public ApiSubError(String object, String message) {
            this.object = object;
            this.message = message;
        }

        public ApiSubError(String object, String field, Object rejectedValue, String message) {
            this.object = object;
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
    }
}