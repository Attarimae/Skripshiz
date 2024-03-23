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
import com.example.skripsi.Activity.DetailEmployeeActivity;
import com.example.skripsi.Model.Promotion.PromotionItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class ManagePromotionAdapter extends ArrayAdapter<PromotionItemModel> {
    private Context context;
    private RequestOptions requestOptions;

    public ManagePromotionAdapter(Context context, ArrayList<PromotionItemModel> promotionItemModelArrayList) {
        super(context, 0, promotionItemModelArrayList);
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

        PromotionItemModel promotionItemModel = getItem(position);

        holder.textView.setText(promotionItemModel.getPromotionName());
        holder.priceView.setText("Quota: " + promotionItemModel.getPromotionQuota());

        // Generate a unique signature based on the current time
        long timestamp = System.currentTimeMillis(); // Subtract one minute
        String signature = String.valueOf(timestamp);

        // Load the image using Glide with cache disabled and a custom signature
        Glide.with(context)
                .load(APIConstant.BASE_URL_DOWNLOAD + promotionItemModel.getImgID())
                .placeholder(R.drawable.discount_tag) // Optional: Add a placeholder image resource
                .error(R.drawable.discount_tag) // Optional: Add an error image resource
                .apply(requestOptions)
                .into(holder.imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailEmployeeActivity.class);
//                intent.putExtra("employee", promotionItemModel);
//                context.startActivity(intent);
//                Toast.makeText(context, "To detail employee: " + promotionItemModel.getStaffName(), Toast.LENGTH_LONG).show();
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
