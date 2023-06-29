package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class KokiAdapter extends ArrayAdapter<OrderListItemDataModel> {
    private static class ViewHolder {
        TextView orderIdTextView;
        TextView tableNumberTextView;
        ListView orderDetailsListView;
    }

    private KokiViewListOrderDetailAdapter adapter;
    public KokiAdapter(Context context, ArrayList<OrderListItemDataModel> orders) {
        super(context, 0, orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListItemDataModel order = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            viewHolder.orderIdTextView = convertView.findViewById(R.id.orderIdTextView);
            viewHolder.tableNumberTextView = convertView.findViewById(R.id.tableNumberTextView);
            viewHolder.orderDetailsListView = convertView.findViewById(R.id.orderView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        viewHolder.tableNumberTextView.setText("Table Number: " + order.getTableNumber());

        // Create an ArrayAdapter to populate the order details in the ListView
        adapter = new KokiViewListOrderDetailAdapter(KokiAdapter.this.getContext(),order.getOrder_detail());
        viewHolder.orderDetailsListView.setAdapter(adapter);

        return convertView;
    }
}
