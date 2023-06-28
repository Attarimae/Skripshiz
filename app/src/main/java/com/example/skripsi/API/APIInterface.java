package com.example.skripsi.API;

import com.example.skripsi.Model.CategoryList;
import com.example.skripsi.Model.CategoryPost;
import com.example.skripsi.Model.Customer.CustomerItemModel;
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.Model.FullProfile;
import com.example.skripsi.Model.LoginDataModel;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.Model.Menus.MenuItemModelWithoutId;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.UpdateOrderDataModel;
import com.example.skripsi.Model.SalesReport.POSTReportOrder;
import com.example.skripsi.Model.RestaurantDataModel;
import com.example.skripsi.Model.StaffDataModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @POST("/staff/login/")
    Call<LoginDataModel> postStaffLogin(@Body StaffDataModel staffDataModel);

    //Get All Staff
    @GET("/staff/users/")
    Call<ArrayList<EmployeeItemModel>> getAllStaff();
    @GET("/staff/users/{params}")
    Call<ArrayList<EmployeeItemModel>> getStaff(@Path("params") String params);
    @POST("/staff/users/")
    Call<EmployeeItemModel> postStaff(@Body EmployeeItemModel itemModel);

    //Create Menu
    @POST("/menu/")
    Call<MenuItemModel> postCreateMenu(@Body MenuItemModelWithoutId menuItemModel);

    @POST("/menu/")
    Call<MenuItemModel> postUpdateMenu(@Body MenuItemModel menuItemModel);

    @POST("/menu/")
    Call<MenuItemModel> postCreateMenuWithErrorResponse(@Body MenuItemModel dataModel);

    //Get Category
    @GET("/category/")
    Call<List<CategoryList>> getCategory();

    @POST("/category/")
    Call<CategoryList> postCategory(@Body CategoryPost categoryPost);

    //Get All Menu
    @GET("/menu/")
    Call<ArrayList<MenuItemModel>> getAllMenu();

    //Get Order (Left Unused)
    //@GET("/order")
    //Call<ArrayList<OrderListItemDataModel>> getAllOrderList();

    //Get Order Ongoing (Order List)
    @GET("/order/ongoing")
    Call<ArrayList<OrderListItemDataModel>> getOngoingOrderList();

    //Get Order Finished (Order History)
    @GET("/order/finished")
    Call<ArrayList<OrderListItemDataModel>> getFinishedOrderList();

    //Order
    @POST("/order/")
    Call<OrderListItemDataModel> postCreateOrder(@Body OrderListItemDataModel orderListItemDataModel);
    //blm kutest

    //Update Order
    @Headers("Content-Type: application/json")
    @PATCH("/order/")
    Call<OrderListItemDataModel> patchOrderDetails(@Body OrderListItemDataModel orderListItemDataModel);

    //Update Order Detail Status
    @POST("/order-detail/status")
    Call<UpdateOrderDataModel> updateOrderDetailsStatus(@Body UpdateOrderDataModel updateOrderDataModel);

    //Update Order Status
    @POST("/order/status")
    Call<UpdateOrderDataModel> updateOrdersStatus(@Body UpdateOrderDataModel updateOrderDataModel);

    //Get Menu by Category
    //@GET("/menu")
    //Call<ArrayList<MenuItemModel>> getMenubyCategory(@Query("category") String namaCategory);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file,
            @Part("type") RequestBody type,
            @Part("name") RequestBody name,
            @Part("Authorization") RequestBody authorization
    );

    //Delete Photo
    //@DELETE("/delete/")
    //

    @GET("/download/{params}")
    Call<ResponseBody> getPhoto(@Path("params") String params);

    @GET("/customer/users/")
    Call<ArrayList<CustomerItemModel>> getAllCustomer();
    @POST("/customer/users/")
    Call<CustomerItemModel> postCustomer(@Body CustomerItemModel itemModel);

    @GET("/restaurant/profile")
    Call<FullProfile> getRestaurantProfile();
    @POST("/restaurant/profile")
    Call<FullProfile> postProfile(@Body FullProfile itemModel);
    //Report Order
    @POST("/report-order")
    Call<ArrayList<POSTReportOrder>> postReportOrder(@Body POSTReportOrder postReportOrder);
}
