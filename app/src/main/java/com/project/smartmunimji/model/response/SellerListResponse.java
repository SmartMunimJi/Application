// app/src/main/java/com/project/smartmunimji/model/response/SellerListResponse.java

package com.project.smartmunimji.model.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SellerListResponse {
    // This class represents an individual seller object in the list
    @SerializedName("sellerId")
    private int sellerId;
    @SerializedName("shopName")
    private String shopName;

    // Getters
    public int getSellerId() { return sellerId; }
    public String getShopName() { return shopName; }
}