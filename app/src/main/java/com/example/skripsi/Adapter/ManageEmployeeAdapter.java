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
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class ManageEmployeeAdapter extends ArrayAdapter<EmployeeItemModel> {
    private Context context;
    private RequestOptions requestOptions;

    public ManageEmployeeAdapter(Context context, ArrayList<EmployeeItemModel> employeeItemModelArrayList) {
        super(context, 0, employeeItemModelArrayList);
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

        EmployeeItemModel employeeItemModel = getItem(position);

        holder.textView.setText(employeeItemModel.getStaffName());
        holder.priceView.setText(employeeItemModel.getRole());

        // Generate a unique signature based on the current time
        long timestamp = System.currentTimeMillis(); // Subtract one minute
        String signature = String.valueOf(timestamp);

        // Load the image using Glide with cache disabled and a custom signature
        Glide.with(context)
                .load(APIConstant.BASE_URL_DOWNLOAD + employeeItemModel.getImgID())
                .placeholder(R.drawable.default_profile) // Optional: Add a placeholder image resource
                .error(R.drawable.default_profile) // Optional: Add an error image resource
                .apply(requestOptions)
                .into(holder.imageView);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEmployeeActivity.class);
                intent.putExtra("employee", employeeItemModel);
                context.startActivity(intent);
                Toast.makeText(context, "To detail employee: " + employeeItemModel.getStaffName(), Toast.LENGTH_LONG).show();
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
