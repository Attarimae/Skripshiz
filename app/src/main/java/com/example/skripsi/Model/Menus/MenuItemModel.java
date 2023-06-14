package com.example.skripsi.Model.Menus;

import com.google.gson.annotations.SerializedName;

public class MenuItemModel {

    @SerializedName(value="categoryName", alternate={"category","category_name"})
    private String menuCategory;
    @SerializedName("menu_name")
    private String menuName;
    @SerializedName("price")
    private String menuPrice;
    @SerializedName("description")
    private String menuDescription;
    @SerializedName("picture_code")
    private int imgID; //R.drawable pake int soalny

    //Display
    public MenuItemModel(String menuName, String menuPrice, int imgID) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.imgID = imgID;
    }

    //POST ke Endpoint
    public MenuItemModel(String menuCategory, String menuName, String menuDescription, String menuPrice, int imgID){
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
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

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }
}