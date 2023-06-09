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
import com.example.skripsi.Adapter.CheckoutGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

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
        ArrayList<CheckoutItemModel> checkoutList = new ArrayList<CheckoutItemModel>();

        SharedPreferencesCashier spc = new SharedPreferencesCashier(requireContext());
        checkoutList = spc.getCheckoutList();
        Log.i("Fragment Checkout", "onCreateView: " + checkoutList.size());

        CheckoutGridAdapter adapter = new CheckoutGridAdapter(requireActivity(), checkoutList);
        checkoutGrid.setAdapter(adapter);

        TextView checkoutTotalPrice = view.findViewById(R.id.FR_txtViewCheckoutTotalPriceRight);
        int totalPrice = 0;
        int price, quantity;

        for (CheckoutItemModel item : checkoutList) {
            price = Integer.parseInt(item.getCheckoutPrice().substring(4).replace(".", ""));
            quantity = item.getCheckoutQuantity();
            totalPrice += price * quantity;
        }

        checkoutTotalPrice.setText("Rp. " + formatPrice(totalPrice));

        return view;
    }
}