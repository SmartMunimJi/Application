// app/src/main/java/com/project/smartmunimji/model/response/CommonResponse.java

package com.project.smartmunimji.model.response;

// For API responses that only contain status and message, or status, message, and generic data
public class CommonResponse<T> {
    private String status; // "success", "fail", "error"
    private String message;
    private T data; // Generic type for the data payload

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}