package com.example.skripsi.Model.SalesReport;

import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SalesReportModel {
    @SerializedName("created_at")
    private String orderDate;
    @SerializedName("table_number")
    private int orderTableNumber;
    @SerializedName("total_price")
    private String orderTotalPrice;
    @SerializedName(value = "orderDetails", alternate = "order_details") //Ini adalah "order_detail"
    ArrayList<OrderListItemDetailsDataModel> order_detail;

    private boolean isExpandable;

    public SalesReportModel(String orderDate, int orderTableNumber, String orderTotalPrice, ArrayList<OrderListItemDetailsDataModel> order_detail) {
        this.orderDate = orderDate;
        this.orderTableNumber = orderTableNumber;
        this.orderTotalPrice = orderTotalPrice;
        this.order_detail = order_detail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderTableNumber() {
        return orderTableNumber;
    }

    public void setOrderTableNumber(int orderTableNumber) {
        this.orderTableNumber = orderTableNumber;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public ArrayList<OrderListItemDetailsDataModel> getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(ArrayList<OrderListItemDetailsDataModel> order_detail) {
        this.order_detail = order_detail;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
