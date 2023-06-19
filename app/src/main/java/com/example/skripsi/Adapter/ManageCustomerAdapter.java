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
import com.example.skripsi.Activity.DetailCustomerActivity;
import com.example.skripsi.Activity.DetailEmployeeActivity;
import com.example.skripsi.Model.Customer.CustomerItemModel;
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class ManageCustomerAdapter extends ArrayAdapter<CustomerItemModel> {
    private Context context;
    private RequestOptions requestOptions;

    public ManageCustomerAdapter(Context context, ArrayList<CustomerItemModel> customerItemModels) {
        super(context, 0, customerItemModels);
        this.context = context;

        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
            holder = new ViewHolder();
            holder.card_name = convertView.findViewById(R.id.card_name);
            holder.card_description = convertView.findViewById(R.id.card_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CustomerItemModel customerItemModel = getItem(position);

        holder.card_name.setText(customerItemModel.getName());
        holder.card_description.setText(customerItemModel.getEmail());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailCustomerActivity.class);
                intent.putExtra("customer", customerItemModel);
                context.startActivity(intent);
                Toast.makeText(context, "To detail customer: " + customerItemModel.getName(), Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView card_name;
        TextView card_description;
    }
}
