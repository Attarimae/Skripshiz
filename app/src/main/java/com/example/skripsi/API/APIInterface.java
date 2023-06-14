package com.example.skripsi.API;

import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.RestaurantDataModel;
import com.example.skripsi.Model.StaffDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface APIInterface {
    //Register Restaurant
    @POST("/restaurant/register/")
    Call<RestaurantDataModel> postRestaurantRegister(@Body RestaurantDataModel restaurantDataModel);

    //Login Restaurant
    @POST("/restaurant/login/")
    Call<RestaurantDataModel> postRestaurantLogin(@Body RestaurantDataModel restaurantDataModel);

    //Register Staff
    @POST("/staff/register/")
    Call<StaffDataModel> postStaffRegister(@Body StaffDataModel staffDataModel);

    //Login Staff
    @POST("/staff/Login/")
    Call<StaffDataModel> postStaffLogin(@Body StaffDataModel staffDataModel);

    //Get All Staff
    //@GET("/staff/users/")
    //Call<StaffDataModel> getAllStaff();
    //blm kubuat bisa pake API ini

    //Update Employee
    //@POST("/staff/users/")
    //

    //Create Menu
    //@POST("/menu/")
    //Call<MenuItemModel> postCreateMenu(@Body MenuItemModel menuItemModel);
    //blm kutest

    //Get All Menu
    @GET("/menu/")
    Call<ArrayList<MenuItemModel>> getAllMenu();
    //blm kubuat bisa pake API ini

    //Get Order
    @GET("/order")
    Call<ArrayList<OrderListItemDataModel>> getAllOrderList();
    //model baru, blm kutest

    //Order
    @POST("/order/")
    Call<OrderListItemDataModel> postCreateOrder(@Body OrderListItemDataModel orderListItemDataModel);
    //blm kutest

    //Update Order
    //@PATCH("/order/")
    //Call<OrderDataModel> updateOrder();
    //masih blm kubuat ini

    //Get Menu by Category
    //@GET("/menu")
    //Call<MenuDataModel> getMenuCategory(@Query("category") String category);
    //blm kubuat bisa pake API ini, mungkin perlu kuupdate jg ini

    //Upload Photo
    //@POST("/upload/")
    //

    //Delete Photo
    //@DELETE("/delete/")
    //

    //Get Photo
    //@GET("/download/")
    //
}
