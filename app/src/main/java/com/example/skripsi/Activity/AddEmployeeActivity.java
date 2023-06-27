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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.Model.Employee.EmployeeItemModelWithoutId;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.StaffDataModel;
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

public class AddEmployeeActivity extends AppCompatActivity {
    private TextView employeeNameTextView, manageEmployeeTextView, employeePasswordTextView;
    private TextView employeeEmailTextView, employee_number;
    private ImageView employeeImageView;
    private Spinner employeeCategorySpinner;
    private int employeeId;

    private String employeeStaffId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);
        sm = new SessionManager(this);
        employeeNameTextView = findViewById(R.id.employee_name_textview);
        employeeEmailTextView = findViewById(R.id.employee_email_textview);
        employeeImageView = findViewById(R.id.employee_imageview);
        employeeCategorySpinner = findViewById(R.id.employee_role_spinner);
        employee_number = findViewById(R.id.employee_number_textview);
        employeePasswordTextView = findViewById(R.id.employee_password_textview);

        setEmployeeRoleView();

        manageEmployeeTextView = findViewById(R.id.manage_menu);
        manageEmployeeTextView.setText("Add New Employee");

        employeeNameTextView.setText("");
        employeeEmailTextView.setText("");
        employee_number.setText("");
        employeePasswordTextView.setText("");
    }

    private void setEmployeeRoleView() {
        List<String> role = new ArrayList<>();
        role.add("Cashier");
        role.add("Manager");
        // Populate category spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEmployeeActivity.this, android.R.layout.simple_spinner_item, role);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeCategorySpinner.setAdapter(adapter);
    }


    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(AddEmployeeActivity.this, ManageEmployeeActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }

    public void onSaveButtonClicked(View view) {
        // Get the references to the EditText fields
        EditText nameEditText = findViewById(R.id.employee_name_textview);
        EditText emailEditText = findViewById(R.id.employee_email_textview);
        EditText phoneEditText = findViewById(R.id.employee_number_textview);
        EditText passwordEditText = findViewById(R.id.employee_password_textview);
        Spinner employeeCategorySpinner = findViewById(R.id.employee_role_spinner);
        ImageView imageView = findViewById(R.id.employee_imageview);

        // Get the values from the EditText fields
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String employeeCategory = employeeCategorySpinner.getSelectedItem().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

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

        if (password.isEmpty()) {
            passwordEditText.setError("Password cannot be empty");
            return;
        }
        //this empty because imgid is set when image is sent
        StaffDataModel apiModel = new StaffDataModel(name, email, phone, password, employeeCategory);
        sendToApi(apiModel);
    }

    private void sendImageToAPI(EmployeeItemModel apiModel, ImageView imageView) {
        //sendToApi(apiModel);
        Uri imageUri = getImageUri(imageView);
        if (imageUri != null) {
            // Convert the image URI to a File object
            File imageFile = new File(imageUri.getPath());

            // Create the request body for the image file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

            // Create the other request parameters
            RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "employee");
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), apiModel.getStaffId());
            RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

            // Make the upload request
            ServiceGenerator service = new ServiceGenerator();
            Call<ResponseBody> call = service.getApiService(this).uploadFile(filePart, typePart, namePart, authorizationPart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Image uploaded successfully
                        Toast.makeText(AddEmployeeActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        Toast.makeText(AddEmployeeActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle network or other errors
                    Toast.makeText(AddEmployeeActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, continue with saving the data to the API
            //sendToApi(apiModel);
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

    private void sendToApi(StaffDataModel dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        Call<EmployeeItemModel> call = service.getApiService(this).postCreateStaff(dataModel);
        call.enqueue(new Callback<EmployeeItemModel>() {
            @Override
            public void onResponse(Call<EmployeeItemModel> call, Response<EmployeeItemModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployeeActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddEmployeeActivity.this, ManageEmployeeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(AddEmployeeActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddEmployeeActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmployeeItemModel> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(AddEmployeeActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        EmployeeItemModelWithoutId apiModel = new EmployeeItemModelWithoutId(
//                dataModel.getStaffId(),
//                dataModel.getStaffName(),
//                dataModel.getEmail(),
//                dataModel.getPhoneNumber(),
//                dataModel.getRole(),
//                dataModel.getImgID()
//        );
//        Call<EmployeeItemModelWithoutId> call = service.getApiService(this).postCreateStaff(apiModel);
//        call.enqueue(new Callback<EmployeeItemModelWithoutId>() {
//            @Override
//            public void onResponse(Call<EmployeeItemModelWithoutId> call, Response<EmployeeItemModelWithoutId> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(AddEmployeeActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddEmployeeActivity.this, ManageEmployeeActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if (response.code() == 400) {
//                    try {
//                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
//                        String responseMessage = errorResponse.getResponseMessage();
//                        Toast.makeText(AddEmployeeActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Toast.makeText(AddEmployeeActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(AddEmployeeActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EmployeeItemModelWithoutId> call, Throwable t) {
//                // Handle API call failure
//                Toast.makeText(AddEmployeeActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
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
                    Glide.get(AddEmployeeActivity.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(AddEmployeeActivity.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (which == 1) {
                    // Camera option clicked
                    // Clear Glide cache before capturing a new image
                    Glide.get(AddEmployeeActivity.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(AddEmployeeActivity.this).clearDiskCache();
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
                    .into(employeeImageView);
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
            employeeImageView.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        }
    }
}

