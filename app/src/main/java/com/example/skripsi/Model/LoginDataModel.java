package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

public class LoginDataModel {
    @SerializedName("staff_id")
    private String staff_id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("password")
    private String password;
    @SerializedName("role")
    private String role;
    @SerializedName("restaurant_name")
    private String restaurant_name;
    @SerializedName("restaurant_id")
    private String restaurant_id;
    @SerializedName("token")
    private String key;

    public LoginDataModel(String staff_id, String name, String email, String phone_number, String password, String role, String restaurant_name, String restaurant_id, String key) {
        this.staff_id = staff_id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.role = role;
        this.restaurant_name = restaurant_name;
        this.restaurant_id = restaurant_id;
        this.key = key;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
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
