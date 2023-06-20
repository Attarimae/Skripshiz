package com.example.skripsi.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.R;

public class ManagerMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle, mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView MMA_txtViewWelcome;
    private ImageView MMA_gambarRestoran;
    private Button MMA_ManageRestoMenu,MMA_ManageEmployeeMenu,MMA_ManageCustomer,MMA_EditLandingPage,logoutButton;;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(this);
        setContentView(R.layout.activity_manager_main);
        mTitle = mDrawerTitle = getTitle();

        MMA_gambarRestoran = findViewById(R.id.MMA_gambarRestoran);
        Glide.with(this)
                .load(APIConstant.BASE_URL_DOWNLOAD + sm.fetchRestaurantID()+"_"+sm.fetchStaffID())
                .placeholder(R.drawable.default_profile) // Optional: Add a placeholder image resource
                .error(R.drawable.default_profile) // Optional: Add an error image resource
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(new ObjectKey(System.currentTimeMillis())))
                .into(MMA_gambarRestoran);


        ManageMenu();
        ManageEmployee();
        MMA_ManageCustomer = findViewById(R.id.MMA_ManageCustomer);
        MMA_ManageCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, ManageCustomer.class);
                startActivity(intent);
            }
        });

        MMA_EditLandingPage = findViewById(R.id.MMA_EditLandingPage);
        MMA_EditLandingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, EditLandingPageActivity.class);
                startActivity(intent);
            }
        });
        logoutButton = findViewById(R.id.MMA_buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.menu_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_manager);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        MMA_txtViewWelcome=findViewById(R.id.MMA_txtViewWelcome);
        MMA_txtViewWelcome.setText("Welcome, "+sm.fetchStaffName()+"\n"+sm.fetchStaffRole());

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        sm = new SessionManager(this);
    }

    private void ManageEmployee() {
        MMA_ManageEmployeeMenu = findViewById(R.id.MMA_ManageEmployeeMenu);
        MMA_ManageEmployeeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, ManageEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ManageMenu() {
        MMA_ManageRestoMenu = findViewById(R.id.MMA_ManageRestoMenu);
        MMA_ManageRestoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ManageMenuActivity
                Intent intent = new Intent(ManagerMainActivity.this, ManageMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void logout() {
        sm.clearSession();
        Intent intent = new Intent(ManagerMainActivity.this, RestaurantLogin.class);
        startActivity(intent);
        finish();
    }
}