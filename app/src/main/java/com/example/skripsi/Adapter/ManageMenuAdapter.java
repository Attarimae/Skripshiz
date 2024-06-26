package com.example.skripsi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.Activity.DetailMenuActivity;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class ManageMenuAdapter extends ArrayAdapter<MenuItemModel> {
    private Context context;
    private RequestOptions requestOptions;

    public ManageMenuAdapter(Context context, ArrayList<MenuItemModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
        this.context = context;

        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_card_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.menu_Image);
            holder.textView = convertView.findViewById(R.id.menu_Name);
            holder.priceView = convertView.findViewById(R.id.menu_Price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItemModel menuItemModel = getItem(position);

        holder.textView.setText(menuItemModel.getMenuName());
        holder.priceView.setText(menuItemModel.getMenuPrice());

        // Generate a unique signature based on the current time
        long timestamp = System.currentTimeMillis(); // Subtract one minute
        String signature = String.valueOf(timestamp);

        // Load the image using Glide with cache disabled and a custom signature
        Glide.with(context)
                .load(APIConstant.BASE_URL_DOWNLOAD + menuItemModel.getImgID())
                .placeholder(R.drawable.smallsalad) // Optional: Add a placeholder image resource
                .error(R.drawable.smallsalad) // Optional: Add an error image resource
                .apply(requestOptions)
                .into(holder.imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMenuActivity.class);
                intent.putExtra("menu", menuItemModel);
                context.startActivity(intent);
                Toast.makeText(context, "To detail menu: " + menuItemModel.getMenuName(), Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView priceView;
    }
}
