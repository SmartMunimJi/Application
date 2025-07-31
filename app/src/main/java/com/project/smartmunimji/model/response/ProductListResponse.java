// app/src/main/java/com/project/smartmunimji/model/response/ProductListResponse.java

package com.project.smartmunimji.model.response;

import com.google.gson.annotations.SerializedName;

public class ProductListResponse {
    @SerializedName("registeredProductId")
    private int registeredProductId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("shopName")
    private String shopName;
    @SerializedName("sellerOrderId")
    private String sellerOrderId;
    @SerializedName("dateOfPurchase")
    private String dateOfPurchase;
    @SerializedName("warrantyValidUntil")
    private String warrantyValidUntil;
    @SerializedName("isWarrantyEligible")
    private boolean isWarrantyEligible;
    @SerializedName("daysRemaining")
    private int daysRemaining; // From backend, can be 0 or negative

    // Getters
    public int getRegisteredProductId() { return registeredProductId; }
    public String getProductName() { return productName; }
    public String getShopName() { return shopName; }
    public String getSellerOrderId() { return sellerOrderId; }
    public String getDateOfPurchase() { return dateOfPurchase; }
    public String getWarrantyValidUntil() { return warrantyValidUntil; }
    public boolean isWarrantyEligible() { return isWarrantyEligible; }
    public int getDaysRemaining() { return daysRemaining; }
}