package com.example.skripsi.Model.Orders;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderListItemDataModel {

    @SerializedName(value = "tableNumber", alternate = "table_number")
    private int tableNumber;
    @SerializedName(value="orderId", alternate="order_id")
    private String orderId;
    @SerializedName(value = "orderDetails", alternate = "order_details") //Ini adalah "order_detail"
    ArrayList<OrderListItemDetailsDataModel> order_detail;
    @SerializedName(value = "order_status")
    private String order_status;
    @SerializedName(value = "order_done")
    private int order_done;
    @SerializedName(value = "total_price")
    private String totalPrice;
    @SerializedName(value = "id")
    private String id;
    @SerializedName(value = "created_at")
    private String created_at;

//    private String orderName;
//    private int imgID;

    //Ini buat Display Dummy Data
//    public OrderListItemDataModel(String orderName, String totalPrice, int imgID) {
//        this.orderName = orderName;
//        this.totalPrice = totalPrice;
//        this.imgID = imgID;
//    }

    //Ini buat POST ke Endpoint Create Order
    public OrderListItemDataModel(int tableNumber, ArrayList<OrderListItemDetailsDataModel> order_detail){
        this.tableNumber = tableNumber;
        this.order_detail = order_detail;
        this.order_status = order_status;
    }

    //Ini buat GET Order List/Order History
    public OrderListItemDataModel(int tableNumber, String totalPrice, String created_at, ArrayList<OrderListItemDetailsDataModel> order_detail, String order_status, String id){
        this.tableNumber = tableNumber;
        this.totalPrice = totalPrice;
        this.created_at = created_at;
        this.order_detail = order_detail;
        this.order_status = order_status;
        this.id = id;
    }

    public OrderListItemDataModel(String orderId,int tableNumber, String totalPrice, ArrayList<OrderListItemDetailsDataModel> order_detail){
        this.orderId=orderId;
        this.tableNumber = tableNumber;
        this.order_detail = order_detail;
        this.totalPrice = totalPrice;
    }

    //Ini buat PATCH ke Endpoint Update Order
    public OrderListItemDataModel(String id, int tableNumber, ArrayList<OrderListItemDetailsDataModel> order_detail){
        this.orderId = id;
        this.tableNumber = tableNumber;
        this.order_detail = order_detail;
    }

//    public String getOrderName() {
//        return orderName;
//    }
//
//    public void setOrderName(String orderName) {
//        this.orderName = orderName;
//    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

//    public int getImgID() {
//        return imgID;
//    }
//
//    public void setImgID(int imgID) {
//        this.imgID = imgID;
//    }

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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public int getOrder_done() {
        return order_done;
    }

    public void setOrder_done(int order_done) {
        this.order_done = order_done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
