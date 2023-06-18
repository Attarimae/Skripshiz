package com.example.skripsi.Model.Employee;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmployeeItemModel implements Serializable {

    @SerializedName(value="id")
    private int id;
    @SerializedName(value="staff_id")
    private String staffId;
    @SerializedName(value="staff_name", alternate={"staffName"})
    private String staffName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("role")
    private String role;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    @SerializedName("picture_code")
    private String imgID;

    public EmployeeItemModel(int id, String staffId, String staffName, String email, String phoneNumber, String role, String imgID) {
        this.id = id;
        this.staffId = staffId;
        this.staffName = staffName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.imgID = imgID;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getrole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
