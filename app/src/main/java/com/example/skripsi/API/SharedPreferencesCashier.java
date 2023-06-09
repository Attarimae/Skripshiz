package com.example.skripsi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferencesCashier {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<CheckoutItemModel> checkoutList;

    public SharedPreferencesCashier(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveCheckoutList(ArrayList<CheckoutItemModel> checkoutList){
        Gson gson = new Gson();
        String json = gson.toJson(checkoutList);
        editor.putString("checkoutList", json);
        editor.commit();
    }

    public ArrayList<CheckoutItemModel> getCheckoutList(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("checkoutList", null);
        Type type = new TypeToken<ArrayList<CheckoutItemModel>>() {}.getType();
        checkoutList = gson.fromJson(json, type);
        if(checkoutList == null){
            checkoutList = new ArrayList<>();
        }
        return checkoutList;
    }
}
