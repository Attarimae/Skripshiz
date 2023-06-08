package com.example.skripsi.API;

import com.example.skripsi.Model.RestaurantDataModel;
import com.example.skripsi.Model.StaffDataModel;

import retrofit2.Call;
import retrofit2.http.Body;
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
    //blom kubuat getAllStaffnya

    //Update Restaurant
    //@POST("/staff/users/")
    //

    //Create Menu
    //@POST("/menu/")
    //

    //Get All Menu
    //@GET("/menu/")
    //

    //Order
    //@POST("/order/")
    //

    //Update Order
    //@PATCH("/order/")
    //Call<OrderDataModel> updateOrder();
    //blom kubuat modelnya

    //Get Menu by Category
    //@GET("/menu")
    //Call<MenuDataModel> getMenuCategory(@Query("category") String category);
    //ini juga blom

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
