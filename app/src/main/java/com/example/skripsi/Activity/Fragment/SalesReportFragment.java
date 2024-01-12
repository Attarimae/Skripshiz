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
import com.example.skripsi.Adapter.SalesReportRecyclerViewAdapter;
import com.example.skripsi.Model.SalesReport.POSTReportOrder;
import com.example.skripsi.Model.SalesReport.SalesReportModel;
import com.example.skripsi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReportFragment extends Fragment {

    ArrayList<SalesReportModel> dummyData = new ArrayList<>();
    String date;
    TextView salesReportDateAmount;
    RecyclerView salesReportRecyclerViewEmployees;
    SalesReportRecyclerViewAdapter adapter;
    Integer totalSalesReportToday = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_report, container, false);
        TextView salesReportDateOneText = view.findViewById(R.id.FR_salesReport_DateOneText);
        TextView salesReportDateTwoText = view.findViewById(R.id.FR_salesReport_DateTwoText);
        salesReportDateAmount = view.findViewById(R.id.FR_salesReport_AmountTotalSales);
        Button salesReportCheckSalesButton = view.findViewById(R.id.FR_salesReport_CheckSalesBtn);
        salesReportRecyclerViewEmployees = view.findViewById(R.id.FR_salesReport_recyclerviewSalesEmployee);

        SimpleDateFormat sdfOne = new SimpleDateFormat("'Start Date:\n'dd/MM/yyyy");
        salesReportDateOneText.setText(sdfOne.format(new Date()));

        SimpleDateFormat sdfTwo = new SimpleDateFormat("'End Date:\n'dd/MM/yyyy");
        salesReportDateTwoText.setText(sdfTwo.format(new Date()));

        salesReportDateOneText.setOnClickListener(new View.OnClickListener() {
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
                        salesReportDateOneText.setText("Start Date: " + dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        salesReportDateTwoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        salesReportDateTwoText.setText("End Date: " + dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        salesReportCheckSalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if End Date is larger than Start Date
                SimpleDateFormat sdfCompare = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d1 = sdfCompare.parse(salesReportDateOneText.getText().toString().substring(12));
                    Date d2 = sdfCompare.parse(salesReportDateTwoText.getText().toString().substring(10));
                    long time_Difference = d2.getTime() - d1.getTime();
                    long days_difference = (time_Difference / (1000 * 60 * 60 * 24));
                    if(days_difference < 0){
                        Toast.makeText(requireContext(), "Cannot perform Sales Report\nMake sure End Date is not larger than Start Date", Toast.LENGTH_LONG).show();
                    } else {
                        //Get your Sales Report Here!
                        days_difference+=1;
                        dummyData.clear();
                        getSalesReport(days_difference);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void getSalesReport(long unit){
        totalSalesReportToday = 0;
        ArrayList<POSTReportOrder> todaySalesReport = new ArrayList<>();
        ServiceGenerator service = new ServiceGenerator();
        POSTReportOrder modal = new POSTReportOrder(date, (int) unit); //Tanggal dan Unit
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
                            dummyData.add(new SalesReportModel(item.getCreated_at(), item.getTableNumber(), item.getTotalPrice(), item.getOrder_detail()));
                        }
                        adapter = new SalesReportRecyclerViewAdapter(requireContext(), dummyData);
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