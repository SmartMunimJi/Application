// app/src/main/java/com/project/smartmunimji/model/response/CustomerProfileResponse.java

package com.project.smartmunimji.model.response;

import com.google.gson.annotations.SerializedName;

public class CustomerProfileResponse {
    @SerializedName("userId")
    private int userId;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber") // Backend field name
    private String phoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;

    // Getters
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getName() { return name; }
    public String getAddress() { return address; }
}