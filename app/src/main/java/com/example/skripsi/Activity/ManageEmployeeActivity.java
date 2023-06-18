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
import com.example.skripsi.Adapter.ManageEmployeeAdapter;
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageEmployeeActivity extends AppCompatActivity {

    private ArrayList<EmployeeItemModel> employeeList;
    private ArrayList<EmployeeItemModel> filteredEmployeeList;
    private ArrayList<EmployeeItemModel> storedEmployeeList;

    private ManageEmployeeAdapter adapter;
    private SearchView searchView;
    private TextView textViewNotFound,toolbarManageEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);

        GridView menuGrid = findViewById(R.id.FR_gridManageEmployee);
        ImageView imageView = findViewById(R.id.btn_add_menu);
        imageView.setVisibility(View.GONE);
        employeeList = new ArrayList<>();
        filteredEmployeeList = new ArrayList<>();
        storedEmployeeList = new ArrayList<>(); // Initialize the stored menu data list
        adapter = new ManageEmployeeAdapter(this, filteredEmployeeList);
        menuGrid.setAdapter(adapter);

        textViewNotFound = findViewById(R.id.text_view_not_found);
        toolbarManageEmployee = findViewById(R.id.manage_menu);
        toolbarManageEmployee.setText("Manage Employee");
        fetchEmployeeData();

        // Setup search view
        searchView = findViewById(R.id.FT_searchViewManageMenu);
        setupSearchView();
    }

    private void fetchEmployeeData() {
        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<EmployeeItemModel>> call = service.getApiService(this).getAllStaff();
        call.enqueue(new Callback<ArrayList<EmployeeItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeItemModel>> call, Response<ArrayList<EmployeeItemModel>> response) {
                if (response.isSuccessful()) {
                    List<EmployeeItemModel> employeeItems = response.body();
                    if (employeeItems != null) {
                        employeeList.addAll(employeeItems);
                        storedEmployeeList.addAll(employeeItems); // Store the fetched menu data
                        filteredEmployeeList.addAll(employeeItems);
                        adapter.notifyDataSetChanged();
                        checkIfEmpty();
                    }
                } else {
                    Log.e("ManageMenuActivity", "Failed to fetch menu data");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeItemModel>> call, Throwable t) {
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
            filteredEmployeeList.clear();
            filteredEmployeeList.addAll(storedEmployeeList); // Perform search on the stored menu data
        } else {
            // Filter the menu items based on the search query
            filteredEmployeeList.clear();
            for (EmployeeItemModel employeeItem : storedEmployeeList) { // Perform search on the stored menu data
                if (employeeItem.getStaffName().toLowerCase().contains(query.toLowerCase())) {
                    filteredEmployeeList.add(employeeItem);
                }
            }
        }

        adapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (filteredEmployeeList.isEmpty()) {
            textViewNotFound.setVisibility(View.VISIBLE);
        } else {
            textViewNotFound.setVisibility(View.GONE);
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(ManageEmployeeActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish();
    }
}
