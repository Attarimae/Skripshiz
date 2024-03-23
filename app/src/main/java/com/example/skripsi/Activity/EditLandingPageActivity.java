package com.example.skripsi.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Activity.Fragment.OrderListFragment;
import com.example.skripsi.Model.Customer.CustomerItemModel;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.FullProfile;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLandingPageActivity extends AppCompatActivity {


    private TextView motto, mottoDetail, restaurantStory, openDay,
            openTime, Location, officialEmail, facebookUrl, instagramUrl, whatsappNumber,manage_menu;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_landing_page);
        sm = new SessionManager(this);
        motto = findViewById(R.id.profile_motto_textview);
        mottoDetail = findViewById(R.id.profile_motto_detail_textview);
        restaurantStory = findViewById(R.id.profile_restaurant_story_textview);
        openDay = findViewById(R.id.profile_open_day_textview);
        openTime = findViewById(R.id.profile_open_time_textview);
        Location = findViewById(R.id.profile_location_textview);
        officialEmail = findViewById(R.id.official_email_textview);
        facebookUrl = findViewById(R.id.facebook_url_textview);
        instagramUrl = findViewById(R.id.instagram_url_textview);
        whatsappNumber = findViewById(R.id.whatsapp_number_textview);
        manage_menu = findViewById(R.id.manage_menu);
        manage_menu.setText("Edit Landing Page");
        setDataFromAPI();
    }

    public void setDataFromAPI(){
        ServiceGenerator service = new ServiceGenerator();
        Call<FullProfile> call = service.getApiService(this).getRestaurantProfile();
        call.enqueue(new Callback<FullProfile>() {
            @Override
            public void onResponse(Call<FullProfile> call, Response<FullProfile> response) {
                if (response.isSuccessful()) {
                    FullProfile fullProfile = response.body();
                    if (fullProfile != null) {
                        motto.setText(fullProfile.getMotto() != null ? fullProfile.getMotto() : "");
                        mottoDetail.setText(fullProfile.getMottoDetail() != null ? fullProfile.getMottoDetail() : "");
                        restaurantStory.setText(fullProfile.getRestaurantStory() != null ? fullProfile.getRestaurantStory() : "");
                        openDay.setText(fullProfile.getOpenDay() != null ? fullProfile.getOpenDay() : "");
                        openTime.setText(fullProfile.getOpenTime() != null ? fullProfile.getOpenTime() : "");
                        Location.setText(fullProfile.getLocation() != null ? fullProfile.getLocation() : "");
                        officialEmail.setText(fullProfile.getOfficialEmail() != null ? fullProfile.getOfficialEmail() : "");
                        facebookUrl.setText(fullProfile.getFacebookUrl() != null ? fullProfile.getFacebookUrl() : "");
                        instagramUrl.setText(fullProfile.getInstagramUrl() != null ? fullProfile.getInstagramUrl() : "");
                        whatsappNumber.setText(fullProfile.getWhatsappNumber() != null ? fullProfile.getWhatsappNumber() : "");
                    }
                } else {
                    Log.e("Fragment Checkout", "API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FullProfile> call, Throwable t) {
                Log.e("Fragment Checkout", "onFailure: Failed Create Order");
                Log.e("Fragment Checkout", t.getMessage());
            }
        });
    }
    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(EditLandingPageActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }

    public void onSaveButtonClicked(View view) {
        // motto, mottoDetail, restaurantStory, openDay,
        //            openTime, Location, officialEmail, facebookUrl, instagramUrl, whatsappNumber;
        EditText mottoText = findViewById(R.id.profile_motto_textview);
        EditText mottoDetailText = findViewById(R.id.profile_motto_detail_textview);
        EditText restaurantStoryText = findViewById(R.id.profile_restaurant_story_textview);
        EditText openDayText = findViewById(R.id.profile_open_day_textview);
        EditText openTimeText = findViewById(R.id.profile_open_time_textview);
        EditText LocationText = findViewById(R.id.profile_location_textview);
        EditText officialEmailText = findViewById(R.id.official_email_textview);
        EditText facebookUrlText = findViewById(R.id.facebook_url_textview);
        EditText instagramUrlText = findViewById(R.id.instagram_url_textview);
        EditText whatsappNumberText = findViewById(R.id.whatsapp_number_textview);

        // Get the values from the EditText fields
        String motto = mottoText.getText().toString();
        String mottoDetail = mottoDetailText.getText().toString();
        String restaurantStory = restaurantStoryText.getText().toString();
        String openDay = openDayText.getText().toString();
        String openTime = openTimeText.getText().toString();
        String location = LocationText.getText().toString();
        String officialEmail = officialEmailText.getText().toString();
        String facebookUrl = facebookUrlText.getText().toString();
        String instagramUrl = instagramUrlText.getText().toString();
        String whatsappNumber = whatsappNumberText.getText().toString();

        FullProfile apiModel = new FullProfile(1,motto,mottoDetail,restaurantStory,openDay,openTime,location,officialEmail,facebookUrl,instagramUrl,whatsappNumber);
        sendToApi(apiModel);
    }

    private void sendToApi(FullProfile dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        Call<FullProfile> call = service.getApiService(this).postProfile(dataModel);
        call.enqueue(new Callback<FullProfile>() {
            @Override
            public void onResponse(Call<FullProfile> call, Response<FullProfile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditLandingPageActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditLandingPageActivity.this, ManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(EditLandingPageActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditLandingPageActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditLandingPageActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FullProfile> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(EditLandingPageActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
