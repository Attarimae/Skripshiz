package com.example.skripsi.Activity.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.Adapter.OrderListGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {

    private ArrayList<OrderListItemDataModel> orderListArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        GridView orderListGrid = view.findViewById(R.id.FR_gridOrderList);
        orderListArrayList = new ArrayList<>();

        orderListArrayList.add(new OrderListItemDataModel("101", "Rp 420.690", R.drawable.ic_launcher_background));
        orderListArrayList.add(new OrderListItemDataModel("200", "Rp 100.001", R.drawable.ic_launcher_background));
//        orderListArrayList.add(new OrderListItemDataModel("256", "Rp 12.345", R.drawable.ic_launcher_background));

        OrderListGridAdapter adapter = new OrderListGridAdapter(requireActivity(), orderListArrayList);
        orderListGrid.setAdapter(adapter);

        ServiceGenerator service = new ServiceGenerator();
        Call<ArrayList<OrderListItemDataModel>> call = service.getApiService(requireContext()).getAllOrderList();
        call.enqueue(new Callback<ArrayList<OrderListItemDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderListItemDataModel>> call, Response<ArrayList<OrderListItemDataModel>> response) {
                int orderListSize = response.body().size();
                for(int i=0; i < orderListSize; i++){
                    orderListArrayList.add(new OrderListItemDataModel(
                            response.body().get(i).getTableNumber(),
                            response.body().get(i).getOrder_detail()
                    ));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrderListItemDataModel>> call, Throwable t) {
                Log.e("Fragment OrderList", "onFailure: Failed Get Order List");
                Log.e("Fragment OrderList", t.getMessage());
            }
        });
        //Search View
        SearchView orderListSearchView = view.findViewById(R.id.FT_searchViewOrderList);
        orderListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<OrderListItemDataModel> filteredOrderList = new ArrayList<>();
                for(OrderListItemDataModel filter : orderListArrayList){
                    if(String.valueOf(filter.getTableNumber()).toLowerCase().contains(newText.toLowerCase())){
                        filteredOrderList.add(filter);
                        OrderListGridAdapter adapter = new OrderListGridAdapter(requireActivity(), filteredOrderList);
                        orderListGrid.setAdapter(adapter);
                    }
                } if(filteredOrderList.isEmpty()){
                    Toast.makeText(requireContext(), "Filter not found...", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return view;
    }
}