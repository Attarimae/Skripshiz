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

import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class OrderListGridAdapter extends ArrayAdapter<OrderListItemDataModel> {

    public OrderListGridAdapter(@NonNull Context context, ArrayList<OrderListItemDataModel> menuItemModelArrayList) {
        super(context, 0, menuItemModelArrayList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.order_list_card_item, parent, false);
        }

        OrderListItemDataModel menuItemModel = getItem(position);
        TextView orderListName = listitem.findViewById(R.id.order_list_Name);
        TextView orderListStatus = listitem.findViewById(R.id.order_list_OrderStatus);
        ImageView orderListImage = listitem.findViewById(R.id.order_list_Image);

        orderListName.setText(String.valueOf(menuItemModel.getTableNumber()));
        orderListStatus.setText(menuItemModel.getOrder_status());
        orderListImage.setImageResource(R.drawable.ic_launcher_background);
        //orderListImage.setImageResource(menuItemModel.getImgID());
        return listitem;
    }
}
