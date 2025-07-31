// app/src/main/java/com/project/smartmunimji/model/Product.java

package com.project.smartmunimji.model;

import com.google.gson.annotations.SerializedName;

// This model represents a registered product from the backend API (GET /sm/customer/products)
public class Product {
    @SerializedName("registeredProductId")
    private int registeredProductId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("shopName")
    private String shopName;
    @SerializedName("sellerOrderId")
    private String sellerOrderId;
    @SerializedName("dateOfPurchase")
    private String dateOfPurchase; // YYYY-MM-DD format
    @SerializedName("warrantyValidUntil")
    private String warrantyValidUntil; // YYYY-MM-DD format
    @SerializedName("isWarrantyEligible")
    private boolean isWarrantyEligible;
    @SerializedName("daysRemaining")
    private int daysRemaining;

    public Product(int registeredProductId, String productName, String shopName, String sellerOrderId,
                   String dateOfPurchase, String warrantyValidUntil, boolean isWarrantyEligible, int daysRemaining) {
        this.registeredProductId = registeredProductId;
        this.productName = productName;
        this.shopName = shopName;
        this.sellerOrderId = sellerOrderId;
        this.dateOfPurchase = dateOfPurchase;
        this.warrantyValidUntil = warrantyValidUntil;
        this.isWarrantyEligible = isWarrantyEligible;
        this.daysRemaining = daysRemaining;
    }

    // Getters
    public int getRegisteredProductId() { return registeredProductId; }
    public String getProductName() { return productName; }
    public String getShopName() { return shopName; }
    public String getSellerOrderId() { return sellerOrderId; }
    public String getDateOfPurchase() { return dateOfPurchase; }
    public String getWarrantyValidUntil() { return warrantyValidUntil; }
    public boolean isWarrantyEligible() { return isWarrantyEligible; }
    public int getDaysRemaining() { return daysRemaining; }

    // No setters needed if this is primarily a data response model
}