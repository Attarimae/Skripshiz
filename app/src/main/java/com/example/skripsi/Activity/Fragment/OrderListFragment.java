package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.skripsi.Adapter.OrderListGridAdapter;
import com.example.skripsi.Model.OrderListItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class OrderListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        GridView orderListGrid = view.findViewById(R.id.FR_gridOrderList);
        ArrayList<OrderListItemModel> orderListArrayList = new ArrayList<OrderListItemModel>();

        orderListArrayList.add(new OrderListItemModel("Order Pertama Coy", "Rp 420.690", R.drawable.ic_launcher_background));
        orderListArrayList.add(new OrderListItemModel("Banh Udah Banh", "Rp 100.001", R.drawable.ic_launcher_background));
        orderListArrayList.add(new OrderListItemModel("Cek Kembali Ordernya", "Rp 12.345", R.drawable.ic_launcher_background));

        OrderListGridAdapter adapter = new OrderListGridAdapter(requireActivity(), orderListArrayList);
        orderListGrid.setAdapter(adapter);
        return view;
    }
}