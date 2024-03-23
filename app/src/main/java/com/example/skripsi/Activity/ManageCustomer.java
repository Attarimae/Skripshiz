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
import com.example.skripsi.Adapter.ManageCustomerAdapter;
import com.example.skripsi.Model.Customer.CustomerItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCustomer extends AppCompatActivity {


    private ArrayList<CustomerItemModel> customerList;
    private ArrayList<CustomerItemModel> filteredCustomerList;
    private ArrayList<CustomerItemModel> storedCustomerList;

    private ManageCustomerAdapter adapter;
    private SearchView searchView;
    private TextView textViewNotFound,toolbarManageCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        GridView menuGrid = findViewById(R.id.FR_gridManageCustomer);
        ImageView imageView = findViewById(R.id.btn_add_menu);
        imageView.setVisibility(View.GONE);
        customerList = new ArrayList<>();
        filteredCustomerList = new ArrayList<>();
        storedCustomerList = new ArrayList<>(); // Initialize the stored menu data list
        adapter = new ManageCustomerAdapter(this, filteredCustomerList);
        menuGrid.setAdapter(adapter);

        textViewNotFound = findViewById(R.id.text_view_not_found);
        toolbarManageCustomer = findViewById(R.id.manage_menu);
        toolbarManageCustomer.setText("Manage Customer");
        fetchCustomerData();

        // Setup search view
        searchView = findViewById(R.id.FT_searchViewManageMenu);
        searchView.setQueryHint("Search Customer By Name");
        setupSearchView();
    }

    private void fetchCustomerData() {
        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<CustomerItemModel>> call = service.getApiService(this).getAllCustomer();
        call.enqueue(new Callback<ArrayList<CustomerItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CustomerItemModel>> call, Response<ArrayList<CustomerItemModel>> response) {
                if (response.isSuccessful()) {
                    List<CustomerItemModel> customerItems = response.body();
                    if (customerItems != null) {
                        customerList.addAll(customerItems);
                        storedCustomerList.addAll(customerItems); // Store the fetched menu data
                        filteredCustomerList.addAll(customerItems);
                        adapter.notifyDataSetChanged();
                        checkIfEmpty();
                    }
                } else {
                    textViewNotFound.setVisibility(View.VISIBLE);
                    textViewNotFound.setText("No Customer Found");
                    Log.e("ManageMenuActivity", "Failed to fetch Customer data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CustomerItemModel>> call, Throwable t) {
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
            filteredCustomerList.clear();
            filteredCustomerList.addAll(storedCustomerList); // Perform search on the stored menu data
        } else {
            // Filter the menu items based on the search query
            filteredCustomerList.clear();
            for (CustomerItemModel customerItem : storedCustomerList) { // Perform search on the stored menu data
                if (customerItem.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredCustomerList.add(customerItem);
                }
            }
        }

        adapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (filteredCustomerList.isEmpty()) {
            textViewNotFound.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.GONE);
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(ManageCustomer.this, ManagerMainActivity.class);
        startActivity(intent);
        finish();
    }
}
