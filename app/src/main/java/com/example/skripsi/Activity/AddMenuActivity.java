package com.example.skripsi.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.CategoryList;
import com.example.skripsi.Model.CategoryPost;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.Model.Menus.MenuItemModelWithoutId;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenuActivity extends AppCompatActivity {
    private TextView menuNameTextView, manageMenuTextView;
    private TextView menuPriceTextView, menuDescipritionTextView;
    private ImageView menuImageView;
    private Spinner menuCategorySpinner;
    private int menuId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    private PopupWindow categoryPopupWindow;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_menu);
        sm = new SessionManager(this);
        menuNameTextView = findViewById(R.id.menu_name_textview);
        menuPriceTextView = findViewById(R.id.menu_price_textview);
        menuImageView = findViewById(R.id.menu_imageview);
        menuDescipritionTextView = findViewById(R.id.menu_description_textview);
        Button addButton = findViewById(R.id.add_category);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_add_category, null);

                categoryPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                categoryPopupWindow.setFocusable(true);

                categoryPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                Button saveButton = popupView.findViewById(R.id.save_button);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText categoryNameEditText = popupView.findViewById(R.id.category_name_edittext);
                        String categoryName = categoryNameEditText.getText().toString();

                        ServiceGenerator service = new ServiceGenerator();
                        CategoryPost categoryPost = new CategoryPost(categoryName);
                        Call<CategoryList> categoryCall = service.getApiService(AddMenuActivity.this).postCategory(categoryPost);

                        categoryCall.enqueue(new Callback<CategoryList>() {
                            @Override
                            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(AddMenuActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                    categoryPopupWindow.dismiss(); // Close the popup window
                                    Intent intent = new Intent(AddMenuActivity.this, AddMenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (response.code() == 400) {
                                    try {
                                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                                        String responseMessage = errorResponse.getResponseMessage();
                                        Toast.makeText(AddMenuActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(AddMenuActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AddMenuActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CategoryList> call, Throwable t) {
                                // Handle API call failure
                                Toast.makeText(AddMenuActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        manageMenuTextView = findViewById(R.id.manage_menu);
        manageMenuTextView.setText("Add New Menu");

        menuCategorySpinner = findViewById(R.id.menu_category_spinner);
        menuDescipritionTextView.setText("");
        menuNameTextView.setText("");
        menuPriceTextView.setText("");

        fetchCategoriesFromApi();
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(AddMenuActivity.this, ManageMenuActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }

    private void fetchCategoriesFromApi() {
        ServiceGenerator service = new ServiceGenerator();
        Call<List<CategoryList>> categoryCall = service.getApiService(this).getCategory();

        categoryCall.enqueue(new Callback<List<CategoryList>>() {
            @Override
            public void onResponse(Call<List<CategoryList>> call, Response<List<CategoryList>> response) {
                if (response.isSuccessful()) {
                    List<CategoryList> categories = response.body();

                    List<String> categoryNames = new ArrayList<>();
                    categoryNames.add("ALL");
                    for (CategoryList category : categories) {
                        categoryNames.add(category.getCategoryName());
                    }

                    // Populate category spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMenuActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    menuCategorySpinner.setAdapter(adapter);
                } else {
                    // Handle API error
                    Toast.makeText(AddMenuActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryList>> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(AddMenuActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSaveButtonClicked(View view) {
        // Get the references to the EditText fields
        EditText nameEditText = findViewById(R.id.menu_name_textview);
        EditText priceEditText = findViewById(R.id.menu_price_textview);
        EditText descriptionEditText = findViewById(R.id.menu_description_textview);
        Spinner menuCategorySpinner = findViewById(R.id.menu_category_spinner);
        ImageView imageView = findViewById(R.id.menu_imageview);

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

        MenuItemModel apiModel = new MenuItemModel(menuCategory, name, description, price, "1");
        sendImageToAPI(apiModel, imageView);
    }

    private void sendImageToAPI(MenuItemModel apiModel, ImageView imageView) {
        sendToApi(apiModel);
        Uri imageUri = getImageUri(imageView);
        if (imageUri != null) {
            // Convert the image URI to a File object
            File imageFile = new File(imageUri.getPath());

            // Create the request body for the image file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

            // Create the other request parameters
            RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "menu");
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), apiModel.getMenuName());
            RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

            // Make the upload request
            ServiceGenerator service = new ServiceGenerator();
            Call<ResponseBody> call = service.getApiService(this).uploadFile(filePart, typePart, namePart, authorizationPart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Image uploaded successfully
                        Toast.makeText(AddMenuActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        Toast.makeText(AddMenuActivity.this, "Failed to upload image, Reason: {}", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle network or other errors
                    Toast.makeText(AddMenuActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, continue with saving the data to the API
            sendToApi(apiModel);
        }
    }


    private Uri getImageUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return getImageUriFromBitmap(bitmap);
        }
        return null;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream outputStream = new FileOutputStream(cachePath + "/image.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return Uri.fromFile(new File(cachePath + "/image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendToApi(MenuItemModel dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        MenuItemModelWithoutId apiModel = new MenuItemModelWithoutId(
                dataModel.getMenuCategory(),
                dataModel.getMenuName(),
                dataModel.getMenuDescription(),
                dataModel.getMenuPrice(),
                dataModel.getImgID()
        );
        Call<MenuItemModel> call = service.getApiService(this).postCreateMenu(apiModel);
        call.enqueue(new Callback<MenuItemModel>() {
            @Override
            public void onResponse(Call<MenuItemModel> call, Response<MenuItemModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddMenuActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddMenuActivity.this, ManageMenuActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(AddMenuActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddMenuActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddMenuActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MenuItemModel> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(AddMenuActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onImageClicked(View view) {
        // Show options for image selection (gallery or camera)
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Gallery option clicked
                    // Clear Glide cache before selecting a new image
                    Glide.get(AddMenuActivity.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(AddMenuActivity.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (which == 1) {
                    // Camera option clicked
                    // Clear Glide cache before capturing a new image
                    Glide.get(AddMenuActivity.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(AddMenuActivity.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Image selected from gallery
            Uri imageUri = data.getData();
            // Process the selected image Uri as per your requirement

            // For example, you can display the selected image in the ImageView
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(menuImageView);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        } else if (requestCode == CAMERA_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Image captured from camera
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Process the captured image Bitmap as per your requirement

            // For example, you can display the captured image in the ImageView
            menuImageView.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        }
    }
}

