package com.example.skripsi.Model.SalesReport;

import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class POSTReportOrder {

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
    @SerializedName(value = "user_id")
    private String user_id;
    @SerializedName(value = "id")
    private String id;
    @SerializedName(value = "created_at")
    private String created_at;
    @SerializedName(value = "date")
    private String date;
    @SerializedName(value = "unit")
    private int unit;

    public POSTReportOrder(String date, int unit) {
        this.date = date;
        this.unit = unit;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
