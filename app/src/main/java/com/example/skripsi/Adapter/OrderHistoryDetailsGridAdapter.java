package com.example.skripsi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.Activity.Fragment.OrderHistoryDetailsFragment;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class OrderHistoryDetailsGridAdapter extends ArrayAdapter<OrderListItemDetailsDataModel> {

    private OrderHistoryDetailsFragment orderHistoryDetailsFragment;

    private String formatPrice(int price){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###", symbols);
        return decimalFormat.format(price);
    }

    public OrderHistoryDetailsGridAdapter(@NonNull Context context, @NonNull ArrayList<OrderListItemDetailsDataModel> orderListItemDetailsDataModels, OrderHistoryDetailsFragment orderHistoryDetailsFragment) {
        super(context, 0, orderListItemDetailsDataModels);
        this.orderHistoryDetailsFragment = orderHistoryDetailsFragment;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.order_history_details_card_item, parent, false);
        }
        OrderListItemDetailsDataModel orderHistoryItemDetailsDataModel = getItem(position);
        TextView orderHistoryDetailsName = listitem.findViewById(R.id.orderHistory_details_Name);
        TextView orderHistoryDetailsQuantity = listitem.findViewById(R.id.orderHistory_details_AmountQuantity);
        TextView orderHistoryDetailsPrice = listitem.findViewById(R.id.orderHistory_details_TotalPrice);
        ImageView orderHistoryDetailsImage = listitem.findViewById(R.id.orderHistory_details_Image);
        TextView orderHistoryDetailsDescription = listitem.findViewById(R.id.orderHistory_details_txtNotes);
        TextView orderHistoryDetailsStatus = listitem.findViewById(R.id.orderHistory_details_txtStatus);

        orderHistoryDetailsName.setText(orderHistoryItemDetailsDataModel.getMenuName());
        orderHistoryDetailsQuantity.setText("Quantity: " + orderHistoryItemDetailsDataModel.getMenuQuantity());
        orderHistoryDetailsPrice.setText("Rp. " + orderHistoryItemDetailsDataModel.getMenuPrice());
        orderHistoryDetailsDescription.setText(orderHistoryItemDetailsDataModel.getMenuDescription());
        orderHistoryDetailsStatus.setText(orderHistoryItemDetailsDataModel.getStatus());
        Glide.with(orderHistoryDetailsFragment.requireContext())
                .load(APIConstant.BASE_URL_DOWNLOAD + orderHistoryItemDetailsDataModel.getImgID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.smallsalad)
                .into(orderHistoryDetailsImage);

        orderHistoryDetailsFragment.updateTotalPrice();
        return listitem;
    }
}
