package com.example.skripsi.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Activity.Fragment.OrderListDetailsFragment;
import com.example.skripsi.Adapter.OrderListGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    private ArrayList<OrderListItemDataModel> orderListArrayList;
    private OrderListItemDataModel selectedOrderListArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        GridView orderListGrid = findViewById(R.id.FR_gridOrderList);
        orderListArrayList = new ArrayList<>();

        OrderListGridAdapter adapter = new OrderListGridAdapter(OrderListActivity.this, orderListArrayList);
        orderListGrid.setAdapter(adapter);

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<OrderListItemDataModel>> call = service.getApiService(OrderListActivity.this).getOngoingOrderList();
        call.enqueue(new Callback<ArrayList<OrderListItemDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderListItemDataModel>> call, Response<ArrayList<OrderListItemDataModel>> response) {
                if(response.isSuccessful()){
                    int orderListSize = response.body().size();
                    if(orderListSize == 0){
                        Toast.makeText(OrderListActivity.this, "There's no orders right now...", Toast.LENGTH_SHORT).show();
                    } else {
                        for(int i=0; i < orderListSize; i++){
                            if(response.body().get(i).getOrder_status().equals("Order Ongoing")){
                                orderListArrayList.add(new OrderListItemDataModel(
                                        response.body().get(i).getOrderId(),
                                        response.body().get(i).getTableNumber(),
                                        response.body().get(i).getTotalPrice(),
                                        response.body().get(i).getOrder_detail()
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(OrderListActivity.this, "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderListItemDataModel>> call, Throwable t) {
                Log.e("Activity OrderList", "onFailure: Failed Get Order List");
                Log.e("Activity OrderList", t.getMessage());
            }
        });

        //Search View
        SearchView orderListSearchView = findViewById(R.id.FT_searchViewOrderList);
        orderListSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        orderListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<OrderListItemDataModel> filteredOrderList = new ArrayList<>();
                for(OrderListItemDataModel filter : orderListArrayList) {
                    if (String.valueOf(filter.getTableNumber()).toLowerCase().contains(newText.toLowerCase())) {
                        filteredOrderList.add(filter);
                    }
                }
                OrderListGridAdapter adapter = new OrderListGridAdapter(OrderListActivity.this, filteredOrderList);
                orderListGrid.setAdapter(adapter);
                if(filteredOrderList.isEmpty()){
                    Toast.makeText(OrderListActivity.this, "Filter not found...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        orderListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOrderListArrayList = adapter.getItem(position);
                openOrderDetails(selectedOrderListArrayList.getOrder_detail(), selectedOrderListArrayList.getTableNumber(), selectedOrderListArrayList.getOrderId());
            }
        });
    }

    private void openOrderDetails(ArrayList<OrderListItemDetailsDataModel> order_detail, int tableNumber, String orderId){
        SharedPreferencesCashier spc = new SharedPreferencesCashier(OrderListActivity.this);
        spc.saveOrderId(orderId);
        spc.saveTableNumber(tableNumber);
        spc.saveOrderDetails(order_detail);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new OrderListDetailsFragment()).addToBackStack("OrderListDetails");
        ft.commit();
    }
}
