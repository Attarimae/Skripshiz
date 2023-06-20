package com.example.skripsi.Activity.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.SalesReportRecyclerViewEmployeeAdapter;
import com.example.skripsi.Model.Employee.EmployeeItemModel;
import com.example.skripsi.Model.SalesReport.POSTReportOrder;
import com.example.skripsi.Model.SalesReport.SalesReportEmployeeModel;
import com.example.skripsi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReportFragment extends Fragment {

    ArrayList<SalesReportEmployeeModel> dummyData = new ArrayList<>();
    String date;
    TextView salesReportDateAmount;
    RecyclerView salesReportRecyclerViewEmployees;
    SalesReportRecyclerViewEmployeeAdapter adapter;
    Integer totalSalesReportToday = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_report, container, false);
        TextView salesReportDateText = view.findViewById(R.id.FR_salesReport_DateText);
        salesReportDateAmount = view.findViewById(R.id.FR_salesReport_AmountTotalSales);
        Button salesReportDateButton = view.findViewById(R.id.FR_salesReport_DateButton);
        salesReportRecyclerViewEmployees = view.findViewById(R.id.FR_salesReport_recyclerviewSalesEmployee);

        SimpleDateFormat sdf = new SimpleDateFormat("'Date: 'dd/MM/yyyy");
        salesReportDateText.setText(sdf.format(new Date()));

        salesReportDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date = dayOfMonth + "/" + (month+1) + "/" + year;
                                salesReportDateText.setText("Date: " + dayOfMonth + "/" + (month+1) + "/" + year);
                                //Get your Sales Report Here!
                                getSalesReport();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        return view;
    }

    private void getSalesReport(){
        totalSalesReportToday = 0;
        ArrayList<POSTReportOrder> todaySalesReport = new ArrayList<>();
        ServiceGenerator service = new ServiceGenerator();
        POSTReportOrder modal = new POSTReportOrder(date, 30); //Tanggal dan Unit
        Call<ArrayList<POSTReportOrder>> call = service.getApiService(requireContext()).postReportOrder(modal);
        call.enqueue(new Callback<ArrayList<POSTReportOrder>>() {
            @Override
            public void onResponse(Call<ArrayList<POSTReportOrder>> call, Response<ArrayList<POSTReportOrder>> response) {
                if(response.isSuccessful()){
                    int postreportordersize = response.body().size();
                    if(postreportordersize == 0){
                        Toast.makeText(requireContext(), "There's no sales today...", Toast.LENGTH_SHORT).show();
                    } else {
                        todaySalesReport.addAll(response.body());
                        for(POSTReportOrder item : todaySalesReport){
                            if(item.getTotalPrice() == null){
                                totalSalesReportToday += 0;
                            } else {
                                totalSalesReportToday += Integer.parseInt(item.getTotalPrice());
                            }
                        }
                        salesReportDateAmount.setText("Rp. " + String.valueOf(totalSalesReportToday));

                        for(POSTReportOrder item : todaySalesReport){
                            String employeeName = item.getUser_id();
                            int removeHyphen = employeeName.indexOf("-");
                            dummyData.add(new SalesReportEmployeeModel(employeeName.substring(0,removeHyphen), Integer.parseInt(item.getTotalPrice())));
                        }
                        adapter = new SalesReportRecyclerViewEmployeeAdapter(dummyData);
                        salesReportRecyclerViewEmployees.setAdapter(adapter);
                        salesReportRecyclerViewEmployees.setLayoutManager(new LinearLayoutManager(requireContext()));
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<POSTReportOrder>> call, Throwable t) {
                Log.e("Fragment SalesReport", "Failed to get Sales Report");
                Log.e("Fragment SalesReport", t.getMessage());
            }
        });
    }
}