package com.example.skripsi.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Activity.Fragment.CheckoutFragment;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.MenuItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MenuGridAdapter extends ArrayAdapter<MenuItemModel> {
    private Context context;
    private ArrayList<CheckoutItemModel> checkoutList;

    public MenuGridAdapter(@NonNull Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
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
        menuImage.setImageResource(menuItemModel.getImgID());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutList.add(new CheckoutItemModel(menuItemModel.getMenuName(),
                        menuItemModel.getMenuPrice(), 1, menuItemModel.getImgID()));

                SharedPreferences sharedPreferences = context.getSharedPreferences("Point of Sales", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String checkoutListJson = gson.toJson(checkoutList);
                editor.putString("checkoutList", checkoutListJson);
                editor.apply();
            }
        });
        return listItem;
    }
}
