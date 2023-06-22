package com.example.skripsi.Activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Activity.Fragment.CheckoutFragment;
import com.example.skripsi.Activity.Fragment.MenuFragment;
import com.example.skripsi.Activity.Fragment.OrderHistoryFragment;
import com.example.skripsi.Activity.Fragment.OrderListFragment;
import com.example.skripsi.Activity.Fragment.ProfilePictureFragment;
import com.example.skripsi.Activity.Fragment.SalesReportFragment;
import com.example.skripsi.Adapter.MainDrawerItemAdapter;
import com.example.skripsi.Model.MainDrawerMenuModel;
import com.example.skripsi.R;

public class CashierMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle, mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu cashierMenu;

//    TextView MA_txtViewWelcome;
//    Button logoutButton;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_main);

        sm = new SessionManager(this);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.menu_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        MA_txtViewWelcome = findViewById(R.id.MA_txtViewWelcome);
        System.out.println("STAFFF NAME "+sm.fetchStaffName());
        System.out.println("STAFFF ID " +sm.fetchStaffID());
//        MA_txtViewWelcome.setText("Welcome, "+sm.fetchStaffName()+"\n"+sm.fetchStaffRole());
//        logoutButton = findViewById(R.id.MA_buttonLogout);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout();
//            }
//        });

//        ImageView MA_gambarRestoran = findViewById(R.id.MA_gambarRestoran);
//        Glide.with(this)
//                .load(APIConstant.BASE_URL_DOWNLOAD + sm.fetchRestaurantID() + "_" + sm.fetchStaffID())
//                .placeholder(R.drawable.default_profile)
//                .error(R.drawable.default_profile)
//                .apply(new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .signature(new ObjectKey(System.currentTimeMillis())))
//                .into(MA_gambarRestoran);

        setupToolbar();

        MainDrawerMenuModel[] drawerItem = new MainDrawerMenuModel[5];

        //drawerItem[0] = new MainDrawerMenuModel(R.drawable.ic_baseline_person_24,"Profile Picture");
        drawerItem[0] = new MainDrawerMenuModel(R.drawable.ic_baseline_home_24,"Home");
        drawerItem[1] = new MainDrawerMenuModel(R.drawable.ic_baseline_list_24,"Order List");
        drawerItem[2] = new MainDrawerMenuModel(R.drawable.ic_baseline_history_24,"Order History");
        drawerItem[3] = new MainDrawerMenuModel(R.drawable.ic_baseline_bar_chart_24,"Report Order");
        drawerItem[4] = new MainDrawerMenuModel(R.drawable.ic_baseline_settings_24,"Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        MainDrawerItemAdapter adapter = new MainDrawerItemAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        getBundle();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        cashierMenu = menu;
        return true;
    }

    private void selectItem(int position) {
        Fragment fragment = null;

        switch (position) {

//            case 0: // Profile Picture Fragment
//                fragment = new ProfilePictureFragment();
//                removeCheckoutCart();
//                break;
            case 0: // Home Fragment
                fragment = new MenuFragment();
                addCheckoutCart();
                break;
            case 1: // Order List Fragment
                fragment = new OrderListFragment();
                removeCheckoutCart();
                break;
            case 2: // Order History Fragment
                fragment = new OrderHistoryFragment();
                removeCheckoutCart();
                break;
            case 3: // Sales Report / Report Order Fragment
                fragment = new SalesReportFragment();
                removeCheckoutCart();
                break;
            case 4: // Settings Fragment
                openCashierSettings();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            //mDrawerList.setItemChecked(position, true);
            //mDrawerList.setSelection(position);
            //setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("CashierMainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    public void onCheckoutCartClicked(MenuItem mi) {
        Fragment fragment = new CheckoutFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void removeCheckoutCart(){
        cashierMenu.findItem(R.id.action_checkout).setVisible(false);
        cashierMenu.findItem(R.id.action_checkout).setEnabled(false);
    }

    private void addCheckoutCart(){
        cashierMenu.findItem(R.id.action_checkout).setVisible(true);
        cashierMenu.findItem(R.id.action_checkout).setEnabled(true);
    }
    private void logout() {
        sm.clearSession();
        Intent intent = new Intent(CashierMainActivity.this, RestaurantLogin.class);
        startActivity(intent);
        finish();
    }

    private void openCashierSettings(){
        Intent intent = new Intent(this, CashierSettingActivity.class);
        startActivity(intent);
        finish();
    }

    private void getBundle(){
        Bundle bundle = getIntent().getExtras();
        String fragmentString = bundle.getString("openfragment", "MenuFragment()");
        Fragment fragment = null;
        switch (fragmentString){
            case "MenuFragment()":
                fragment = new MenuFragment();
                break;
            case "OrderListFragment()":
                fragment = new OrderListFragment();
                break;
            case "OrderHistoryFragment()":
                fragment = new OrderHistoryFragment();
                break;
            case "SalesReportFragment()":
                fragment = new SalesReportFragment();
                break;
            default:
                break;
        }
        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            Log.e("CashierMainActivity", "getBundle: Error in creating fragment");
        }
    }
}