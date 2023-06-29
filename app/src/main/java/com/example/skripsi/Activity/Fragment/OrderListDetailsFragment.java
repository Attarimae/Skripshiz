package com.example.skripsi.Activity.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Activity.CashierMainActivity;
import com.example.skripsi.Activity.RestaurantRegister;
import com.example.skripsi.Adapter.OrderListDetailsGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.Model.Orders.UpdateOrderDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListDetailsFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderListDetails, originalDetails;
    private TextView orderDetailsTotalPrice;

    SharedPreferencesCashier spc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String formatPrice(int price) {
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
        spc = new SharedPreferencesCashier(requireContext());

        orderListDetails = new ArrayList<>();
        originalDetails = new ArrayList<>();

        for (int i = 0; i < spc.fetchOrderDetails().size(); i++) {
            Log.i("Fragment OrderListDetails", "onCreateView: " + spc.fetchOrderId());
            originalDetails.add(spc.fetchOrderDetails().get(i));
            orderListDetails.add(spc.fetchOrderDetails().get(i));
        }

        OrderListDetailsGridAdapter adapter = new OrderListDetailsGridAdapter(requireContext(), orderListDetails, this);
        orderDetailsGrid.setAdapter(adapter);

        orderDetailsTotalPrice = view.findViewById(R.id.FR_txtViewOrderDetailsTotalPriceRight);
        Button updateButton = view.findViewById(R.id.FR_buttonUpdateOrder);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update Order and save it to API / Endpoint if there's changes
                // Otherwise, send Toast if there's no changes.
                ServiceGenerator service = new ServiceGenerator();

                for (OrderListItemDetailsDataModel item : orderListDetails) {
                    switch (item.getStatus()) {
                        case "Order Receive":
                            item.setStatus("01");
                            break;
                        case "Preparing Order":
                            item.setStatus("02");
                            break;
                        case "Order Has been Cooked":
                            item.setStatus("03");
                            break;
                        case "Order Served":
                            item.setStatus("04");
                            break;
                    }
                    UpdateOrderDataModel modal = new UpdateOrderDataModel(spc.fetchOrderId(),item.getOrder_detail_id(), item.getStatus());
                    Call<UpdateOrderDataModel> call = service.getApiService(requireContext()).updateOrderDetailsStatus(modal);
                    call.enqueue(new Callback<UpdateOrderDataModel>() {
                        @Override
                        public void onResponse(Call<UpdateOrderDataModel> call, Response<UpdateOrderDataModel> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(v.getContext(), "Succesfully updated", Toast.LENGTH_SHORT).show();
                                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new OrderListFragment());
                                ft.commit();
                            } else {
                                Toast.makeText(v.getContext(), "Failed to update status to the servers", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateOrderDataModel> call, Throwable t) {
                            Toast.makeText(v.getContext(), "Failed to update status to the servers", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

        //        Button paymentButton = view.findViewById(R.id.FR_buttonPayment);
        //
        //        paymentButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                // Update Order, save it to API / Endpoint, and complete the Payment if there's changes
        //                // Otherwise, just complete the Payment if there's no changes.
        //                if(!orderListDetails.equals(originalDetails)){
        //                    ServiceGenerator service = new ServiceGenerator();
        //                    OrderListItemDataModel modal = new OrderListItemDataModel(spc.fetchOrderId(), spc.fetchTableNumber(), orderListDetails);
        //                    Call<OrderListItemDataModel> call = service.getApiService(requireContext()).patchOrderDetails(modal);
        //                    call.enqueue(new Callback<OrderListItemDataModel>() {
        //                        @Override
        //                        public void onResponse(Call<OrderListItemDataModel> call, Response<OrderListItemDataModel> response) {
        //                            Toast.makeText(v.getContext(), "Succesfully updated orders", Toast.LENGTH_SHORT).show();
        //                        }
        //
        //                        @Override
        //                        public void onFailure(Call<OrderListItemDataModel> call, Throwable t) {
        //                            Log.e("Fragment OrderDetails", "onFailure: Failed to Update Orders");
        //                            Log.e("Fragment OrderDetails", t.getMessage());
        //                        }
        //                    });
        //                }
        //                //openPaymentCashDialog();
        //            }
        //        });

        return view;
    }
}