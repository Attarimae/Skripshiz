package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.skripsi.Activity.Fragment.OrderListDetailsFragment;
import com.example.skripsi.Adapter.KokiViewListOrderDetailAdapter;
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

    public interface StatusUpdateCallback {
        void onStatusUpdated();
    }

    private StatusUpdateCallback callback;


    public KokiAdapter(Context context, ArrayList<OrderListItemDataModel> orders, StatusUpdateCallback callback) {
        super(context, 0, orders);
        this.callback = callback;
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

        viewHolder.orderIdTextView.setText("ID: " + order.getOrderId());
        viewHolder.tableNumberTextView.setText("Table Number: " + order.getTableNumber());

        adapter = new KokiViewListOrderDetailAdapter(KokiAdapter.this.getContext(), order.getOrder_detail(), order.getOrderId());
        adapter.setStatusUpdateCallback(new KokiViewListOrderDetailAdapter.StatusUpdateCallback() {
            @Override
            public void onStatusUpdated() {
                if (callback != null) {
                    callback.onStatusUpdated();
                }
            }
        });
        viewHolder.orderDetailsListView.setAdapter(adapter);

        return convertView;
    }
}
