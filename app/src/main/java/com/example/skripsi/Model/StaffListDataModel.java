package com.example.skripsi.Model;

import java.util.ArrayList;

public class StaffListDataModel {
    private ArrayList<StaffDataModel> staffs;

    public ArrayList<StaffDataModel> getStaffs() {
        return staffs;
    }

    public void setStaffs(ArrayList<StaffDataModel> staffs) {
        this.staffs = staffs;
    }

    public StaffListDataModel(ArrayList<StaffDataModel> staffs) {
        this.staffs = staffs;
    }
}
