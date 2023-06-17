package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skripsi.Activity.Fragment.OrderListDetailsFragment;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class OrderListDetailsGridAdapter extends ArrayAdapter<OrderListItemDetailsDataModel> {

    private OrderListDetailsFragment orderListDetailsFragment;

    private final String[] spinnerStatus = {
            "Order Receive", // 01
            "Order Has been Prepare", // 02
            "Order Has been Cooking", // 03
            "Order Has been Serve" // 04
    };

    private final String[] spinnerID_Status = {
            "01", "02", "03", "04"
    };

    private String formatPrice(int price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###", symbols);
        return decimalFormat.format(price);
    }

    public OrderListDetailsGridAdapter(@NonNull Context context, @NonNull ArrayList<OrderListItemDetailsDataModel> orderListItemDetailsDataModels, OrderListDetailsFragment orderListDetailsFragment) {
        super(context, 0, orderListItemDetailsDataModels);
        this.orderListDetailsFragment = orderListDetailsFragment;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.order_details_card_item, parent, false);
        }

        OrderListItemDetailsDataModel orderListItemDetailsDataModel = getItem(position);
        TextView orderDetailsName = listitem.findViewById(R.id.orderdetails_Name);
        Button addOrderDetailsQuantity = listitem.findViewById(R.id.orderdetails_AddQuantity);
        Button subOrderDetailsQuantity = listitem.findViewById(R.id.orderdetails_SubQuantity);
        TextView orderDetailsQuantity = listitem.findViewById(R.id.orderdetails_AmountQuantity);
        TextView orderDetailsPrice = listitem.findViewById(R.id.orderdetails_TotalPrice);
        ImageView orderDetailsImage = listitem.findViewById(R.id.orderdetails_Image);

        Spinner orderDetailsStatus = listitem.findViewById(R.id.orderdetails_optionStatus);
        ArrayAdapter<CharSequence> spinnerAD = new ArrayAdapter<>(orderListDetailsFragment.requireContext(), android.R.layout.simple_spinner_item, spinnerStatus);
        spinnerAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderDetailsStatus.setAdapter(spinnerAD);
        final String strCurrentStatus = orderListItemDetailsDataModel.getStatus();
        final int spinnerPosition = spinnerAD.getPosition(strCurrentStatus);
        orderDetailsStatus.setSelection(spinnerPosition);
        orderDetailsStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderListItemDetailsDataModel.setStatus(spinnerID_Status[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Auto Generated
            }
        });

        final String txtOrderDetailsPrice = orderListItemDetailsDataModel.getMenuPrice();
        final Integer orderDetailsQty = orderListItemDetailsDataModel.getMenuQuantity();
        final Integer orderDetailsSubtotalPrice = Integer.parseInt(txtOrderDetailsPrice) * orderDetailsQty;

        orderDetailsName.setText(orderListItemDetailsDataModel.getMenuName());
        orderDetailsQuantity.setText(String.valueOf(orderDetailsQty));
        orderDetailsPrice.setText("Rp. " + formatPrice(orderDetailsSubtotalPrice));
        orderDetailsImage.setImageResource(orderListItemDetailsDataModel.getImgID());

        orderListDetailsFragment.updateTotalPrice();

        addOrderDetailsQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListItemDetailsDataModel.setMenuQuantity(orderListItemDetailsDataModel.getMenuQuantity() + 1);
                int updatedQuantity = orderListItemDetailsDataModel.getMenuQuantity();
                int updatedPrice = Integer.parseInt(txtOrderDetailsPrice) * updatedQuantity;
                orderDetailsQuantity.setText(String.valueOf(updatedQuantity));
                orderDetailsPrice.setText("Rp. " + formatPrice(updatedPrice));
                orderListDetailsFragment.updateTotalPrice();
            }
        });

        subOrderDetailsQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = orderListItemDetailsDataModel.getMenuQuantity();
                if (currentQuantity > 0) {
                    orderListItemDetailsDataModel.setMenuQuantity(orderListItemDetailsDataModel.getMenuQuantity() - 1);
                    int updatedQuantity = orderListItemDetailsDataModel.getMenuQuantity();
                    int updatedPrice = Integer.parseInt(txtOrderDetailsPrice) * updatedQuantity;
                    orderDetailsQuantity.setText(String.valueOf(updatedQuantity));
                    orderDetailsPrice.setText("Rp. " + formatPrice(updatedPrice));
                    orderListDetailsFragment.updateTotalPrice();
                } else {
                    Toast.makeText(v.getContext(), "Quantity can't be less than 0", Toast.LENGTH_LONG).show();
                }
            }
        });

        return listitem;
    }
}
