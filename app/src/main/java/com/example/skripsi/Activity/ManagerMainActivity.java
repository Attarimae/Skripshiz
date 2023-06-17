package com.example.skripsi.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.skripsi.API.SessionManager;
import com.example.skripsi.R;

public class ManagerMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle, mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button MMA_ManageRestoMenu,MMA_ManageEmployeeMenu,MMA_EditLandingPage,MMA_EditWebsiteMenu;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        mTitle = mDrawerTitle = getTitle();
        MMA_ManageRestoMenu = findViewById(R.id.MMA_ManageRestoMenu);

        MMA_ManageEmployeeMenu = findViewById(R.id.MMA_ManageEmployeeMenu);
        MMA_ManageRestoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ManageMenuActivity
                Intent intent = new Intent(ManagerMainActivity.this, ManageMenuActivity.class);
                startActivity(intent);
            }
        });
        MMA_ManageEmployeeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerMainActivity.this, ManageEmployee.class);
                startActivity(intent);
            }
        });
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.menu_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_manager);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        sm = new SessionManager(this);
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}