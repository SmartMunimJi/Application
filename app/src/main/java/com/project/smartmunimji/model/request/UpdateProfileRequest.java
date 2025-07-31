// app/src/main/java/com/project/smartmunimji/model/request/UpdateProfileRequest.java

package com.project.smartmunimji.model.request;

public class UpdateProfileRequest {
    private String name;
    private String address;

    public UpdateProfileRequest(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters (optional for request objects but good practice)
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}