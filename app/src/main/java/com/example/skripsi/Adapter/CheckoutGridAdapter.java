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

import com.example.skripsi.Activity.Fragment.CheckoutFragment;
import com.example.skripsi.Model.CheckoutItemModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class CheckoutGridAdapter extends ArrayAdapter<CheckoutItemModel> {

    private CheckoutFragment checkoutFragment;

    private String formatPrice(int price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###", symbols);
        return decimalFormat.format(price);
    }

    public CheckoutGridAdapter(@NonNull Context context, ArrayList<CheckoutItemModel> checkoutList, CheckoutFragment checkoutFragment) {
        super(context, 0, checkoutList);
        this.checkoutFragment = checkoutFragment;
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

        final String txtCheckoutPrice = checkoutItemModel.getCheckoutMenuPrice();
        final String replace_txtPrice = txtCheckoutPrice.substring(4).replace(".", "");

        final Integer checkoutTotalQty = checkoutItemModel.getCheckoutMenuQuantity();
        final Integer checkoutSubtotalPrice = Integer.parseInt(replace_txtPrice) * checkoutTotalQty;

        checkoutName.setText(checkoutItemModel.getCheckoutMenuName());
        checkoutQuantity.setText(String.valueOf(checkoutTotalQty));
        checkoutPrice.setText("Rp. " + formatPrice(checkoutSubtotalPrice));
        checkoutImg.setImageResource(checkoutItemModel.getImgID());

        addCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutItemModel.setCheckoutMenuQuantity(checkoutItemModel.getCheckoutMenuQuantity() + 1);
                int updatedQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                int updatedPrice = Integer.parseInt(replace_txtPrice) * updatedQuantity;
                checkoutQuantity.setText(String.valueOf(updatedQuantity));
                checkoutPrice.setText("Rp. " + formatPrice(updatedPrice));
                checkoutFragment.updateTotalPrice();
            }
        });

        subCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                if (currentQuantity > 0) {
                    checkoutItemModel.setCheckoutMenuQuantity(currentQuantity - 1);
                    int updatedQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                    int updatedPrice = Integer.parseInt(replace_txtPrice) * updatedQuantity;
                    checkoutQuantity.setText(String.valueOf(updatedQuantity));
                    checkoutPrice.setText("Rp. " + formatPrice(updatedPrice));
                    checkoutFragment.updateTotalPrice();
                } else {
                    Toast.makeText(v.getContext(), "Quantity can't be less than 0", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkoutFragment.updateTotalPrice();

        return listitem;
    }
}
