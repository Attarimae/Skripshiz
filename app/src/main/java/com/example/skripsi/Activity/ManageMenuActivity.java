package com.example.skripsi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.ManageMenuAdapter;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuActivity extends AppCompatActivity {

    private ArrayList<MenuItemModel> menuList;
    private ArrayList<MenuItemModel> filteredMenuList;
    private ArrayList<MenuItemModel> storedMenuList; // Store the fetched menu data
    private ManageMenuAdapter adapter;
    private SearchView searchView;
    private TextView textViewNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        GridView menuGrid = findViewById(R.id.FR_gridManageMenu);
        ImageView imageView = findViewById(R.id.btn_add_menu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageMenuActivity.this, AddMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


        menuList = new ArrayList<>();
        filteredMenuList = new ArrayList<>();
        storedMenuList = new ArrayList<>(); // Initialize the stored menu data list
        adapter = new ManageMenuAdapter(this, filteredMenuList);
        menuGrid.setAdapter(adapter);

        textViewNotFound = findViewById(R.id.text_view_not_found);

        // Fetch menu data from API
        fetchMenuData();

        // Setup search view
        searchView = findViewById(R.id.FT_searchViewManageMenu);
        setupSearchView();
    }

    private void fetchMenuData() {
        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<MenuItemModel>> call = service.getApiService(this).getAllMenu();
        call.enqueue(new Callback<ArrayList<MenuItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItemModel>> call, Response<ArrayList<MenuItemModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MenuItemModel> menuItems = response.body();
                    if (menuItems != null) {
                        menuList.addAll(menuItems);
                        storedMenuList.addAll(menuItems); // Store the fetched menu data
                        filteredMenuList.addAll(menuItems);
                        adapter.notifyDataSetChanged();
                        checkIfEmpty();
                    }
                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                    textViewNotFound.setText("No Menu Found");
                    Log.e("ManageMenuActivity", "Failed to fetch menu data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItemModel>> call, Throwable t) {
                Log.e("ManageMenuActivity", t.getMessage());
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submit (e.g., perform search)
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text change (e.g., perform search as the user types)
                performSearch(newText);
                return false;
            }
        });
    }

    private void performSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            // Show all menu items if the query is empty
            filteredMenuList.clear();
            filteredMenuList.addAll(storedMenuList); // Perform search on the stored menu data
        } else {
            // Filter the menu items based on the search query
            filteredMenuList.clear();
            for (MenuItemModel menuItem : storedMenuList) { // Perform search on the stored menu data
                if (menuItem.getMenuName().toLowerCase().contains(query.toLowerCase())) {
                    filteredMenuList.add(menuItem);
                }
            }
        }

        adapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (filteredMenuList.isEmpty()) {
            textViewNotFound.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.GONE);
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(ManageMenuActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }
}