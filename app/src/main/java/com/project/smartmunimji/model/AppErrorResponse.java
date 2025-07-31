// app/src/main/java/com/project/smartmunimji/model/AppErrorResponse.java

package com.project.smartmunimji.model;

import com.google.gson.annotations.SerializedName;

public class AppErrorResponse {
    @SerializedName("status")
    private String status; // "fail" or "error"
    @SerializedName("message")
    private String message;

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}