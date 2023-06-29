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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MenuGridAdapter extends ArrayAdapter<MenuItemModel> {
    private Context context;
    private ArrayList<CheckoutItemModel> checkoutList = new ArrayList<>();

    public MenuGridAdapter(@NonNull Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
        this.context = context;
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
        Glide.with(context)
                .load(APIConstant.BASE_URL_DOWNLOAD + menuItemModel.getImgID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.smallsalad)
                .into(menuImage);
//        menuImage.setImageResource(Integer.parseInt(menuItemModel.getImgID()));

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

                SharedPreferences sharedPreferences = context.getSharedPreferences("Point of Sales", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String checkoutListJson = gson.toJson(checkoutList);
                editor.putString("checkoutList", checkoutListJson);
                editor.apply();

                Toast.makeText(context, "Berhasil menambahkan " + menuItemModel.getMenuName(), Toast.LENGTH_LONG).show();
            }
        });
        return listItem;
    }
}
