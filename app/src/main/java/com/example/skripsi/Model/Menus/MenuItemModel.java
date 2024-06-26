package com.example.skripsi.Model.Menus;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuItemModel implements Serializable {

    @SerializedName(value="id")
    private int id;
    @SerializedName(value="category_name", alternate={"category","categoryName"})
    private String menuCategory;
    @SerializedName("menu_name")
    private String menuName;
    @SerializedName("price")
    private String menuPrice;
    @SerializedName("description")
    private String menuDescription;

    @SerializedName("picture_code")
    private String imgID; //R.drawable pake int soalny
    //Display
    public MenuItemModel(String menuName, String menuPrice, String imgID,int id) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.imgID = imgID;
        this.id = id;
    }

    //POST ke Endpoint
    public MenuItemModel(int id,String menuCategory, String menuName, String menuDescription, String menuPrice, String imgID){
        this.id = id;
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.menuPrice = menuPrice;
        this.imgID = imgID;
    }

    public MenuItemModel(String menuCategory, String menuName, String menuDescription, String menuPrice, String imgID){
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

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
