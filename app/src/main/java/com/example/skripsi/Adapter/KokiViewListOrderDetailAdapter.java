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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.Activity.Fragment.OrderListDetailsFragment;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class KokiViewListOrderDetailAdapter extends ArrayAdapter<OrderListItemDetailsDataModel> {
    private OrderListDetailsFragment orderDetailsFragment;
    private String selectedStatus;
    private String[] spinnerStatus = {
            "Order Receive",
            "Preparing Order",
            "Order Has been Cooked",
            "Order Served"
    };

    private String formatPrice(int price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###", symbols);
        return decimalFormat.format(price);
    }

    public KokiViewListOrderDetailAdapter(@NonNull Context context, @NonNull ArrayList<OrderListItemDetailsDataModel> orderListItemDetailsDataModels) {
        super(context, 0, orderListItemDetailsDataModels);
        this.orderDetailsFragment = orderDetailsFragment;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.order_details_card_item, parent, false);
        }

        OrderListItemDetailsDataModel orderListItemDetailsDataModel = getItem(position);
        TextView orderDetailsName = listitem.findViewById(R.id.orderdetails_Name);
        TextView orderDetailsDescription = listitem.findViewById(R.id.orderdetails_txtNotes);
        TextView orderDetailsQuantity = listitem.findViewById(R.id.orderdetails_AmountQuantity);
        TextView orderDetailsPrice = listitem.findViewById(R.id.orderdetails_TotalPrice);
        ImageView orderDetailsImage = listitem.findViewById(R.id.orderdetails_Image);

        Spinner orderDetailsStatus = listitem.findViewById(R.id.orderdetails_optionStatus);
        ArrayAdapter<CharSequence> spinnerAD = new ArrayAdapter<>(orderDetailsFragment.requireContext(), android.R.layout.simple_spinner_item, spinnerStatus);
        spinnerAD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderDetailsStatus.setAdapter(spinnerAD);
        final String strCurrentStatus = orderListItemDetailsDataModel.getStatus();
        final int spinnerPosition = spinnerAD.getPosition(strCurrentStatus);
        orderDetailsStatus.setSelection(spinnerPosition);
        final String txtOrderDetailsPrice = orderListItemDetailsDataModel.getMenuPrice();
        final Integer orderDetailsQty = orderListItemDetailsDataModel.getMenuQuantity();
        final Integer orderDetailsSubtotalPrice = Integer.parseInt(txtOrderDetailsPrice);

        orderDetailsName.setText(orderListItemDetailsDataModel.getMenuName());
        orderDetailsDescription.setText(orderListItemDetailsDataModel.getMenuDescription());
        orderDetailsQuantity.setText(String.valueOf(orderDetailsQty) + "pcs");
        orderDetailsPrice.setText("Rp. " + formatPrice(orderDetailsSubtotalPrice));
        Glide.with(orderDetailsFragment.requireContext())
                .load(APIConstant.BASE_URL_DOWNLOAD + orderListItemDetailsDataModel.getImgID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.smallsalad)
                .into(orderDetailsImage);
        orderDetailsStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = spinnerStatus[position];
                orderListItemDetailsDataModel.setStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected, if needed
            }
        });
        return listitem;
    }
}
