package com.example.skripsi.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.LoginDataModel;
import com.example.skripsi.Model.StaffDataModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSLogin extends AppCompatActivity {

    boolean isAllFieldsChecked = false;

    private Button loginbtn;
    private EditText email, password;
    private TextView registerPOS;

    private static final String TAG = "POSLogin";
    private static final String password_regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).+$";

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_login);

        sm = new SessionManager(this);

        loginbtn = findViewById(R.id.loginbtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        registerPOS = findViewById(R.id.posopenregister);
        String loginRestaurantText = "Don't have account yet? Register here ";
        SpannableString ss = new SpannableString(loginRestaurantText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openPOSRegistration();
            }
        };

        ss.setSpan(clickableSpan, 24, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerPOS.setText(ss);
        registerPOS.setMovementMethod(LinkMovementMethod.getInstance());

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    postPOSLogin(sm.fetchStaffID(), sm.fetchStaffRole(), email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void openPOSRegistration() {
        Intent intent = new Intent(this, RestaurantRegister.class);
        startActivity(intent);
        finish();
    }

    private void openPOSHomepage() {
        System.out.println("SM role "+sm.fetchStaffRole());
        System.out.println("SM restaurant id "+sm.fetchRestaurantID());
        System.out.println("SM restaurant name "+sm.fetchRestaurantName());
        System.out.println("SM staff name "+sm.fetchStaffName());
        System.out.println("SM staff id "+sm.fetchStaffID());
        String role = sm.fetchStaffRole();
        if(role.equalsIgnoreCase("cashier")){
            Intent intent = new Intent(this, CashierMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("openfragment", "MenuFragment()");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        if(role.equalsIgnoreCase("manager")){
            Intent intent = new Intent(this, ManagerMainActivity.class);
            startActivity(intent);
            finish();
        }
        if(role.equalsIgnoreCase("koki")){
            Intent intent = new Intent(this, KokiMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void postPOSLogin(String staff_id, String role, String email, String password) {
        ServiceGenerator service = new ServiceGenerator();
        StaffDataModel modal = new StaffDataModel(staff_id, role, email, password);
        Call<LoginDataModel> call = service.getApiService(this).postStaffLogin(modal);
        call.enqueue(new Callback<LoginDataModel>() {

            @Override
            public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {
                LoginDataModel modalAPI = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(POSLogin.this, "Berhasil Login akun POS", Toast.LENGTH_SHORT).show();
                    sm.saveStaffRole(modalAPI.getRole());
                    sm.saveStaffID(modalAPI.getStaff_id());
                    sm.saveStaffName(modalAPI.getName());
                    sm.saveAuthToken(modalAPI.getKey());
                    sm.saveRestaurantID(modalAPI.getRestaurant_id());
                    sm.saveRestaurantName(modalAPI.getRestaurant_name());
                    openPOSHomepage();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(POSLogin.this, "Gagal Login akun pos: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(POSLogin.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(POSLogin.this, "Gagal Login akun pos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed - POST POS Login");
            }
        });
    }

    private boolean CheckAllFields() {
        String emailToText = email.getText().toString();
        String passwordToText = password.getText().toString();

        if (emailToText.isEmpty()) {
            email.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            email.setError("Email is invalid");
            return false;
        } else {
            email.setError(null);
        }

        if (passwordToText.length() == 0) {
            password.setError("Password is required");
            return false;
        } else if (passwordToText.length() < 8) {
            password.setError("Password must be minimum 8 characters");
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }
}