package com.example.skripsi.Model.Promotion;

import com.google.gson.annotations.SerializedName;

public class PromotionItemModel {

    //@SerializedName(value="id")
    private int id;
    //@SerializedName(value="category_name", alternate={"category","categoryName"})
    private int promotionQuota;
    //@SerializedName("menu_name")
    private String promotionName;
    //@SerializedName("description")
    private String promotionDescription;

    private String promotionDate;

    private String promotionType;

    //@SerializedName("picture_code")
    private String imgID; //R.drawable pake int soalny

    //Display
    public PromotionItemModel(int id, int promotionQuota, String promotionName, String promotionDescription, String promotionDate){
        this.id = id;
        this.promotionQuota = promotionQuota;
        this.promotionName = promotionName;
        this.promotionDescription = promotionDescription;
        this.promotionDate = promotionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromotionQuota() {
        return promotionQuota;
    }

    public void setPromotionQuota(int promotionQuota) {
        this.promotionQuota = promotionQuota;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public String getPromotionDate() {
        return promotionDate;
    }

    public void setPromotionDate(String promotionDate) {
        this.promotionDate = promotionDate;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
