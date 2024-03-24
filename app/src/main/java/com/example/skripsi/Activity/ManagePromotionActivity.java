package com.example.skripsi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.ManagePromotionAdapter;
import com.example.skripsi.Model.Promotion.PromotionItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagePromotionActivity extends AppCompatActivity {

    private ArrayList<PromotionItemModel> promotionList;
    private ArrayList<PromotionItemModel> filteredPromotionList;
    private ArrayList<PromotionItemModel> storedPromotionList; // Store the fetched promotion data

    private ManagePromotionAdapter adapter;
    private SearchView searchView;
    private TextView textViewNotFound, toolbarManagePromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_promotion);

        GridView menuGrid = findViewById(R.id.FR_gridManagePromotion);
        ImageView imageView = findViewById(R.id.btn_add_menu);
        textViewNotFound = findViewById(R.id.text_view_not_found); // Initialize textViewNotFound

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagePromotionActivity.this, AddPromotionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fetchPromotionData();
        promotionList = new ArrayList<>();
        filteredPromotionList = new ArrayList<>();
        storedPromotionList = new ArrayList<>(); // Initialsize the stored menu data list
        adapter = new ManagePromotionAdapter(this, filteredPromotionList);
        menuGrid.setAdapter(adapter);

        toolbarManagePromotion = findViewById(R.id.manage_menu);
        toolbarManagePromotion.setText("Manage Promotion");

        // Setup search view
        searchView = findViewById(R.id.FT_searchViewManageMenu);
        setupSearchView();
    }

    private void fetchPromotionData() {
        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<PromotionItemModel>> call = service.getApiService(this).getPromotion();
        Log.d("NetworkRequest", "URL: " + call.request().url().toString()); // Log the full URL
        Log.d("NetworkRequest", "Headers: " + call.request().headers().toString());
        call.enqueue(new Callback<ArrayList<PromotionItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PromotionItemModel>> call, Response<ArrayList<PromotionItemModel>> response) {
                if (response.isSuccessful()) {
                    List<PromotionItemModel> promotionItems = response.body();
                    if (promotionItems != null) {
                        promotionList.addAll(promotionItems);
                        storedPromotionList.addAll(promotionItems); // Store the fetched menu data
                        filteredPromotionList.addAll(promotionItems);
                        adapter.notifyDataSetChanged();
                        checkIfEmpty();
                    }
                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                    textViewNotFound.setText("No Promotion Found");
                    Log.e("ManageMenuActivity", "Failed to fetch menu data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PromotionItemModel>> call, Throwable t) {
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
            filteredPromotionList.clear();
            filteredPromotionList.addAll(storedPromotionList); // Perform search on the stored menu data
        } else {
            // Filter the menu items based on the search query
            filteredPromotionList.clear();
            for (PromotionItemModel promotionItem : storedPromotionList) { // Perform search on the stored menu data
                if (promotionItem.getPromotionName().toLowerCase().contains(query.toLowerCase())) {
                    filteredPromotionList.add(promotionItem);
                }
            }
        }

        checkIfEmpty();
        adapter.notifyDataSetChanged(); // Move adapter.notifyDataSetChanged() here
    }

    private void checkIfEmpty() {
        if (filteredPromotionList.isEmpty()) {
            textViewNotFound.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.GONE);
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(ManagePromotionActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish();
    }
}
