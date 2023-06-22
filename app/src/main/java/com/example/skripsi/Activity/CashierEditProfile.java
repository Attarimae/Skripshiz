package com.example.skripsi.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.R;

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

public class CashierEditProfile extends AppCompatActivity {

    ImageView editProfilePicImages;
    SessionManager sm;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_edit_profile);
        sm = new SessionManager(this);
        editProfilePicImages = findViewById(R.id.cashier_editprofile_Images);

        Glide.with(this)
                .load(APIConstant.BASE_URL_DOWNLOAD + sm.fetchRestaurantID() + "_" + sm.fetchStaffID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.default_profile)
                .into(editProfilePicImages);

        editProfilePicImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });

        TextView editProfilePicName = findViewById(R.id.cashier_editprofile_ViewUserName);
        editProfilePicName.setText(sm.fetchStaffName());
        TextView editProfilePicRole = findViewById(R.id.cashier_editprofile_UserRole);
        editProfilePicRole.setText(sm.fetchStaffRole());

        Button editProfilePicSaveButton = findViewById(R.id.cashier_editprofile_SubmitButton);

        editProfilePicSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri profilepictureUri = getProfilePictureUri(editProfilePicImages);
                if(profilepictureUri != null){
                    File profilepictureFile = new File(profilepictureUri.getPath());

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), profilepictureFile);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", profilepictureFile.getName(), requestFile);

                    RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "employee");
                    RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchStaffID());
                    RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

                    ServiceGenerator service = new ServiceGenerator();
                    Call<ResponseBody> call = service.getApiService(getApplicationContext()).uploadFile(filePart, typePart, namePart, authorizationPart);
                    call.enqueue(new Callback<ResponseBody>() {
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        public void onFailure(Call<ResponseBody> call, Throwable t){
                            Toast.makeText(getApplicationContext(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Images uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(this, CashierSettingActivity.class);
        startActivity(intent);
        finish();
    }

    private void selectImages(){
        //Select from Camera/Gallery
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Gallery option clicked
                    // Clear Glide cache before selecting a new image
                    Glide.get(CashierEditProfile.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(CashierEditProfile.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (which == 1) {
                    // Camera option clicked
                    // Clear Glide cache before capturing a new image
                    Glide.get(CashierEditProfile.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(CashierEditProfile.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    .into(editProfilePicImages);
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
            editProfilePicImages.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        }
    }

    private Uri getProfilePictureUri(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return getProfilePictureUriFromBitmap(bitmap);
        }
        return null;
    }

    private Uri getProfilePictureUriFromBitmap(Bitmap bitmap){
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
}