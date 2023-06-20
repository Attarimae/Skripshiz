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
import com.example.skripsi.Model.RestaurantDataModel;
import com.example.skripsi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantLogin extends AppCompatActivity {

    private static final String TAG = "RestaurantLogin";

    boolean isAllFieldsChecked = false;

    private static final String email_regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*" + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String password_regex = "^(?=.*[a-zA-Z])(?=\\S+$).{8,}$";

    private EditText email, password;
    private Button loginButton;
    private TextView restaurantRegistration;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_login);

        sm = new SessionManager(this);

        email = findViewById(R.id.restaurantLogin_Email);
        password = findViewById(R.id.restaurantLogin_Password);

        loginButton = findViewById(R.id.restaurantLoginButton);

        restaurantRegistration = findViewById(R.id.restaurantClickableRegister);
        String loginRestaurantText = "Don't have account yet? Register here ";
        SpannableString ss = new SpannableString(loginRestaurantText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openRestaurantRegistration();
            }
        };

        ss.setSpan(clickableSpan, 24, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        restaurantRegistration.setText(ss);
        restaurantRegistration.setMovementMethod(LinkMovementMethod.getInstance());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    postRestaurantLogin(email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void openRestaurantRegistration(){
        Intent intent = new Intent(this, RestaurantRegister.class);
        startActivity(intent);
        finish();
    }

    private void openPOSLogin(){
        Intent intent = new Intent(this, POSLogin.class);
        startActivity(intent);
        finish();
    }

    private void postRestaurantLogin(String restaurant_id, String password){
        ServiceGenerator service = new ServiceGenerator();
        RestaurantDataModel modal = new RestaurantDataModel(restaurant_id, password);
        Call<RestaurantDataModel> call = service.getApiService(this).postRestaurantLogin(modal);
        call.enqueue(new Callback<RestaurantDataModel>(){

            @Override
            public void onResponse(Call<RestaurantDataModel> call, Response<RestaurantDataModel> response) {
                RestaurantDataModel modalAPI = response.body();
                sm.saveAuthToken(modalAPI.getKey());
                sm.saveRestaurantID(modalAPI.getRestaurant_id());
                sm.saveRestaurantName(modalAPI.getRestaurant_name());
                openPOSLogin();
            }

            @Override
            public void onFailure(Call<RestaurantDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed - POST Restaurant Register");
            }
        });
    }

    private boolean CheckAllFields(){
        String emailToText = email.getText().toString();
        String passwordToText = password.getText().toString();

        if(emailToText.isEmpty()){
            email.setError("Email is required");
            return false;
        }

        if(passwordToText.length() == 0){
            password.setError("Password is required");
            return false;
        }
        return true;
    }
}