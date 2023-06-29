package com.example.skripsi.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Activity.Fragment.CheckoutFragment;
import com.example.skripsi.Activity.Fragment.MenuFragment;
import com.example.skripsi.Activity.Fragment.OrderHistoryFragment;
import com.example.skripsi.Activity.Fragment.OrderListFragment;
import com.example.skripsi.Activity.Fragment.SalesReportFragment;
import com.example.skripsi.Adapter.MainDrawerItemAdapter;
import com.example.skripsi.Model.MainDrawerMenuModel;
import com.example.skripsi.R;

import java.util.Objects;

public class CashierSettingActivity extends AppCompatActivity {

    ImageView cashierSettingsPicture;
    Button cashierSettingsEditProfile, cashierSettingsSendFeedback, cashierSettingsHelp, cashierSettingsLogout;
    TextView cashierSettingsName, cashierSettingsRole, cashierSettingsVersion;
    SessionManager sm;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle, mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private Menu cashierSettingsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_setting);

        sm = new SessionManager(this);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.menu_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.cashier_settings_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.cashier_settings_left_drawer);

        setupToolbar();

        MainDrawerMenuModel[] drawerItem = new MainDrawerMenuModel[5];

        //drawerItem[0] = new MainDrawerMenuModel(R.drawable.ic_baseline_person_24,"Profile Picture");
        drawerItem[0] = new MainDrawerMenuModel(R.drawable.ic_baseline_home_24,"Home");
        drawerItem[1] = new MainDrawerMenuModel(R.drawable.ic_baseline_list_24,"Order List");
        drawerItem[2] = new MainDrawerMenuModel(R.drawable.ic_baseline_history_24,"Order History");
        drawerItem[3] = new MainDrawerMenuModel(R.drawable.ic_baseline_bar_chart_24,"Report Order");
        drawerItem[4] = new MainDrawerMenuModel(R.drawable.ic_baseline_settings_24,"Settings");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        MainDrawerItemAdapter adapter = new MainDrawerItemAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new CashierSettingActivity.DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.cashier_settings_drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

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

//    public void onBackButtonClicked(View view) {
//        Intent intent = new Intent(this, CashierMainActivity.class);
//        startActivity(intent);
//        finish();
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        cashierSettingsMenu = menu;
        cashierSettingsMenu.findItem(R.id.action_checkout).setVisible(false);
        cashierSettingsMenu.findItem(R.id.action_checkout).setEnabled(false);
        return true;
    }

    public void onCheckoutCartClicked(MenuItem mi) {
        //Unused
    }

    private void selectItem(int position) {
        String fragment = null;

        switch (position) {
            case 0: // Home Fragment
                fragment = "MenuFragment()";
                break;
            case 1: // Order List Fragment
                fragment = "OrderListFragment()";
                break;
            case 2: // Order History Fragment
                fragment = "OrderHistoryFragment()";
                break;
            case 3: // Sales Report / Report Order Fragment
                fragment = "SalesReportFragment()";
                break;
            case 4: // Settings Fragment
                break;

            default:
                break;
        }

        if (fragment != null) {
            mDrawerLayout.closeDrawer(mDrawerList);
            Intent intent = new Intent(this, CashierMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("openfragment", fragment);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.cashier_settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    private void openEditProfile(){
        Intent intent = new Intent(this, CashierEditProfile.class);
        startActivity(intent);
        finish();
    }

    private void logout(){
        sm.clearSession();
        Intent intent = new Intent(this, POSLogin.class);
        startActivity(intent);
        finish();
    }
}