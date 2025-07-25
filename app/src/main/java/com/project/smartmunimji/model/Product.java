package com.project.smartmunimji.model;

public class Product {
    private String name;
    private String purchaseDate;
    private String warrantyEndDate;
    private boolean isWarrantyValid;
    private String issueDescription;
    private String claimStatus;

    public Product(String name, String purchaseDate, String warrantyEndDate, boolean isWarrantyValid) {
        this(name, purchaseDate, warrantyEndDate, isWarrantyValid, null, null);
    }

    public Product(String name, String purchaseDate, String warrantyEndDate, boolean isWarrantyValid, String issueDescription, String claimStatus) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.warrantyEndDate = warrantyEndDate;
        this.isWarrantyValid = isWarrantyValid;
        this.issueDescription = issueDescription;
        this.claimStatus = claimStatus;
    }

    public String getName() {
        return name;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public boolean isWarrantyValid() {
        return isWarrantyValid;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getClaimStatus() {
        return claimStatus;
    }
}