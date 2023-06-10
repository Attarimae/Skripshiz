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

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.StaffDataModel;
import com.example.skripsi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSLogin extends AppCompatActivity {

    boolean isAllFieldsChecked = false;

    private Button loginbtn;
    private EditText email, password;
    private TextView registerPOS;

    private static final String TAG = "POSLogin";
    private static final String password_regex = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_login);

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
                if(isAllFieldsChecked){
                    //postPOSLogin(sm.fetchStaffID(), sm.fetchStaffRole(), email.getText().toString(), password.getText().toString());
                }
            }
        });

        sm = new SessionManager(this);
    }

    private void openPOSRegistration(){
        Intent intent = new Intent(this, POSRegister.class);
        startActivity(intent);
        finish();
    }

    private void openCashierPOSHomepage(){
        Intent intent = new Intent(this, CashierMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void postPOSLogin(String staff_id, String role, String email, String password){
        ServiceGenerator service = new ServiceGenerator();
        StaffDataModel modal = new StaffDataModel(staff_id, role, email, password);
        Call<StaffDataModel> call = service.getApiService(this).postStaffLogin(modal);
        call.enqueue(new Callback<StaffDataModel>(){

            @Override
            public void onResponse(Call<StaffDataModel> call, Response<StaffDataModel> response) {
                StaffDataModel modalAPI = response.body();
                sm.saveStaffRole(modalAPI.getRole());
                openCashierPOSHomepage();
            }

            @Override
            public void onFailure(Call<StaffDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed - POST POS Login");
            }
        });
    }

    private boolean CheckAllFields(){
        String emailToText = email.getText().toString();
        String passwordToText = password.getText().toString();

        if(emailToText.isEmpty()){
            email.setError("Email is required");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()){
            email.setError("Email is invalid");
            return false;
        } else {
            email.setError(null);
        }

        if(passwordToText.length() == 0){
            password.setError("Password is required");
            return false;
        } else if(passwordToText.length() < 8){
            password.setError("Password must be minimum 8 characters");
        } else if(!passwordToText.matches(password_regex)){
            password.setError("Password is invalid");
        } else {
            password.setError(null);
        }
        return true;
    }
}