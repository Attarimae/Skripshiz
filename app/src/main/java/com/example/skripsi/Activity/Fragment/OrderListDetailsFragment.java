package com.example.skripsi.Activity.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
        spc = new SharedPreferencesCashier(requireContext());

        orderListDetails = new ArrayList<>();
        originalDetails = new ArrayList<>();

        for(int i=0; i < spc.fetchOrderDetails().size(); i++){
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
                OrderListItemDataModel modal = new OrderListItemDataModel(spc.fetchOrderId(), spc.fetchTableNumber(), orderListDetails);
                Call<OrderListItemDataModel> call = service.getApiService(requireContext()).patchOrderDetails(modal);
                call.enqueue(new Callback<OrderListItemDataModel>() {
                    @Override
                    public void onResponse(Call<OrderListItemDataModel> call, Response<OrderListItemDataModel> response) {
                        Log.i("Fragment OrderDetails", "onResponse:\norder_id: " +
                                response.body().getOrderId() + "\ntable_number: " +
                                response.body().getTableNumber() + "\norder_done: " +
                                response.body().getOrder_done() + "\ntotal_price: " +
                                response.body().getTotalPrice() + "\norder_status: " +
                                response.body().getOrder_status());
                        for(int i = 0; i < response.body().getOrder_detail().size(); i++){
                            Log.i("Fragment OrderDetails", "orderDetails:\nid: " +
                                    response.body().getOrder_detail().get(i).getMenuId() + "\namount: " +
                                    response.body().getOrder_detail().get(i).getMenuPrice() + "\ndescription: " +
                                    response.body().getOrder_detail().get(i).getMenuDescription() + "\nstatus: " +
                                    response.body().getOrder_detail().get(i).getStatus() + "\norder_detail_id: " +
                                    response.body().getOrder_detail().get(i).getOrder_detail_id() + "\nmenu_name: " +
                                    response.body().getOrder_detail().get(i).getMenuName() + "\nquantity: " +
                                    response.body().getOrder_detail().get(i).getMenuQuantity());
                        }
                        Toast.makeText(v.getContext(), "Succesfully updated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<OrderListItemDataModel> call, Throwable t) {
                        Log.e("Fragment OrderDetails", "onFailure: Failed to Update Orders");
                        Log.e("Fragment OrderDetails", t.getMessage());
                    }
                });
            }
        });

        Button paymentButton = view.findViewById(R.id.FR_buttonPayment);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update Order, save it to API / Endpoint, and complete the Payment if there's changes
                // Otherwise, just complete the Payment if there's no changes.
                if(!orderListDetails.equals(originalDetails)){
                    ServiceGenerator service = new ServiceGenerator();
                    OrderListItemDataModel modal = new OrderListItemDataModel(spc.fetchOrderId(), spc.fetchTableNumber(), orderListDetails);
                    Call<OrderListItemDataModel> call = service.getApiService(requireContext()).patchOrderDetails(modal);
                    call.enqueue(new Callback<OrderListItemDataModel>() {
                        @Override
                        public void onResponse(Call<OrderListItemDataModel> call, Response<OrderListItemDataModel> response) {
                            Toast.makeText(v.getContext(), "Succesfully updated orders", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OrderListItemDataModel> call, Throwable t) {
                            Log.e("Fragment OrderDetails", "onFailure: Failed to Update Orders");
                            Log.e("Fragment OrderDetails", t.getMessage());
                        }
                    });
                }
                openPaymentCashDialog();
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

    private void openPaymentCashDialog(){
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_payment_cash);
        dialog.setCancelable(true);
        dialog.show();

        EditText inputPayment = dialog.findViewById(R.id.form_payment_cash_amount);
        inputPayment.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView totalPricePayment = dialog.findViewById(R.id.form_payment_cash_total_price);
        Button buttonCompletePayment = dialog.findViewById(R.id.form_payment_cash_button);

        totalPricePayment.setText("Total Price: " + orderDetailsTotalPrice.getText());
        buttonCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPayment_TXT = inputPayment.getText().toString();
                int inputPayment_Integer = 0;
                if(!"".equals(inputPayment_TXT)){
                    inputPayment_Integer = Integer.parseInt(inputPayment_TXT);
                }
                String totalPricePayment_TXT = totalPricePayment.getText().toString().substring(17).replace(".", "");

                if(inputPayment_Integer == 0){
                    inputPayment.setError("Amount is required");
                } else if(inputPayment_Integer < Integer.parseInt(totalPricePayment_TXT)) {
                    inputPayment.setError("Insufficient amount");
                } else {
                    inputPayment.setError("null");
                    dialog.dismiss();
                    finishPayment();
                }
            }
        });
    }

    private void finishPayment(){
        //Update Order Detail Status -> Semua order dijadiin Order Has been Served
        ServiceGenerator service = new ServiceGenerator();
        for(int i = 0; i < orderListDetails.size(); i ++){
            UpdateOrderDataModel modal_orderDetailStatus = new UpdateOrderDataModel(spc.fetchOrderId(), orderListDetails.get(i).getOrder_detail_id(), "04"); // 04 = Order Has been Served
            Call<UpdateOrderDataModel> call = service.getApiService(requireContext()).updateOrderDetailsStatus(modal_orderDetailStatus);
            call.enqueue(new Callback<UpdateOrderDataModel>() {
                @Override
                public void onResponse(Call<UpdateOrderDataModel> call, Response<UpdateOrderDataModel> response) {
                    Log.i("Fragment OrderListDetails", "id: " + response.body().getOrderId() + "\namount: " +
                            response.body().getAmount() + "\ndescription: " + response.body().getDescription() +
                            "\nstatus: " + response.body().getStatus() + "\norder_detail_id: " +
                            response.body().getOrder_detail_id() + "\nmenu_name: " + response.body().getMenu_name() +
                            "\nquantity: " + response.body().getQuantity());
                    Toast.makeText(requireContext(), "Updated Order Details Status", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<UpdateOrderDataModel> call, Throwable t) {
                    Log.e("Fragment OrderListDetails", "Failed to finish payment (Updating Order Details Status)");
                    Log.e("Fragment OrderListDetails", t.getMessage());
                }
            });
        }
        //Update Order Status -> Order skrg Order Finished
        UpdateOrderDataModel modal_orderStatus = new UpdateOrderDataModel(spc.fetchOrderId(), "02");
        Call<UpdateOrderDataModel> call = service.getApiService(requireContext()).updateOrdersStatus(modal_orderStatus);
        call.enqueue(new Callback<UpdateOrderDataModel>() {
            @Override
            public void onResponse(Call<UpdateOrderDataModel> call, Response<UpdateOrderDataModel> response) {
                Log.i("Fragment OrderDetails", "onResponse:\norder_id: " +
                        response.body().getOrderId() + "\ntable_number: " +
                        response.body().getTableNumber() + "\norder_done: " +
                        response.body().getOrder_done() + "\ntotal_price: " +
                        response.body().getTotalPrice() + "\norder_status: " +
                        response.body().getOrder_status());
                for(int i = 0; i < response.body().getOrder_detail().size(); i++){
                    Log.i("Fragment OrderDetails", "orderDetails:\nid: " +
                            response.body().getOrder_detail().get(i).getMenuId() + "\namount: " +
                            response.body().getOrder_detail().get(i).getMenuPrice() + "\ndescription: " +
                            response.body().getOrder_detail().get(i).getMenuDescription() + "\nstatus: " +
                            response.body().getOrder_detail().get(i).getStatus() + "\norder_detail_id: " +
                            response.body().getOrder_detail().get(i).getOrder_detail_id() + "\nmenu_name: " +
                            response.body().getOrder_detail().get(i).getMenuName() + "\nquantity: " +
                            response.body().getOrder_detail().get(i).getMenuQuantity());
                }
                Toast.makeText(requireContext(), "Updated Order Status", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UpdateOrderDataModel> call, Throwable t) {
                Log.e("Fragment OrderListDetails", "Failed to finish payment (Updating Order Status)");
                Log.e("Fragment OrderListDetails", t.getMessage());
            }
        });

        Toast.makeText(requireActivity(), "Payment successful", Toast.LENGTH_SHORT).show();
//        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_frame, new OrderListFragment());
//        ft.commit();
    }
}