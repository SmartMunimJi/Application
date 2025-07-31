// app/src/main/java/com/project/smartmunimji/model/request/ClaimWarrantyRequest.java

package com.project.smartmunimji.model.request;

public class ClaimWarrantyRequest {
    private int registeredProductId;
    private String issueDescription;

    public ClaimWarrantyRequest(int registeredProductId, String issueDescription) {
        this.registeredProductId = registeredProductId;
        this.issueDescription = issueDescription;
    }

    // Getters
    public int getRegisteredProductId() { return registeredProductId; }
    public String getIssueDescription() { return issueDescription; }
}