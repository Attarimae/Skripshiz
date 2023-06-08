package com.example.skripsi.Model;

import com.google.gson.annotations.SerializedName;

public class StaffDataModel {
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

    private String key;

    //No Argument Constructor
    public StaffDataModel(){

    }

    //Register
    public StaffDataModel(String name, String email, String phone_number, String password) {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
    }

    //Login
    public StaffDataModel(String staff_id, String email, String password) {
        this.staff_id = staff_id;
        this.email = email;
        this.password = password;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
