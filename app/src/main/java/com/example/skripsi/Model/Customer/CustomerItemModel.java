package com.example.skripsi.Model.Customer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerItemModel implements Serializable {

    @SerializedName(value="id")
    private int id;
    @SerializedName(value="user_id")
    private String userId;

    @SerializedName(value="name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;

    public CustomerItemModel(int id, String userId, String name, String email, String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
