package com.example.skripsi.Activity;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.StaffDataModel;
import com.example.skripsi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSRegister extends AppCompatActivity {

    private static final String TAG = "POSRegister";
    private static final String password_regex = "^(?=.*[a-zA-Z])(?=\\S+$).{8,20}$";

    private EditText posName, email, phonenumber, password, confirmpassword;
    private Button registerbtn;
    private TextView loginPOS;

    boolean isAllFieldsChecked = false;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posregister);

        posName = findViewById(R.id.posname);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpw);

        loginPOS = findViewById(R.id.posopenlogin);
        String loginPOSText = "Already have an account? Sign in here!";
        SpannableString ss = new SpannableString(loginPOSText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                openPOSLogin();
            }
        };

        ss.setSpan(clickableSpan, 25, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginPOS.setText(ss);
        loginPOS.setMovementMethod(LinkMovementMethod.getInstance());

        registerbtn = findViewById(R.id.confirm_pos_button);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked){
                    postPOSRegister(sm.fetchRestaurantID(), email.getText().toString(), phonenumber.getText().toString(), password.getText().toString());
                }
            }
        });

        sm = new SessionManager(this);
    }

    private void openPOSLogin(){
        Intent intent = new Intent(this, POSLogin.class);
        startActivity(intent);
        finish();
    }

    private boolean CheckAllFields(){
        String posNameToText = posName.getText().toString();
        String emailToText = email.getText().toString();
        String passwordToText = password.getText().toString();
        String phoneNumberToText = phonenumber.getText().toString();

        if(posNameToText.isEmpty()){
            posName.setError("Name is required");
            return false;
        } else if(!posNameToText.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+")){
            posName.setError("Name is invalid");
            return false;
        } else {
            posName.setError(null);
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

    private void postPOSRegister(String staffName, String email, String phoneNumber, String password){
        ServiceGenerator service = new ServiceGenerator();
        StaffDataModel modal = new StaffDataModel(staffName, email, phoneNumber, password);
        Call<StaffDataModel> call = service.getApiService(this).postStaffRegister(modal);
        call.enqueue(new Callback<StaffDataModel>(){

            @Override
            public void onResponse(Call<StaffDataModel> call, Response<StaffDataModel> response) {
                StaffDataModel modalAPI = response.body();
                sm.saveStaffID(modalAPI.getStaff_id());
                sm.saveStaffRole(modalAPI.getRole());
                sm.saveStaffName(staffName);
                Toast.makeText(POSRegister.this, "Berhasil membuat akun POS", Toast.LENGTH_SHORT).show();
                openPOSLogin();
            }

            @Override
            public void onFailure(Call<StaffDataModel> call, Throwable t) {
                Log.e(TAG, "onFailure: Failed - POST Restaurant Register");
            }
        });
    }
}
