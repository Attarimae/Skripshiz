package com.example.skripsi.API;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesCashier {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<CheckoutItemModel> checkoutList;
    ArrayList<OrderListItemDetailsDataModel> order_detail;

    public SharedPreferencesCashier(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    //Checkout (Create Order)
    public void saveCheckoutList(ArrayList<CheckoutItemModel> checkoutList){
        Gson gson = new Gson();
        String json = gson.toJson(checkoutList);
        editor.putString("checkoutList", json);
        editor.commit();
    }

    public ArrayList<CheckoutItemModel> fetchCheckoutList(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("checkoutList", null);
        Type type = new TypeToken<ArrayList<CheckoutItemModel>>() {}.getType();
        checkoutList = gson.fromJson(json, type);
        if(checkoutList == null){
            checkoutList = new ArrayList<>();
        }
        return checkoutList;
    }

    //Profile Picture
    public void saveCashierPic(String storedImgB64){
        editor.putString("cashierImage", storedImgB64);
        editor.commit();
    }

    public String fetchCashierPic(){
        return sharedPreferences.getString("cashierImage", null);
    }

    //Order Details
    public void saveOrderId(String order_id){
        editor.putString("order_id", order_id);
        editor.commit();
    }

    public String fetchOrderId(){
        return sharedPreferences.getString("order_id", null);
    }

    public void saveTableNumber(int table_number){
        editor.putInt("table_number", table_number);
        editor.commit();
    }

    public int fetchTableNumber(){
        return sharedPreferences.getInt("table_number", 0);
    }

    public void saveOrderDetails(ArrayList<OrderListItemDetailsDataModel> order_detail){
        Gson gson = new Gson();
        String json = gson.toJson(order_detail);
        editor.putString("order_detail", json);
        editor.commit();
    }

    public ArrayList<OrderListItemDetailsDataModel> fetchOrderDetails(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("order_detail", null);
        Type type = new TypeToken<ArrayList<OrderListItemDetailsDataModel>>() {}.getType();
        order_detail = gson.fromJson(json, type);
        if(order_detail == null){
            order_detail = new ArrayList<>();
        }
        return order_detail;
    }
}
