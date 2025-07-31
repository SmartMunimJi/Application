// app/src/main/java/com/project/smartmunimji/model/request/RegisterCustomerRequest.java

package com.project.smartmunimji.model.request;

public class RegisterCustomerRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber; // Note: Backend uses 'phoneNumber'
    private String address;

    public RegisterCustomerRequest(String name, String email, String password, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
}