package com.example.skripsi.API;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.skripsi.R;

public class SessionManager{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void clearSession(){
        editor.clear();
        editor.apply();
    }
    public void saveAuthToken(String authToken){
        editor.putString("AuthToken", authToken);
        editor.commit();
    }

    public String fetchAuthToken(){
        return sharedPreferences.getString("AuthToken", null);
    }

    public void saveRestaurantID(String restaurant_id){
        editor.putString("restaurant_id", restaurant_id);
        editor.commit();
    }

    public String fetchRestaurantID(){
        return sharedPreferences.getString("restaurant_id", null);
    }

    public void saveStaffID(String staff_id){
        editor.putString("staff_id", staff_id);
        editor.commit();
    }

    public String fetchStaffID(){
        return sharedPreferences.getString("staff_id", null);
    }

    public void saveStaffRole(String role){
        editor.putString("role", role);
        editor.commit();
    }

    public String fetchStaffRole(){
        return sharedPreferences.getString("role", null);
    }

    public void saveStaffName(String staff_name){
        editor.putString("staff_name", staff_name);
        editor.commit();
    }

    public String fetchStaffName() {
        return sharedPreferences.getString("staff_name", null);
    }

    public void saveRestaurantName(String restaurant_name){
        editor.putString("restaurant_name", restaurant_name);
        editor.commit();
    }

    public void addCartTotal(){
        int countCart = fetchCartTotal();
        System.out.println(countCart);
        countCart++;
        editor.putInt("cart", countCart);
        editor.commit();
    }

    public int fetchCartTotal(){
        return sharedPreferences.getInt("cart", 0);
    }

    public void resetCartTotal(){
        editor.putInt("cart", 0);
        editor.commit();
    }


    public String fetchRestaurantName() {
        return sharedPreferences.getString("restaurant_name", null);
    }
}
