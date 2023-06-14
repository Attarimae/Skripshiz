package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
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
                    //int menuImg = response.body().get(i).getImgID(); karena masih null, jadi g kumasukkin dlu
                    menuList.add(new MenuItemModel(menuCategory, menuName, menuDescription, menuPrice, R.drawable.ic_launcher_background));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MenuItemModel>> call, Throwable t) {
                Log.e("Fragment Menu", t.getMessage());
            }
        });

        return view;
    }
}