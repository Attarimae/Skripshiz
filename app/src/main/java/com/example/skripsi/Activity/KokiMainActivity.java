package com.example.skripsi.Activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.KokiAdapter;
import com.example.skripsi.Adapter.KokiViewListOrderDetailAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KokiMainActivity extends AppCompatActivity implements KokiAdapter.StatusUpdateCallback {


    @Override
    public void onStatusUpdated() {
        adapter.notifyDataSetChanged();
    }

    private TextView kokiTextView;
    private ListView listView;

    private ArrayList<OrderListItemDataModel> orderListDetails;
    private KokiAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koki_main);
        kokiTextView = findViewById(R.id.manage_menu);
        kokiTextView.setText("On Going Order");

        listView = findViewById(R.id.ordersListView);
        orderListDetails = new ArrayList<>();

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<OrderListItemDataModel>> call = service.getApiService(this).getOngoingOrderList();
        call.enqueue(new Callback<ArrayList<OrderListItemDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderListItemDataModel>> call, Response<ArrayList<OrderListItemDataModel>> response) {
                if (response.isSuccessful()) {
                    int orderListSize = response.body().size();
                    if (orderListSize == 0) {
                        Toast.makeText(KokiMainActivity.this, "There's no orders right now...", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < orderListSize; i++) {
                            if (response.body().get(i).getOrder_status().equals("Order Ongoing")) {
                                orderListDetails.add(new OrderListItemDataModel(
                                        response.body().get(i).getOrderId(),
                                        response.body().get(i).getTableNumber(),
                                        response.body().get(i).getOrder_detail()
                                ));
                            }
                        }
                        adapter = new KokiAdapter(KokiMainActivity.this, orderListDetails, KokiMainActivity.this);
                        listView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(KokiMainActivity.this, "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderListItemDataModel>> call, Throwable t) {
                Toast.makeText(KokiMainActivity.this, "Failed to connect the servers", Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout = findViewById(R.id.mainKokiRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                ServiceGenerator service = new ServiceGenerator();
                Call<ArrayList<OrderListItemDataModel>> call = service.getApiService(KokiMainActivity.this).getOngoingOrderList();
                call.enqueue(new Callback<ArrayList<OrderListItemDataModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OrderListItemDataModel>> call, Response<ArrayList<OrderListItemDataModel>> response) {
                        if (response.isSuccessful()) {
                            orderListDetails.clear();
                            int orderListSize = response.body().size();
                            if (orderListSize == 0) {
                                Toast.makeText(KokiMainActivity.this, "There's no orders right now...", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < orderListSize; i++) {
                                    if (response.body().get(i).getOrder_status().equals("Order Ongoing")) {
                                        orderListDetails.add(new OrderListItemDataModel(
                                                response.body().get(i).getOrderId(),
                                                response.body().get(i).getTableNumber(),
                                                response.body().get(i).getOrder_detail()
                                        ));
                                    }
                                }
                                adapter = new KokiAdapter(KokiMainActivity.this, orderListDetails, KokiMainActivity.this);
                                listView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        } else {
                            Toast.makeText(KokiMainActivity.this, "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OrderListItemDataModel>> call, Throwable t) {
                        Toast.makeText(KokiMainActivity.this, "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
