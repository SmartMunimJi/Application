// app/src/main/java/com/project/smartmunimji/model/request/AddProductRequest.java

package com.project.smartmunimji.model.request;

public class AddProductRequest {
    private int sellerId;
    private String orderId;
    private String purchaseDate;

    public AddProductRequest(int sellerId, String orderId, String purchaseDate) {
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.purchaseDate = purchaseDate;
    }

    // Getters
    public int getSellerId() { return sellerId; }
    public String getOrderId() { return orderId; }
    public String getPurchaseDate() { return purchaseDate; }
}