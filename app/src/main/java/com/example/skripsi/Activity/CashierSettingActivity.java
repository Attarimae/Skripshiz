package com.example.skripsi.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.R;

public class CashierSettingActivity extends AppCompatActivity {

    ImageView cashierSettingsPicture;
    Button cashierSettingsEditProfile, cashierSettingsSendFeedback, cashierSettingsHelp, cashierSettingsLogout;
    TextView cashierSettingsName, cashierSettingsRole, cashierSettingsVersion;
    SessionManager sm;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_setting);

        sm = new SessionManager(this);

        cashierSettingsPicture = findViewById(R.id.cashier_settings_picture);

        cashierSettingsEditProfile = findViewById(R.id.cashier_settings_profileButton);
        cashierSettingsSendFeedback = findViewById(R.id.cashier_settings_feedbackButton);
        cashierSettingsHelp = findViewById(R.id.cashier_settings_HelpButton);
        cashierSettingsLogout = findViewById(R.id.cashier_settings_logoutButton);

        cashierSettingsName = findViewById(R.id.cashier_settings_name);
        cashierSettingsRole = findViewById(R.id.cashier_settings_role);
        cashierSettingsVersion = findViewById(R.id.cashier_settings_appVersionText);

        //Using Glide to Display the Profile Picture
        Glide.with(this)
                .load(APIConstant.BASE_URL_DOWNLOAD + sm.fetchRestaurantID() + "_" + sm.fetchStaffID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.default_profile)
                .into(cashierSettingsPicture);

        cashierSettingsName.setText("Name: " + sm.fetchStaffName());
        cashierSettingsRole.setText("Role: " + sm.fetchStaffRole());

        cashierSettingsLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        cashierSettingsEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });

        cashierSettingsSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CashierSettingActivity.this, "Feedback Feature Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });

        cashierSettingsHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CashierSettingActivity.this, "Help Feature Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(this, CashierMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openEditProfile(){
        Intent intent = new Intent(this, CashierEditProfile.class);
        startActivity(intent);
        finish();
    }

    private void logout(){
        sm.clearSession();
        Intent intent = new Intent(this, RestaurantLogin.class);
        startActivity(intent);
        finish();
    }
}