package com.example.skripsi.Model;

import java.util.ArrayList;

public class OrderListDataModel {

    private ArrayList<OrderListItemDataModel> orderLists;

    public ArrayList<OrderListItemDataModel> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(ArrayList<OrderListItemDataModel> orderLists) {
        this.orderLists = orderLists;
    }

    public OrderListDataModel(ArrayList<OrderListItemDataModel> orderLists) {
        this.orderLists = orderLists;
    }
}
