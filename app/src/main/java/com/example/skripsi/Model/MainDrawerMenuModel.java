package com.example.skripsi.Model;

public class MainDrawerMenuModel {

    private String name;
    private int icon;

    public MainDrawerMenuModel(int icon, String name) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
