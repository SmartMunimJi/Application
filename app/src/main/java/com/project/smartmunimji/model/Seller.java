// app/src/main/java/com/project/smartmunimji/model/Seller.java

package com.project.smartmunimji.model;

import com.google.gson.annotations.SerializedName;

// This model represents a Seller as returned by the API for dropdowns (e.g., GET /sm/customer/sellers)
public class Seller {
    @SerializedName("sellerId")
    private int sellerId;
    @SerializedName("shopName")
    private String shopName;

    public Seller(int sellerId, String shopName) {
        this.sellerId = sellerId;
        this.shopName = shopName;
    }

    // Getters
    public int getSellerId() {
        return sellerId;
    }

    public String getShopName() {
        return shopName;
    }

    // You might want a toString() for ArrayAdapter in spinners if not using custom adapter
    @Override
    public String toString() {
        return shopName;
    }
}