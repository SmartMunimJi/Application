// app/src/main/java/com/project/smartmunimji/model/response/ClaimListResponse.java

package com.project.smartmunimji.model.response;

import com.google.gson.annotations.SerializedName;

public class ClaimListResponse {
    @SerializedName("claimId")
    private int claimId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("issueDescription")
    private String issueDescription;
    @SerializedName("claimStatus")
    private String claimStatus;
    @SerializedName("claimedAt")
    private String claimedAt;
    @SerializedName("sellerResponseNotes")
    private String sellerResponseNotes;

    // Getters
    public int getClaimId() { return claimId; }
    public String getProductName() { return productName; }
    public String getIssueDescription() { return issueDescription; }
    public String getClaimStatus() { return claimStatus; }
    public String getClaimedAt() { return claimedAt; }
    public String getSellerResponseNotes() { return sellerResponseNotes; }
}