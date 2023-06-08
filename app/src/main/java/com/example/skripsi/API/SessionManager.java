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
}
