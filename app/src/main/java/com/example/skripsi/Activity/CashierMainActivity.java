package com.example.skripsi.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Activity.Fragment.CheckoutFragment;
import com.example.skripsi.Activity.Fragment.MenuFragment;
import com.example.skripsi.Activity.Fragment.OrderListFragment;
import com.example.skripsi.Activity.Fragment.ProfilePictureFragment;
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

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_main);

        sm = new SessionManager(this);
        //Ini buat ngetest aja
        sm.saveAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0aW5nLTM3MzFmOWRkLTZjMTYtNGM2OS1iZTA2LWI1MDkyNGM2YWRmYyIsImlhdCI6MTY4NjQ3OTI0OCwiZXhwIjoxMTgwNTM1NDc0MDEwMjB9.Kz8kFzrASWbMrzYOhu0Nt1bVKB8HgzeMdZe61J3St4DTunj5UivqqLnGGh1vEPbzaXgLa7q3fIPpqLgTsu4HYg");
        sm.saveRestaurantName("testing");
        sm.saveRestaurantID("testing-3731f9dd-6c16-4c69-be06-b50924c6adfc");
        sm.saveStaffName("testing");
        sm.saveStaffID("testing-f7dcb933-c6c7-4d46-a713-fe50aa709b38");
        sm.saveStaffRole("cashier");

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.menu_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        MainDrawerMenuModel[] drawerItem = new MainDrawerMenuModel[6];

        drawerItem[0] = new MainDrawerMenuModel(R.drawable.ic_baseline_person_24,"Profile Picture");
        drawerItem[1] = new MainDrawerMenuModel(R.drawable.ic_baseline_home_24,"Home");
        drawerItem[2] = new MainDrawerMenuModel(R.drawable.ic_baseline_list_24,"Order List");
        drawerItem[3] = new MainDrawerMenuModel(R.drawable.ic_baseline_history_24,"Order History");
        drawerItem[4] = new MainDrawerMenuModel(R.drawable.ic_baseline_bar_chart_24,"Report Order");
        drawerItem[5] = new MainDrawerMenuModel(R.drawable.ic_baseline_settings_24,"Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        MainDrawerItemAdapter adapter = new MainDrawerItemAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MenuFragment()).commit();
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

            case 0: // Profile Picture Fragment
                fragment = new ProfilePictureFragment();
                removeCheckoutCart();
                break;
            case 1: // Home Fragment
                fragment = new MenuFragment();
                addCheckoutCart();
                break;
            case 2: // Order List Fragment
                fragment = new OrderListFragment();
                removeCheckoutCart();
                break;
            case 3: // Order History Fragment
                //fragment = new MenuFragment();
                //removeCheckoutCart();
                break;
            case 4: // Sales Report / Report Order Fragment
                //fragment = new CheckoutFragment();
                //removeCheckoutCart();
                break;
            case 5: // Settings Fragment
                //fragment = new MenuFragment();
                Toast.makeText(this, "Setting Soon", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
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

}