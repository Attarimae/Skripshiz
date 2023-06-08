package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.skripsi.Adapter.CheckoutGridAdapter;
import com.example.skripsi.Adapter.MenuGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        GridView checkoutGrid = view.findViewById(R.id.FR_gridCheckout);
        ArrayList<CheckoutItemModel> checkoutList = new ArrayList<CheckoutItemModel>();

        checkoutList.add(new CheckoutItemModel("Yakimeshi Mentai ", "Rp. 20.000", 2, R.drawable.ic_launcher_background));
        checkoutList.add(new CheckoutItemModel("Golden Omurice", "Rp. 25.000", 3, R.drawable.ic_launcher_background));

        CheckoutGridAdapter adapter = new CheckoutGridAdapter(requireActivity(), checkoutList);
        checkoutGrid.setAdapter(adapter);

        return view;
    }
}