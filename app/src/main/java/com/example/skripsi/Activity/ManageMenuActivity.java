package com.example.skripsi.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.ManageMenuAdapter;
import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuActivity extends AppCompatActivity {

    private ArrayList<MenuItemModel> menuList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        GridView menuGrid = findViewById(R.id.FR_gridMenu);
        menuList = new ArrayList<>();

        ManageMenuAdapter adapter = new ManageMenuAdapter(this, menuList);
        menuGrid.setAdapter(adapter);

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<MenuItemModel>> call = service.getApiService(this).getAllMenu();
        call.enqueue(new Callback<ArrayList<MenuItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItemModel>> call, Response<ArrayList<MenuItemModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MenuItemModel> menuItems = response.body();
                    if (menuItems != null) {
                        menuList.addAll(menuItems);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("ManageMenuActivity", "Failed to fetch menu data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItemModel>> call, Throwable t) {
                Log.e("ManageMenuActivity", t.getMessage());
            }
        });
    }
}
