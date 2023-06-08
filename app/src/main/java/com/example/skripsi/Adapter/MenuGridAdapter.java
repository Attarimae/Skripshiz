package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skripsi.Model.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class MenuGridAdapter extends ArrayAdapter<MenuItemModel> {

    public MenuGridAdapter(@NonNull Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.menu_card_item, parent, false);
        }

        MenuItemModel menuItemModel = getItem(position);
        TextView menuName = listitem.findViewById(R.id.menu_Name);
        TextView menuPrice = listitem.findViewById(R.id.menu_Price);
        ImageView menuImage = listitem.findViewById(R.id.menu_Image);

        menuName.setText(menuItemModel.getMenuName());
        menuPrice.setText(menuItemModel.getMenuPrice());
        menuImage.setImageResource(menuItemModel.getImgID());
        return listitem;
    }
}
