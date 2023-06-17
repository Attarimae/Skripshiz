package com.example.skripsi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Model.CategoryList;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMenuActivity extends AppCompatActivity {
    private TextView menuNameTextView,manageMenuTextView;
    private TextView menuPriceTextView,menuDescipritionTextView;
    private ImageView menuImageView;
    private Spinner menuCategorySpinner;
    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        menuNameTextView = findViewById(R.id.menu_name_textview);
        menuPriceTextView = findViewById(R.id.menu_price_textview);
        menuImageView = findViewById(R.id.menu_imageview);
        menuDescipritionTextView = findViewById(R.id.menu_description_textview);
        fetchCategoriesFromApi();

        manageMenuTextView = findViewById(R.id.manage_menu);
        Bundle extras = getIntent().getExtras();
        MenuItemModel menuItem = (MenuItemModel) getIntent().getSerializableExtra("menu");
        if (menuItem != null) {
            menuId = menuItem.getId();
        }
        menuCategorySpinner = findViewById(R.id.menu_category_spinner);

        if (menuItem != null) {
            System.out.println(menuItem.getImgID());

            manageMenuTextView.setText(menuItem.getMenuName());
            menuNameTextView.setText(menuItem.getMenuName());
            menuPriceTextView.setText(menuItem.getMenuPrice());
            menuImageView.setImageResource(menuItem.getImgID());
            menuDescipritionTextView.setText(menuItem.getMenuDescription());
        } else {
            // Handle the case when the menu item is not found
            Toast.makeText(this, "Invalid menu item", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the menu item is not valid
        }
    }

    private void fetchCategoriesFromApi() {
        ServiceGenerator service = new ServiceGenerator();
        Call<List<CategoryList>> categoryCall= service.getApiService(this).getCategory();

        categoryCall.enqueue(new Callback<List<CategoryList>>() {
            @Override
            public void onResponse(Call<List<CategoryList>> call, Response<List<CategoryList>> response) {
                if (response.isSuccessful()) {
                    List<CategoryList> categories = response.body();

                    List<String> categoryNames = new ArrayList<>();
                    for (CategoryList category : categories) {
                        categoryNames.add(category.getCategoryName());
                    }

                    // Populate category spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DetailMenuActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    menuCategorySpinner.setAdapter(adapter);
                } else {
                    // Handle API error
                    Toast.makeText(DetailMenuActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryList>> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(DetailMenuActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(DetailMenuActivity.this, ManageMenuActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }

    public void onSaveButtonClicked(View view) {
        // Get the references to the EditText fields
        EditText nameEditText = findViewById(R.id.menu_name_textview);
        EditText priceEditText = findViewById(R.id.menu_price_textview);
        EditText descriptionEditText = findViewById(R.id.menu_description_textview);
        Spinner menuCategorySpinner = findViewById(R.id.menu_category_spinner);

        // Get the values from the EditText fields
        String name = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String menuCategory = menuCategorySpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString();

        if (name.isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            return;
        }

        if (price.isEmpty()) {
            priceEditText.setError("Price cannot be empty");
            return;
        }

        if (!TextUtils.isDigitsOnly(price)) {
            priceEditText.setError("Price should be a valid number");
            return;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError("Description cannot be empty");
            return;
        }

        MenuItemModel apiModel = new MenuItemModel(menuId,menuCategory,name,description,price,1);
        sendToApi(apiModel);

    }

    private void sendToApi(MenuItemModel dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        Call<MenuItemModel> call = service.getApiService(this).postCreateMenu(dataModel);
        call.enqueue(new Callback<MenuItemModel>() {
            @Override
            public void onResponse(Call<MenuItemModel> call, Response<MenuItemModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailMenuActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailMenuActivity.this, ManageMenuActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(DetailMenuActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailMenuActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailMenuActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MenuItemModel> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(DetailMenuActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

