package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Activity.Fragment.OrderListFragment;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.Model.Orders.UpdateOrderDataModel;
import com.example.skripsi.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KokiViewListOrderDetailAdapter extends ArrayAdapter<OrderListItemDetailsDataModel> {
    private static class ViewHolder {
        TextView orderDetailsNameTextView;
        TextView orderDetailsQuantityTextView;
        TextView orderDetailsTxtNotes;
        TextView orderDetailsTxtStatus;
        Spinner orderDetailsOptionStatus;
        ImageView orderdetails_Image;
    }

    private String[] spinnerStatus = {
            "Order Received",
            "Preparing Order",
            "Order Being Cook",
            "Order Served"
    };
    public interface StatusUpdateCallback {
        void onStatusUpdated();
    }

    private int first;
    private StatusUpdateCallback statusUpdateCallback;
    private String orderId;

    public KokiViewListOrderDetailAdapter(Context context, ArrayList<OrderListItemDetailsDataModel> orderDetailsList, String orderId) {
        super(context, 0, orderDetailsList);
        this.orderId = orderId;
        this.first=0;
    }

    public void setStatusUpdateCallback(StatusUpdateCallback callback) {
        this.statusUpdateCallback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        OrderListItemDetailsDataModel orderDetail = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.koki_details, parent, false);
            viewHolder.orderDetailsNameTextView = convertView.findViewById(R.id.orderdetails_Name);
            viewHolder.orderDetailsQuantityTextView = convertView.findViewById(R.id.orderdetails_AmountQuantity);
            viewHolder.orderDetailsTxtNotes = convertView.findViewById(R.id.orderdetails_txtNotes);
            viewHolder.orderDetailsTxtStatus = convertView.findViewById(R.id.orderdetails_txtStatus);
            viewHolder.orderDetailsOptionStatus = convertView.findViewById(R.id.orderdetails_optionStatus);
            viewHolder.orderdetails_Image = convertView.findViewById(R.id.orderdetails_Image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.orderDetailsNameTextView.setText(orderDetail.getMenuName());
        viewHolder.orderDetailsQuantityTextView.setText("Quantity: " + String.valueOf(orderDetail.getMenuQuantity()));
        viewHolder.orderDetailsTxtNotes.setText("Notes: ");
        viewHolder.orderDetailsTxtStatus.setText("Order Status");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerStatus);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.orderDetailsOptionStatus.setAdapter(spinnerAdapter);
        Glide.with(getContext())
                .load(APIConstant.BASE_URL_DOWNLOAD + orderDetail.getImgID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .error(R.drawable.smallsalad))
                .into(viewHolder.orderdetails_Image);

        // Handle spinner selection
        viewHolder.orderDetailsOptionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = spinnerStatus[position];
                orderDetail.setStatus(selectedStatus);
                ServiceGenerator service = new ServiceGenerator();
                switch (orderDetail.getStatus()) {
                    case "Order Received":
                        orderDetail.setStatus("01");
                        break;
                    case "Preparing Order":
                        orderDetail.setStatus("02");
                        break;
                    case "Order Being Cook":
                        orderDetail.setStatus("03");
                        break;
                    case "Order Served":
                        orderDetail.setStatus("04");
                        break;
                }
                UpdateOrderDataModel modal = new UpdateOrderDataModel(orderId, orderDetail.getOrder_detail_id(), orderDetail.getStatus());
                Call<UpdateOrderDataModel> call = service.getApiService(getContext()).updateOrderDetailsStatus(modal);
                call.enqueue(new Callback<UpdateOrderDataModel>() {
                    @Override
                    public void onResponse(Call<UpdateOrderDataModel> call, Response<UpdateOrderDataModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                            if (statusUpdateCallback != null && first>0) {
                                statusUpdateCallback.onStatusUpdated();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to update status to the server", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderDataModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to update status to the server", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return convertView;
    }
}