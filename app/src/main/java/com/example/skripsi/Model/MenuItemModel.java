package com.example.skripsi.Model;

public class MenuItemModel {

    private String menuName, menuPrice;
    private int imgID;

    public MenuItemModel(String menuName, String menuPrice, int imgID) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.imgID = imgID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
}
