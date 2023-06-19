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

public class RestaurantRegister extends AppCompatActivity {

    private static final String TAG = "RestaurantRegister";

    private EditText restaurantName, email, phonenumber, password, confirmpassword;
    private Button loginButton;
    private TextView loginRestaurant;

    boolean isAllFieldsChecked = false;

    private static final String email_regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*" + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String password_regex = "^(?=.*[a-zA-Z])(?=\\S+$).{8,}$";

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);

        restaurantName = findViewById(R.id.restaurantName);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpw);

        loginButton = findViewById(R.id.confirm_restaurant_registration);

        loginRestaurant = findViewById(R.id.loginRestaurant);
        String loginRestaurantText = "Already have an account? Sign in here!";
        SpannableString ss = new SpannableString(loginRestaurantText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openRestaurantLogin();
            }
        };

        ss.setSpan(clickableSpan, 25, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginRestaurant.setText(ss);
        loginRestaurant.setMovementMethod(LinkMovementMethod.getInstance());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    postRestaurantRegisterData(restaurantName.getText().toString(), email.getText().toString(), phonenumber.getText().toString(), password.getText().toString());
                }
            }
        });

        sm = new SessionManager(this);
    }

    private void openRestaurantLogin(){
        Intent intent = new Intent(this, RestaurantLogin.class);
        startActivity(intent);
        finish();
    }

    private void postRestaurantRegisterData(String restaurantName, String email, String phoneNumber, String password){
        ServiceGenerator service = new ServiceGenerator();
        RestaurantDataModel modal = new RestaurantDataModel(restaurantName, email, phoneNumber, password);
        Call<RestaurantDataModel> call = service.getApiService(this).postRestaurantRegister(modal);
        call.enqueue(new Callback<RestaurantDataModel>(){

            @Override
            public void onResponse(Call<RestaurantDataModel> call, Response<RestaurantDataModel> response) {
                RestaurantDataModel modalAPI = response.body();
                sm.saveRestaurantID(modalAPI.getRestaurant_id());
                sm.saveRestaurantName(restaurantName);
                Toast.makeText(RestaurantRegister.this, "Berhasil membuat akun restaurant", Toast.LENGTH_SHORT).show();
                openRestaurantLogin();
            }

            @Override
            public void onFailure(Call<RestaurantDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed - POST Restaurant Register");
            }
        });
    }

    private boolean CheckAllFields(){
        String restaurantNameToText = restaurantName.getText().toString();
        String emailToText = email.getText().toString();
        String passwordToText = password.getText().toString();
        String phoneNumberToText = phonenumber.getText().toString();

        if(restaurantNameToText.isEmpty()){
            restaurantName.setError("Restaurant Name is required");
            return false;
        } else if(!restaurantNameToText.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+")){
            restaurantName.setError("Restaurant Name is invalid");
            return false;
        } else {
            restaurantName.setError(null);
        }

        if(emailToText.isEmpty()){
            email.setError("Email is required");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()){
            email.setError("Email is invalid");
            return false;
        } else {
            email.setError(null);
        }

        if(phoneNumberToText.isEmpty()){
            phonenumber.setError("Phone Number is required");
            return false;
        } else if(!phoneNumberToText.matches("^(\\+62|62|0)8[1-9][0-9]{6,9}$")){
            phonenumber.setError("Phone Number is invalid");
            return false;
        } else {
            phonenumber.setError(null);
        }

        if(passwordToText.length() == 0){
            password.setError("Password is required");
            return false;
        } else if(passwordToText.length() < 8){
            password.setError("Password must be minimum 8 characters");
            return false;
        } else if(!passwordToText.matches(password_regex)){
            password.setError("Password is invalid");
            return false;
        } else if(!passwordToText.matches(confirmpassword.getText().toString())){
            confirmpassword.setError("Password does not match");
            return false;
        } else {
            password.setError(null);
            confirmpassword.setError(null);
        }
        return true;
    }
}