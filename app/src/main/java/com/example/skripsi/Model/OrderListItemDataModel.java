package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

public class OrderListItemDataModel {

    @SerializedName("tableNumber")
    private String tableNumber;
    @SerializedName(value="orderId", alternate="order_id")
    private String orderId;
    //Kita perlu Nama Ordernya gk ya
    private String orderName;
    //Ini perlu juga kah?
    private String totalPrice;
    //Ini juga kah?
    private int imgID;
    //Note: Blom kutambahin "order_detail" utk POST ke endpointnya

    public OrderListItemDataModel(String orderName, String totalPrice, int imgID) {
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
