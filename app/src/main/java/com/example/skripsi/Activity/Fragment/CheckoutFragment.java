package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.CheckoutGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderListDetails;
    private ArrayList<CheckoutItemModel> checkoutList;
    private TextView checkoutTotalPrice;

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
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        GridView checkoutGrid = view.findViewById(R.id.FR_gridCheckout);
        SharedPreferencesCashier spc = new SharedPreferencesCashier(requireContext());
        orderListDetails = new ArrayList<>();
        checkoutList = spc.fetchCheckoutList();
        Log.i("Fragment Checkout", "onCreateView: " + checkoutList.size());

        CheckoutGridAdapter adapter = new CheckoutGridAdapter(requireActivity(), checkoutList, this);
        checkoutGrid.setAdapter(adapter);

        EditText checkoutTableNumber = view.findViewById(R.id.FR_editTextCheckoutTableNumber);
        checkoutTotalPrice = view.findViewById(R.id.FR_txtViewCheckoutTotalPriceRight);

        adapter.notifyDataSetChanged();

        Button checkoutButton = view.findViewById(R.id.FR_buttonCheckout);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceGenerator service = new ServiceGenerator();
                Integer tableNumber = Integer.valueOf(checkoutTableNumber.toString());
                for(CheckoutItemModel item : checkoutList){
                    orderListDetails.add(new OrderListItemDetailsDataModel(
                            item.getCheckoutMenuName(),
                            item.getCheckoutMenuPrice(),
                            item.getCheckoutMenuDescription(),
                            item.getCheckoutMenuQuantity(),
                            item.getImgID()
                            ));
                }

                OrderListItemDataModel modal = new OrderListItemDataModel(tableNumber, orderListDetails);
                Call<OrderListItemDataModel> call = service.getApiService(requireContext()).postCreateOrder(modal);
                call.enqueue(new Callback<OrderListItemDataModel>() {
                    @Override
                    public void onResponse(Call<OrderListItemDataModel> call, Response<OrderListItemDataModel> response) {
                        checkoutList.clear();
                        Toast.makeText(v.getContext(), "Successfully created Order", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new OrderListFragment());
                        ft.commit();
                    }

                    @Override
                    public void onFailure(Call<OrderListItemDataModel> call, Throwable t) {
                        Log.e("Fragment Checkout", "onFailure: Failed Create Order");
                        Log.e("Fragment Checkout", t.getMessage());
                    }
                });

            }
        });

        return view;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        int price, quantity;

        for (CheckoutItemModel item : checkoutList) {
            price = Integer.parseInt(item.getCheckoutMenuPrice().substring(4).replace(".", ""));
            quantity = item.getCheckoutMenuQuantity();
            totalPrice += price * quantity;
        }

        checkoutTotalPrice.setText("Rp. " + formatPrice(totalPrice));
    }
}