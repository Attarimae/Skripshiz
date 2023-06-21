package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.Model.SalesReport.SalesReportModel;
import com.example.skripsi.R;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SalesReportRecyclerViewAdapter extends RecyclerView.Adapter<SalesReportRecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SalesReportModel> RV_orderItemModels;
    private ArrayList<OrderListItemDetailsDataModel> order_detail = new ArrayList<>();

    public SalesReportRecyclerViewAdapter(Context context, ArrayList<SalesReportModel> employeeItemModels){
        this.context = context;
        RV_orderItemModels = employeeItemModels;
    }

    @Override
    public int getItemCount() {
        return RV_orderItemModels.size();
    }

    @NonNull
    @Override
    public SalesReportRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View recyclerView = inflater.inflate(R.layout.salesreport_recyclerview_employee, parent, false);
        return new ViewHolder(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReportRecyclerViewAdapter.ViewHolder holder, int position) {
        SalesReportModel item = RV_orderItemModels.get(position);
        TextView orderTime = holder.recyclerViewOrderTime;

        boolean isExpandable = item.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        String input = item.getOrderDate();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(input);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = offsetDateTime.format(formatter);
        orderTime.setText(formattedDateTime);

        TextView orderID = holder.recyclerViewOrderID;
        orderID.setText("Table Number: " + item.getOrderTableNumber());

        TextView orderTotalPrice = holder.recyclerViewOrderTotalPrice;
        orderTotalPrice.setText("Rp. " + item.getOrderTotalPrice());

        SalesReportNestedRVAdapter adapter = new SalesReportNestedRVAdapter(order_detail);
        holder.expandedView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.expandedView.setAdapter(adapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setExpandable(!item.isExpandable());
                order_detail = item.getOrder_detail();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recyclerViewOrderTime, recyclerViewOrderID, recyclerViewOrderTotalPrice;
        private RecyclerView expandedView;
        private RelativeLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewOrderTime =  itemView.findViewById(R.id.recyclerview_OrderTime);
            recyclerViewOrderID =  itemView.findViewById(R.id.recyclerview_OrderID);
            recyclerViewOrderTotalPrice = itemView.findViewById(R.id.recyclerview_OrderTotalPrice);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandedView = itemView.findViewById(R.id.recyclerview_expanded);
        }

//        public void toggleExpandedView(){
//            if(expandedView.getVisibility() == View.VISIBLE){
//                expandedView.setVisibility(View.GONE);
//                isExpanded = false;
//            } else {
//                expandedView.setVisibility(View.VISIBLE);
//                isExpanded = true;
//            }
//        }
//
//        public boolean isExpanded(){
//            return isExpanded;
//        }

//        public void populateDetails(ArrayList<OrderListItemDetailsDataModel> order_detail){
//            for(OrderListItemDetailsDataModel item : order_detail){
//
//            }
//        }
    }
}
