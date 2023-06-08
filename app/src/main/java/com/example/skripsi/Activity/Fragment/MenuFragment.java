package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        GridView menuGrid = view.findViewById(R.id.FR_gridMenu);
        ArrayList<MenuItemModel> menuList = new ArrayList<MenuItemModel>();

        menuList.add(new MenuItemModel("Nasi Goreng", "Rp. 69.000", R.drawable.ic_launcher_background));
        menuList.add(new MenuItemModel("Ayam Goreng", "Rp. 96.000", R.drawable.ic_launcher_background));
        menuList.add(new MenuItemModel("Pening Bakar", "Rp. 42.000", R.drawable.ic_launcher_background));
        menuList.add(new MenuItemModel("Kanzler", "Rp. 10.000", R.drawable.ic_launcher_background));
        menuList.add(new MenuItemModel("Blenger", "Rp. 20.000", R.drawable.ic_launcher_background));
        menuList.add(new MenuItemModel("Bento", "Rp. 30.000", R.drawable.ic_launcher_background));

        MenuGridAdapter adapter = new MenuGridAdapter(requireActivity(), menuList);
        menuGrid.setAdapter(adapter);
        return view;
    }
}