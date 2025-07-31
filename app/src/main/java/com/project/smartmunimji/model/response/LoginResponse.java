// app/src/main/java/com/project/smartmunimji/model/response/LoginResponse.java

package com.project.smartmunimji.model.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("jwtToken")
    private String jwtToken;
    @SerializedName("userId")
    private int userId;
    @SerializedName("role")
    private String role;

    // Getters
    public String getJwtToken() {
        return jwtToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}