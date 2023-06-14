package com.example.skripsi.Model.Orders;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderListItemDataModel {

    @SerializedName(value = "tableNumber", alternate = "table_number")
    private int tableNumber;
    @SerializedName(value="order_id", alternate="orderId")
    private String orderId;
    @SerializedName(value = "order_detail", alternate = "orderDetails") //Ini adalah "order_detail"
    ArrayList<OrderListItemDetailsDataModel> order_detail;
    //Kita perlu Nama Ordernya gk ya
    private String orderName;
    //Ini perlu juga kah?
    private String totalPrice;
    //Ini juga kah?
    private int imgID;

    //Ini buat Display
    public OrderListItemDataModel(String orderName, String totalPrice, int imgID) {
        this.orderName = orderName;
        this.totalPrice = totalPrice;
        this.imgID = imgID;
    }

    //Ini buat POST ke Endpoint Create Order
    public OrderListItemDataModel(int tableNumber, ArrayList<OrderListItemDetailsDataModel> order_detail){
        this.tableNumber = tableNumber;
        this.order_detail = order_detail;
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

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<OrderListItemDetailsDataModel> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(ArrayList<OrderListItemDetailsDataModel> order_detail) {
        this.order_detail = order_detail;
    }
}
