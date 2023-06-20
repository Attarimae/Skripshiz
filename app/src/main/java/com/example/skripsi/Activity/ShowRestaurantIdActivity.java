package com.example.skripsi.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.R;

public class ShowRestaurantIdActivity extends AppCompatActivity {

    private TextView textViewRestaurantId;
    private Button buttonCopy;
    private Button button_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant_id);

        textViewRestaurantId = findViewById(R.id.text_view_restaurant_id);
        buttonCopy = findViewById(R.id.button_copy);
        button_to_login = findViewById(R.id.button_to_login);

        // Retrieve the restaurant ID from the intent
        String restaurantId = getIntent().getStringExtra("restaurantId");

        // Display the restaurant ID in the TextView
        textViewRestaurantId.setText("Restaurant ID: " + restaurantId);

        // Set click listener for the copy button
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(restaurantId);
            }
        });

        button_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowRestaurantIdActivity.this, RestaurantLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Restaurant ID", text);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Restaurant ID copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
