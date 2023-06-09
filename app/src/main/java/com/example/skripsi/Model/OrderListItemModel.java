package com.example.skripsi.Model;

public class OrderListItemModel {

    private String orderName, totalPrice;
    private int imgID;

    public OrderListItemModel(String orderName, String totalPrice, int imgID) {
        this.orderName = orderName;
        this.totalPrice = totalPrice;
        this.imgID = imgID;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
}
