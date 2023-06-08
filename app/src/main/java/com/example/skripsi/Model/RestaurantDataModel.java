package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

public class RestaurantDataModel {
    @SerializedName("restaurant_name")
    private String restaurant_name;
    @SerializedName("restaurant_id")
    private String restaurant_id;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String key;

    //No Argument Constructor
    public RestaurantDataModel(){

    }

    //Register
    public RestaurantDataModel(String restaurant_name, String email, String phone_number, String password) {
        this.restaurant_name = restaurant_name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
    }

    //Login
    public RestaurantDataModel(String restaurant_id, String password) {
        this.restaurant_id = restaurant_id;
        this.password = password;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
