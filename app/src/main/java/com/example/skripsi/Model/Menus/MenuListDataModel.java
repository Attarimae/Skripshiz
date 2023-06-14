package com.example.skripsi.Model.Menus;

import java.util.ArrayList;

public class MenuListDataModel {
    private ArrayList<MenuItemModel> menus;

    public ArrayList<MenuItemModel> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<MenuItemModel> menus) {
        this.menus = menus;
    }

    public MenuListDataModel(ArrayList<MenuItemModel> menus) {
        this.menus = menus;
    }
}

