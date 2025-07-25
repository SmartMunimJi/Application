package com.project.smartmunimji.model;

public class Offer {
    private String id;
    private String title;
    private String description;
    private String sellerName;

    public Offer(String id, String title, String description, String sellerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sellerName = sellerName;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerName() {
        return sellerName;
    }
}