package com.example.skripsi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class SalesReportNestedRVAdapter extends RecyclerView.Adapter<SalesReportNestedRVAdapter.NestedViewHolder> {

    private ArrayList<OrderListItemDetailsDataModel> model;

    public SalesReportNestedRVAdapter(ArrayList<OrderListItemDetailsDataModel> model){
        this.model = model;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salesreport_recyclerviewexpanded, parent, false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.orderQty.setText(model.get(position).getMenuQuantity() + "x");
        holder.orderName.setText(model.get(position).getMenuName());
        holder.orderPrice.setText("Rp. " + model.get(position).getMenuPrice());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView orderQty, orderName, orderPrice;
        public NestedViewHolder(@NonNull View itemView){
            super(itemView);
            orderQty = itemView.findViewById(R.id.expanded_OrderQty);
            orderName = itemView.findViewById(R.id.expanded_OrderName);
            orderPrice = itemView.findViewById(R.id.expanded_OrderPrice);
        }
    }
}
