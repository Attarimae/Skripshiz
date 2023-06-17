package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.OrderDetailsGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderListDetails, updatedOrderDetails;
    private TextView orderDetailsTotalPrice;

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
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        GridView orderDetailsGrid = view.findViewById(R.id.FR_gridOrderDetails);
        SharedPreferencesCashier spc = new SharedPreferencesCashier(requireContext());
        orderListDetails = new ArrayList<>();
        orderListDetails.addAll(spc.fetchOrderDetails());
        Log.i("Fragment OrderDetails", "onCreateView:\n" + orderListDetails.size());

        OrderDetailsGridAdapter adapter = new OrderDetailsGridAdapter(requireContext(), orderListDetails, this);
        orderDetailsGrid.setAdapter(adapter);

        orderDetailsTotalPrice = view.findViewById(R.id.FR_txtViewOrderDetailsTotalPriceRight);
        Button updateButton = view.findViewById(R.id.FR_buttonUpdateOrder);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ngeupdate order dan simpan ke API lagi klo ada update.
                // Otherwise, lempar Toast "Orang Gila".

            }
        });

        Button paymentButton = view.findViewById(R.id.FR_buttonPayment);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ngeupdate order, simpan ke API, lalu complete Payment klo ada update.
                // Otherwise, just complete the Payment.

            }
        });

        return view;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        int price, quantity;
        for(OrderListItemDetailsDataModel item : orderListDetails){
            price = Integer.parseInt(item.getMenuPrice());
            quantity = item.getMenuQuantity();
            totalPrice += price * quantity;
        }
        orderDetailsTotalPrice.setText("Rp. " + formatPrice(totalPrice));
    }
}