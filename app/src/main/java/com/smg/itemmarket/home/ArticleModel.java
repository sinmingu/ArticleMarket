package com.smg.itemmarket.home;

public class ArticleModel {
    private String sellerId;
    private String title;
    private Long createAt;
    private String price;
    private String imageUrl;

    public ArticleModel() {
        this.sellerId = "";
        this.title = "";
        this.createAt = 0L;
        this.price = "";
        this.imageUrl = "";
    }

    public ArticleModel(String sellerId, String title, Long createAt, String price, String imageUrl) {
        this.sellerId = sellerId;
        this.title = title;
        this.createAt = createAt;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTitle() {
        return title;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
