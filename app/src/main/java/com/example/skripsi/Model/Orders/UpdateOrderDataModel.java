package com.example.skripsi.Model.Orders;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateOrderDataModel {
    @SerializedName(value="orderId", alternate="order_id")
    private String orderId;
    @SerializedName(value = "order_detail_id")
    private String order_detail_id;
    @SerializedName(value = "status")
    private String status;
    @SerializedName(value = "orderDetails", alternate = "order_details") //Ini adalah "order_detail"
    ArrayList<OrderListItemDetailsDataModel> order_detail;
    @SerializedName(value = "order_status")
    private String order_status;
    @SerializedName(value = "tableNumber", alternate = "table_number")
    private int tableNumber;
    @SerializedName(value = "order_done")
    private int order_done;
    @SerializedName(value = "total_price")
    private String totalPrice;
    @SerializedName(value = "id")
    private String id;
    @SerializedName(value = "amount")
    private int amount;
    @SerializedName(value = "description")
    private String description;
    @SerializedName(value = "menu_name")
    private String menu_name;
    @SerializedName(value = "quantity")
    private int quantity;

    //Ini buat POST ke Endpoint Update Order Details Status
    public UpdateOrderDataModel(String orderId, String order_detail_id, String status){
        this.orderId = orderId;
        this.order_detail_id = order_detail_id;
        this.status = status;
    }

    //Ini buat POST ke Endpoint Update Order Status
    public UpdateOrderDataModel(String orderId, String status){
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
