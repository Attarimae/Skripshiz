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
import com.example.skripsi.Adapter.CheckoutGridAdapter;
import com.example.skripsi.Model.CheckoutItemModel;
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

public class CheckoutFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderListDetails;
    private ArrayList<CheckoutItemModel> checkoutList;
    private TextView checkoutTotalPrice;
    private CheckoutGridAdapter adapter;

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

        adapter = new CheckoutGridAdapter(requireActivity(), checkoutList, this);
        checkoutGrid.setAdapter(adapter);

        EditText checkoutTableNumber = view.findViewById(R.id.FR_editTextCheckoutTableNumber);
        checkoutTableNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        checkoutTotalPrice = view.findViewById(R.id.FR_txtViewCheckoutTotalPriceRight);

        adapter.notifyDataSetChanged();

        Button checkoutButton = view.findViewById(R.id.FR_buttonCheckout);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableNumber = checkoutTableNumber.getText().toString().trim();
                int number = -1;
                try {
                    number = Integer.parseInt(tableNumber);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if(checkoutList.isEmpty()){
                    Toast.makeText(v.getContext(), "Cannot create an order!", Toast.LENGTH_SHORT).show();
                } else {
                    openPaymentCashDialog(number);
                }
            }
        });

        return view;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        int price, quantity;
        for (int i = 0; i < checkoutList.size(); i++) {
            quantity = checkoutList.get(i).getCheckoutMenuQuantity();
            if(quantity != 0) {
                price = Integer.parseInt(checkoutList.get(i).getCheckoutMenuPrice().substring(4).replace(".", ""));
                totalPrice += price * quantity;
            } else {
                checkoutList.remove(i);
            }
        }
        checkoutTotalPrice.setText("Rp. " + formatPrice(totalPrice));
        adapter.notifyDataSetChanged();
    }

    private void openPaymentCashDialog(int tableNumber){
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_payment_cash);
        dialog.setCancelable(true);
        dialog.show();

        EditText inputPayment = dialog.findViewById(R.id.form_payment_cash_amount);
        inputPayment.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView totalPricePayment = dialog.findViewById(R.id.form_payment_cash_total_price);
        Button buttonCompletePayment = dialog.findViewById(R.id.form_payment_cash_button);

        totalPricePayment.setText("Total Price: " + checkoutTotalPrice.getText());
        buttonCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPayment_TXT = inputPayment.getText().toString();
                int inputPayment_Integer = 0;
                if(!"".equals(inputPayment_TXT)){
                    inputPayment_Integer = Integer.parseInt(inputPayment_TXT);
                }
                String totalPricePayment_TXT = totalPricePayment.getText().toString().substring(17).replace(".", "");
                int totalPricePayment_Integer = Integer.parseInt(totalPricePayment_TXT);

                if(inputPayment_Integer == 0){
                    inputPayment.setError("Insert amount here");
                } else if(inputPayment_Integer < totalPricePayment_Integer) {
                    inputPayment.setError("Insufficient amount");
                } else {
                    int changeAmount = inputPayment_Integer - totalPricePayment_Integer;
                    inputPayment.setError("null");
                    dialog.dismiss();
                    finishPayment(tableNumber, changeAmount);
                }
            }
        });
    }


    private void finishPayment(int tableNumber, int changeAmount){
        ServiceGenerator service = new ServiceGenerator();
        for(CheckoutItemModel item : checkoutList){
            if(item.getCheckoutMenuQuantity() > 0){
                orderListDetails.add(new OrderListItemDetailsDataModel(
                        item.getCheckoutMenuName(),
                        item.getCheckoutMenuPrice().substring(4),
                        item.getCheckoutMenuDescription(),
                        item.getCheckoutMenuQuantity(),
                        item.getImgID()
                ));
            }
        }
        OrderListItemDataModel modal = new OrderListItemDataModel(tableNumber, orderListDetails);
        Call<OrderListItemDataModel> call = service.getApiService(requireContext()).postCreateOrder(modal);
        call.enqueue(new Callback<OrderListItemDataModel>() {
            @Override
            public void onResponse(Call<OrderListItemDataModel> call, Response<OrderListItemDataModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(requireActivity(), "Payment Complete\nChanges: Rp. " + changeAmount, Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new OrderListFragment());
                    ft.commit();
                } else {
                    Toast.makeText(requireContext(), "Failed to connect the servers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListItemDataModel> call, Throwable t) {
                Log.e("Fragment Checkout", "onFailure: Failed Create Order");
                Log.e("Fragment Checkout", t.getMessage());
            }
        });
    }
}