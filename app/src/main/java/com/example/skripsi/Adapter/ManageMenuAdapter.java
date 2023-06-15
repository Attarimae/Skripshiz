package com.example.skripsi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skripsi.Activity.DetailMenuActivity;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class ManageMenuAdapter extends ArrayAdapter<MenuItemModel> implements Serializable {
    private Context context;
    private ArrayList<CheckoutItemModel> checkoutList;

    public ManageMenuAdapter(@NonNull Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
        this.context = context;
        this.checkoutList = new ArrayList<>();
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem  = convertView;
        if(listItem  == null){
            listItem  = LayoutInflater.from(getContext()).inflate(R.layout.menu_card_item, parent, false);
        }

        MenuItemModel menuItemModel = getItem(position);
        TextView menuName = listItem .findViewById(R.id.menu_Name);
        TextView menuPrice = listItem .findViewById(R.id.menu_Price);
        ImageView menuImage = listItem .findViewById(R.id.menu_Image);

        menuName.setText(menuItemModel.getMenuName());
        menuPrice.setText(menuItemModel.getMenuPrice());
        if (menuItemModel.getImgID() == 0) {
            // Set default image here
            menuImage.setImageResource(R.drawable.smallsalad);
            menuItemModel.setImgID(R.drawable.smallsalad);
        } else {
            menuImage.setImageResource(menuItemModel.getImgID());
        }


        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutList.add(new CheckoutItemModel(
                        menuItemModel.getMenuName(),
                        menuItemModel.getMenuPrice(),
                        menuItemModel.getMenuCategory(),
                        menuItemModel.getMenuDescription(),
                        menuItemModel.getImgID(),
                        1));

                Intent intent = new Intent(context, DetailMenuActivity.class);
                intent.putExtra("menu", menuItemModel);
                context.startActivity(intent);

                Toast.makeText(context, "To detail menu : " + menuItemModel.getMenuName(), Toast.LENGTH_LONG).show();
            }
        });
        return listItem;
    }
}
