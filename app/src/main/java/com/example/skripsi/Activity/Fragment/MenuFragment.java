package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private ArrayList<MenuItemModel> menuList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        GridView menuGrid = view.findViewById(R.id.FR_gridMenu);
        menuList = new ArrayList<>();
        SharedPreferencesCashier spc = new SharedPreferencesCashier(requireContext());

        MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), menuList);
        menuGrid.setAdapter(adapter);

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<MenuItemModel>> call = service.getApiService(requireContext()).getAllMenu();
        call.enqueue(new Callback<ArrayList<MenuItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MenuItemModel>> call, Response<ArrayList<MenuItemModel>> response) {
                int menuListSize = response.body().size();
                for(int i=0; i < menuListSize; i++){
                    String menuCategory = response.body().get(i).getMenuCategory();
                    String menuName = response.body().get(i).getMenuName();
                    String menuPrice = "Rp. " + response.body().get(i).getMenuPrice();
                    String menuDescription = response.body().get(i).getMenuDescription();
                    int id = response.body().get(i).getId();
                    String menuImg = response.body().get(i).getImgID();
                    menuList.add(new MenuItemModel(id, menuCategory, menuName, menuDescription, menuPrice, "R.drawable.ic_launcher_background"));
                    //menuList.add(new MenuItemModel(id,menuCategory, menuName, menuDescription, menuPrice, menuImg));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItemModel>> call, Throwable t) {
                Log.e("Fragment Menu", t.getMessage());
            }
        });
        //Search View
        SearchView menuSearchView = view.findViewById(R.id.FT_searchViewMenu);
        menuSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MenuItemModel> filteredMenuList = new ArrayList<>();
                for(MenuItemModel filter : menuList) {
                    if (filter.getMenuName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredMenuList.add(filter);
                    }
                }
                MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), filteredMenuList);
                menuGrid.setAdapter(adapter);
                if(filteredMenuList.isEmpty()){
                    Toast.makeText(requireContext(), "Filter not found...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return view;
    }
}