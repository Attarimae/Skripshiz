package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.R;

import java.util.ArrayList;

public class CheckoutGridAdapter extends ArrayAdapter<CheckoutItemModel> {

    String txtCheckoutPrice, txtPrice;
    Integer checkoutTotalQty, checkoutTotalPrice;

    public CheckoutGridAdapter(@NonNull Context context, ArrayList<CheckoutItemModel> checkoutItemModelArrayList) {
        super(context, 0, checkoutItemModelArrayList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.checkout_card_item, parent, false);
        }

        CheckoutItemModel checkoutItemModel = getItem(position);
        TextView checkoutName = listitem.findViewById(R.id.checkout_Name);
        Button addCheckoutQuantity = listitem.findViewById(R.id.checkout_AddQuantity);
        Button subCheckoutQuantity = listitem.findViewById(R.id.checkout_SubQuantity);
        TextView checkoutQuantity = listitem.findViewById(R.id.checkout_AmountQuantity);
        TextView checkoutPrice = listitem.findViewById(R.id.checkout_TotalPrice);
        ImageView checkoutImg = listitem.findViewById(R.id.checkout_Image);

        txtCheckoutPrice = checkoutItemModel.getCheckoutPrice();
        txtPrice = txtCheckoutPrice.substring(4).replace(".","");

        checkoutTotalQty = checkoutItemModel.getCheckoutQuantity();
        checkoutTotalPrice = Integer.parseInt(txtPrice) * checkoutTotalQty;
        checkoutName.setText(checkoutItemModel.getCheckoutName());
        checkoutQuantity.setText(checkoutTotalQty.toString());
        checkoutPrice.setText(checkoutTotalPrice.toString());
        checkoutImg.setImageResource(checkoutItemModel.getImgID());

        addCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutTotalQty++;
                checkoutTotalPrice = Integer.parseInt(txtPrice) * checkoutTotalQty;
                checkoutQuantity.setText(checkoutTotalQty.toString());
                checkoutPrice.setText(checkoutTotalPrice.toString());
            }
        });

        subCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkoutTotalQty > 0){
                    checkoutTotalQty--;
                    checkoutTotalPrice = Integer.parseInt(txtPrice) * checkoutTotalQty;
                    checkoutQuantity.setText(checkoutTotalQty.toString());
                    checkoutPrice.setText(checkoutTotalPrice.toString());
                } else {
                    Toast.makeText(v.getContext(), "Quantity can't less than 0", Toast.LENGTH_LONG).show();
                }
            }
        });

        return listitem;
    }
}
