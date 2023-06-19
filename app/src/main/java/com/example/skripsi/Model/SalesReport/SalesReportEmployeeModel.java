package com.example.skripsi.Model.SalesReport;

import com.google.gson.annotations.SerializedName;

public class SalesReportEmployeeModel {
    @SerializedName(value="id")
    private int id;
    @SerializedName(value="staff_id")
    private String staffId;
    //@SerializedName(value="staff_name", alternate={"staffName"})
    @SerializedName(value="user_id")
    private String staffName;
    //This for Sales Report
    private int salesAmount;

    public SalesReportEmployeeModel(String staffName, int salesAmount) {
        this.staffName = staffName;
        this.salesAmount = salesAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(int salesAmount) {
        this.salesAmount = salesAmount;
    }
}
