package com.vasan12sp.biddobuy;

public class Products {

    String name, description, sellerPhone, category;
    Float basePrice;
    String productId;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Products() {
    }

    public Products(String name, String description, String sellerPhone, String category, Float basePrice, String productId) {
        this.name = name;
        this.description = description;
        this.sellerPhone = sellerPhone;
        this.category = category;
        this.basePrice = basePrice;
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public void setBasePrice(Float basePrice) {
        this.basePrice = basePrice;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public Float getBasePrice() {
        return basePrice;
    }

    public String getProductId() {
        return productId;
    }
}
