package com.example.skripsi.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class DetailMenuActivity extends AppCompatActivity {
    private TextView menuNameTextView;
    private TextView menuPriceTextView;
    private ImageView menuImageView;
    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        menuNameTextView = findViewById(R.id.menu_name_textview);
        menuPriceTextView = findViewById(R.id.menu_price_textview);
        menuImageView = findViewById(R.id.menu_imageview);

        Bundle extras = getIntent().getExtras();
        MenuItemModel menuItem = (MenuItemModel) getIntent().getSerializableExtra("menu");

        if (menuItem != null) {
            System.out.println(menuItem.getImgID());
            menuNameTextView.setText(menuItem.getMenuName());
            menuPriceTextView.setText(menuItem.getMenuPrice());
            menuImageView.setImageResource(menuItem.getImgID());
        } else {
            // Handle the case when the menu item is not found
            Toast.makeText(this, "Invalid menu item", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the menu item is not valid
        }
    }
}

