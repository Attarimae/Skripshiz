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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
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

    static class ViewHolder {
        TextView checkoutName;
        Button addCheckoutQuantity;
        Button subCheckoutQuantity;
        TextView checkoutQuantity;
        TextView checkoutPrice;
        ImageView checkoutImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checkout_card_item, parent, false);
            holder = new ViewHolder();
            holder.checkoutName = convertView.findViewById(R.id.checkout_Name);
            holder.addCheckoutQuantity = convertView.findViewById(R.id.checkout_AddQuantity);
            holder.subCheckoutQuantity = convertView.findViewById(R.id.checkout_SubQuantity);
            holder.checkoutQuantity = convertView.findViewById(R.id.checkout_AmountQuantity);
            holder.checkoutPrice = convertView.findViewById(R.id.checkout_TotalPrice);
            holder.checkoutImg = convertView.findViewById(R.id.checkout_Image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CheckoutItemModel checkoutItemModel = getItem(position);
        final String txtCheckoutPrice = checkoutItemModel.getCheckoutMenuPrice();
        final String replace_txtPrice = txtCheckoutPrice.substring(4).replace(".", "");
        final Integer checkoutTotalQty = checkoutItemModel.getCheckoutMenuQuantity();
        final Integer checkoutSubtotalPrice = Integer.parseInt(replace_txtPrice) * checkoutTotalQty;

        holder.checkoutName.setText(checkoutItemModel.getCheckoutMenuName());
        holder.checkoutQuantity.setText(String.valueOf(checkoutTotalQty));
        holder.checkoutPrice.setText("Rp. " + formatPrice(checkoutSubtotalPrice));

        Glide.with(checkoutFragment.requireContext())
                .load(APIConstant.BASE_URL_DOWNLOAD + checkoutItemModel.getImgID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.smallsalad)
                .into(holder.checkoutImg);

        holder.addCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutItemModel.setCheckoutMenuQuantity(checkoutItemModel.getCheckoutMenuQuantity() + 1);
                int updatedQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                int updatedPrice = Integer.parseInt(replace_txtPrice) * updatedQuantity;
                holder.checkoutQuantity.setText(String.valueOf(updatedQuantity));
                holder.checkoutPrice.setText("Rp. " + formatPrice(updatedPrice));
                checkoutFragment.updateTotalPrice();
            }
        });

        holder.subCheckoutQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                if (currentQuantity > 0) {
                    checkoutItemModel.setCheckoutMenuQuantity(currentQuantity - 1);
                    int updatedQuantity = checkoutItemModel.getCheckoutMenuQuantity();
                    int updatedPrice = Integer.parseInt(replace_txtPrice) * updatedQuantity;
                    holder.checkoutQuantity.setText(String.valueOf(updatedQuantity));
                    holder.checkoutPrice.setText("Rp. " + formatPrice(updatedPrice));
                    checkoutFragment.updateTotalPrice();
                } else {
                    Toast.makeText(v.getContext(), "Quantity can't be less than 0", Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }
}
