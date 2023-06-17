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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Activity.DetailMenuActivity;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuAdapter extends ArrayAdapter<MenuItemModel> implements Serializable {
    private Context context;


    public ManageMenuAdapter(@NonNull Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.menu_card_item, parent, false);
        }
        final String[] url = new String[1];
        MenuItemModel menuItemModel = getItem(position);
        TextView menuName = listItem.findViewById(R.id.menu_Name);
        TextView menuPrice = listItem.findViewById(R.id.menu_Price);
        ImageView menuImage = listItem.findViewById(R.id.menu_Image);

        menuName.setText(menuItemModel.getMenuName());
        menuPrice.setText(menuItemModel.getMenuPrice());

        ServiceGenerator service = new ServiceGenerator();
        Call<ResponseBody> call = service.getApiService(context).getPhoto(menuItemModel.getImgID());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Convert the response body to byte array
                    byte[] imageBytes = new byte[0];
                    try {
                        imageBytes = response.body().bytes();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Load the image into the ImageView using Glide
                    Glide.with(context)
                            .load(imageBytes)
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))
                            .into(menuImage);
                    url[0] = call.request().url().toString();
                } else {
                    menuImage.setImageResource(R.drawable.smallsalad);
                    menuItemModel.setImgID(String.valueOf(R.drawable.smallsalad));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Failed ");
            }
        });

//        if (menuItemModel.getImgID() == 0) {
//            // Set default image here
//            menuImage.setImageResource(R.drawable.smallsalad);
//            menuItemModel.setImgID(R.drawable.smallsalad);
//        } else {
//        }


        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailMenuActivity.class);
                intent.putExtra("menu", menuItemModel);
                intent.putExtra("imageId", url[0]);
                context.startActivity(intent);

                Toast.makeText(context, "To detail menu : " + menuItemModel.getMenuName(), Toast.LENGTH_LONG).show();
            }
        });
        return listItem;
    }
}
