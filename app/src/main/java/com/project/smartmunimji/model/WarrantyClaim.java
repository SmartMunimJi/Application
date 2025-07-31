// app/src/main/java/com/project/smartmunimji/model/WarrantyClaim.java

package com.project.smartmunimji.model;

import com.google.gson.annotations.SerializedName;

// This model represents a Warranty Claim from the backend API (GET /sm/customer/claims)
public class WarrantyClaim {
    @SerializedName("claimId")
    private int claimId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("issueDescription")
    private String issueDescription;
    @SerializedName("claimStatus")
    private String claimStatus;
    @SerializedName("claimedAt")
    private String claimedAt; // Date string in ISO 8601 or YYYY-MM-DD
    @SerializedName("sellerResponseNotes")
    private String sellerResponseNotes; // Can be null

    public WarrantyClaim(int claimId, String productName, String issueDescription,
                         String claimStatus, String claimedAt, String sellerResponseNotes) {
        this.claimId = claimId;
        this.productName = productName;
        this.issueDescription = issueDescription;
        this.claimStatus = claimStatus;
        this.claimedAt = claimedAt;
        this.sellerResponseNotes = sellerResponseNotes;
    }

    // Getters
    public int getClaimId() { return claimId; }
    public String getProductName() { return productName; }
    public String getIssueDescription() { return issueDescription; }
    public String getClaimStatus() { return claimStatus; }
    public String getClaimedAt() { return claimedAt; }
    public String getSellerResponseNotes() { return sellerResponseNotes; }
}