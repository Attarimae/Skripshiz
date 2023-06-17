package com.example.skripsi.Model.Orders;

import com.google.gson.annotations.SerializedName;

public class OrderListItemDetailsDataModel {

    @SerializedName("id")
    private String menuId;
    @SerializedName("menu_name")
    private String menuName;
    @SerializedName("amount")
    private String menuPrice;
    @SerializedName("description")
    private String menuDescription;
    @SerializedName("quantity")
    private int menuQuantity;
    @SerializedName("picture_code")
    private int imgID;
    @SerializedName("status")
    private String status;
    @SerializedName("order_detail_id")
    private String order_detail_id;

    //Display Dummy Data
//    public OrderListItemDetailsDataModel(String menuId, String menuName, String menuPrice, String menuDescription, int menuQuantity, int imgID, String status, String order_detail_id) {
//        this.menuId = menuId;
//        this.menuName = menuName;
//        this.menuPrice = menuPrice;
//        this.menuDescription = menuDescription;
//        this.menuQuantity = menuQuantity;
//        this.imgID = imgID;
//        this.status = status;
//        this.order_detail_id = order_detail_id;
//    }

    //POST ke Endpoint
    public OrderListItemDetailsDataModel(String menuName, String menuPrice, String menuDescription, int menuQuantity, int imgID) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
        this.menuQuantity = menuQuantity;
        this.imgID = imgID;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public int getMenuQuantity() {
        return menuQuantity;
    }

    public void setMenuQuantity(int menuQuantity) {
        this.menuQuantity = menuQuantity;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }
}
