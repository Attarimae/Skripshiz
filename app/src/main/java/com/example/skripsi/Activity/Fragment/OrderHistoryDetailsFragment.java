package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.OrderHistoryDetailsGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class OrderHistoryDetailsFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderHistoryDetails;
    private TextView orderHistoryDetailsTotalPrice;

    SharedPreferencesCashier spc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String formatPrice(int price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###", symbols);
        return decimalFormat.format(price);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_details, container, false);
        GridView orderHistoryDetailsGrid = view.findViewById(R.id.FR_gridOrderHistoryDetails);
        spc = new SharedPreferencesCashier(requireContext());

        orderHistoryDetails = new ArrayList<>();
        for(int i=0; i < spc.fetchOrderDetails().size(); i++){
            Log.i("Fragment OrderHistoryDetails", "onCreateView: " + spc.fetchOrderId());
            orderHistoryDetails.add(spc.fetchOrderDetails().get(i));
        }

        OrderHistoryDetailsGridAdapter adapter = new OrderHistoryDetailsGridAdapter(requireContext(), orderHistoryDetails, this);
        orderHistoryDetailsGrid.setAdapter(adapter);

        orderHistoryDetailsTotalPrice = view.findViewById(R.id.FR_txtViewOrderHistoryDetailsTotalPriceRight);
        return view;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        int price;
        for(OrderListItemDetailsDataModel item : orderHistoryDetails){
            price = Integer.parseInt(item.getMenuPrice());
            totalPrice += price;
        }
        orderHistoryDetailsTotalPrice.setText("Rp. " + formatPrice(totalPrice));
    }
}