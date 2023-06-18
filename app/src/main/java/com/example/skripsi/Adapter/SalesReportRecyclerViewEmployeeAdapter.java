package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skripsi.Model.SalesReport.SalesReportEmployeeModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class SalesReportRecyclerViewEmployeeAdapter extends RecyclerView.Adapter<SalesReportRecyclerViewEmployeeAdapter.ViewHolder>{
    @NonNull
    @Override
    public SalesReportRecyclerViewEmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View RV_employee = inflater.inflate(R.layout.salesreport_recyclerview_employee, parent, false);
        return new ViewHolder(RV_employee);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesReportEmployeeModel item = RV_employeeItemModels.get(position);
        TextView name = holder.recyclerViewEmployeeName;
        name.setText(item.getStaffName());
        TextView sales = holder.recyclerViewEmployeeSales;
        sales.setText(item.getSalesAmount());
    }

    @Override
    public int getItemCount() {
        return RV_employeeItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView recyclerViewEmployeeName, recyclerViewEmployeeSales;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewEmployeeName = (TextView) itemView.findViewById(R.id.recyclerview_EmployeeName);
            recyclerViewEmployeeSales = (TextView) itemView.findViewById(R.id.recyclerview_EmployeeSales);
        }
    }
    private ArrayList<SalesReportEmployeeModel> RV_employeeItemModels;

    public SalesReportRecyclerViewEmployeeAdapter(ArrayList<SalesReportEmployeeModel> employeeItemModels){
        RV_employeeItemModels = employeeItemModels;
    }
}
