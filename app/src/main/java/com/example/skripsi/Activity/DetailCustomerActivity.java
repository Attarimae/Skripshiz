package com.example.skripsi.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.Customer.CustomerItemModel;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCustomerActivity extends AppCompatActivity {
    private TextView customerNameTextView, manageCustomerTextView;
    private TextView customerEmailTextView, customer_number;
    private int customerId;

    private String customerUserId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);
        sm = new SessionManager(this);
        customerNameTextView = findViewById(R.id.customer_name_textview);
        customerEmailTextView = findViewById(R.id.customer_email_textview);
        customer_number = findViewById(R.id.customer_number_textview);
        manageCustomerTextView = findViewById(R.id.manage_menu);
        Bundle extras = getIntent().getExtras();
        CustomerItemModel customerItem = (CustomerItemModel) getIntent().getSerializableExtra("customer");
        if (customerItem != null) {
            customerId = customerItem.getId();
            customerUserId = customerItem.getUserId();
        }

        if (customerItem != null) {

            manageCustomerTextView.setText(customerItem.getName());
            customerNameTextView.setText(customerItem.getName());
            customerEmailTextView.setText(customerItem.getEmail());
            customer_number.setText(customerItem.getPhoneNumber());
        } else {
            // Handle the case when the customer item is not found
            Toast.makeText(this, "Invalid customer item", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the customer item is not valid
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(DetailCustomerActivity.this, ManageCustomer.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }

    public void onSaveButtonClicked(View view) {
        // Get the references to the EditText fields
        EditText nameEditText = findViewById(R.id.customer_name_textview);
        EditText emailEditText = findViewById(R.id.customer_email_textview);
        EditText phoneEditText = findViewById(R.id.customer_number_textview);

        // Get the values from the EditText fields
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (name.isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email cannot be empty");
            return;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Phone cannot be empty");
            return;
        }
        //this empty because imgid is set when image is sent
        CustomerItemModel apiModel = new CustomerItemModel(customerId, customerUserId, name, email, phone);
        sendToApi(apiModel);
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

    private void sendToApi(CustomerItemModel dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        Call<CustomerItemModel> call = service.getApiService(this).postCustomer(dataModel);
        call.enqueue(new Callback<CustomerItemModel>() {
            @Override
            public void onResponse(Call<CustomerItemModel> call, Response<CustomerItemModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailCustomerActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailCustomerActivity.this, ManageCustomer.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(DetailCustomerActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailCustomerActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailCustomerActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerItemModel> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(DetailCustomerActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

