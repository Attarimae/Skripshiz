package com.example.skripsi.Model;

public class CheckoutItemModel {

    private String checkoutName, checkoutPrice;
    private int imgID, checkoutQuantity;

    public CheckoutItemModel(String checkoutName, String checkoutPrice, int checkoutQuantity, int imgID) {
        this.checkoutName = checkoutName;
        this.checkoutPrice = checkoutPrice;
        this.checkoutQuantity = checkoutQuantity;
        this.imgID = imgID;
    }

    public String getCheckoutName() {
        return checkoutName;
    }

    public void setCheckoutName(String checkoutName) {
        this.checkoutName = checkoutName;
    }

    public String getCheckoutPrice() {
        return checkoutPrice;
    }

    public void setCheckoutPrice(String checkoutPrice) {
        this.checkoutPrice = checkoutPrice;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getCheckoutQuantity() {
        return checkoutQuantity;
    }

    public void setCheckoutQuantity(int checkoutQuantity) {
        this.checkoutQuantity = checkoutQuantity;
    }
}
