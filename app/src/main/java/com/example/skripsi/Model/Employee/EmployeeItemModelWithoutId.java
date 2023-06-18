package com.example.skripsi.Model.Employee;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmployeeItemModelWithoutId implements Serializable {
    @SerializedName(value="staffId", alternate={"staff_id"})
    private String staffId;
    @SerializedName("staff_name")
    private String staffName;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("role")
    private String role;

    @SerializedName("picture_code")
    private String imgID;

    public EmployeeItemModelWithoutId(String staffId, String staffName, String email, String phoneNumber, String role, String imgID) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
