package com.example.skripsi.Model;

public class CheckoutItemModel {

    private String checkoutMenuName, checkoutMenuPrice, checkoutMenuCategory, checkoutMenuDescription, imgID;
    private int checkoutMenuQuantity;

    public CheckoutItemModel(String checkoutMenuName, String checkoutMenuPrice, int checkoutMenuQuantity, String imgID) {
        this.checkoutMenuName = checkoutMenuName;
        this.checkoutMenuPrice = checkoutMenuPrice;
        this.checkoutMenuQuantity = checkoutMenuQuantity;
        this.imgID = imgID;
    }

    public CheckoutItemModel(String checkoutMenuName, String checkoutMenuPrice, String checkoutMenuCategory, String checkoutMenuDescription, String imgID, int checkoutMenuQuantity) {
        this.checkoutMenuName = checkoutMenuName;
        this.checkoutMenuPrice = checkoutMenuPrice;
        this.checkoutMenuCategory = checkoutMenuCategory;
        this.checkoutMenuDescription = checkoutMenuDescription;
        this.imgID = imgID;
        this.checkoutMenuQuantity = checkoutMenuQuantity;
    }

    public String getCheckoutMenuName() {
        return checkoutMenuName;
    }

    public void setCheckoutMenuName(String checkoutMenuName) {
        this.checkoutMenuName = checkoutMenuName;
    }

    public String getCheckoutMenuPrice() {
        return checkoutMenuPrice;
    }

    public void setCheckoutMenuPrice(String checkoutMenuPrice) {
        this.checkoutMenuPrice = checkoutMenuPrice;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public int getCheckoutMenuQuantity() {
        return checkoutMenuQuantity;
    }

    public void setCheckoutMenuQuantity(int checkoutMenuQuantity) {
        this.checkoutMenuQuantity = checkoutMenuQuantity;
    }

    public String getCheckoutMenuCategory() {
        return checkoutMenuCategory;
    }

    public void setCheckoutMenuCategory(String checkoutMenuCategory) {
        this.checkoutMenuCategory = checkoutMenuCategory;
    }

    public String getCheckoutMenuDescription() {
        return checkoutMenuDescription;
    }

    public void setCheckoutMenuDescription(String checkoutMenuDescription) {
        this.checkoutMenuDescription = checkoutMenuDescription;
    }
}
