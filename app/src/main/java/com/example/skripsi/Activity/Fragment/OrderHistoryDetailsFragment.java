package com.example.skripsi.Activity.Fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.skripsi.API.SendReceipts;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.Adapter.OrderHistoryDetailsGridAdapter;
import com.example.skripsi.Model.Orders.OrderListItemDetailsDataModel;
import com.example.skripsi.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderHistoryDetailsFragment extends Fragment {

    private ArrayList<OrderListItemDetailsDataModel> orderHistoryDetails;
    private TextView orderHistoryDetailsTotalPrice, orderHistoryDetailsDate;
    private GridView gridView;
    private View view;
    private Button sendReceiptButton;

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
        view = inflater.inflate(R.layout.fragment_order_history_details, container, false);
        gridView = view.findViewById(R.id.FR_gridOrderHistoryDetails);
        spc = new SharedPreferencesCashier(requireContext());

        orderHistoryDetails = new ArrayList<>();
        for(int i=0; i < spc.fetchOrderDetails().size(); i++){
            Log.i("Fragment OrderHistoryDetails", "onCreateView: " + spc.fetchOrderId());
            orderHistoryDetails.add(spc.fetchOrderDetails().get(i));
        }

        OrderHistoryDetailsGridAdapter adapter = new OrderHistoryDetailsGridAdapter(requireContext(), orderHistoryDetails, this);
        gridView.setAdapter(adapter);

        orderHistoryDetailsTotalPrice = view.findViewById(R.id.FR_txtViewOrderHistoryDetailsTotalPriceRight);
        sendReceiptButton = view.findViewById(R.id.FR_buttonSendReceipt);
        sendReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open DialogBox "Send Receipts", input where to send (Email)
                openSendReceiptsDialog();
                sendReceiptButton.setVisibility(View.GONE);
            }
        });

        orderHistoryDetailsDate = view.findViewById(R.id.FR_gridOrderHistoryDetailsDate);

        String date = spc.fetchOrderCreatedAt();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = offsetDateTime.format(formatter);
        orderHistoryDetailsDate.setText("Order created at " + formattedDateTime);

        return view;
    }

    public void updateTotalPrice(){
        int totalPrice = 0;
        int price;
        for(OrderListItemDetailsDataModel item : orderHistoryDetails){
            price = Integer.parseInt(item.getMenuPrice());
            totalPrice += price;
        }
        orderHistoryDetailsTotalPrice.setText("Rp. " + formatPrice(totalPrice));
    }

    private void openSendReceiptsDialog(){
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_send_receipts);
        dialog.setCancelable(true);
        dialog.show();

        EditText inputEmailAddress = dialog.findViewById(R.id.form_send_receipts_email);
        inputEmailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        Button buttonCompleteSendReceipts = dialog.findViewById(R.id.form_send_receipts_button);

        buttonCompleteSendReceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailToText = inputEmailAddress.getText().toString().trim();
                if (emailToText.isEmpty()) {
                    inputEmailAddress.setError("Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
                    inputEmailAddress.setError("Email is invalid");
                } else {
                    inputEmailAddress.setError(null);
                    dialog.dismiss();
                    executeSendReceipts(emailToText);
                }
            }
        });
    }

    private void executeSendReceipts(String email){
        File screenshotFile = screenshotReceipts();
        SendReceipts sendReceipts = new SendReceipts(requireContext(), email, screenshotFile);
        sendReceipts.execute();
        sendReceiptButton.setVisibility(View.VISIBLE);
    }

    private File screenshotReceipts(){
        LinearLayout linearLayout = view.findViewById(R.id.FR_orderDetailsLayout);
        Bitmap linearLayoutBitmap = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas linearLayoutCanvas = new Canvas(linearLayoutBitmap);
        linearLayout.draw(linearLayoutCanvas);

//        Bitmap gridViewBitmap = Bitmap.createBitmap(gridView.getWidth(), gridView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas gridViewCanvas = new Canvas(gridViewBitmap);
//        gridView.draw(gridViewCanvas);
//
//        Bitmap combinedBitmap = Bitmap.createBitmap(linearLayoutBitmap.getWidth(), linearLayoutBitmap.getHeight(), linearLayoutBitmap.getConfig());
//        Canvas combinedCanvas = new Canvas(combinedBitmap);
//        combinedCanvas.drawBitmap(linearLayoutBitmap, 0, 0, null);
//        combinedCanvas.drawBitmap(gridViewBitmap, 0, 0, null);

        File file = new File(requireActivity().getExternalFilesDir(null), "receipts_order.jpg");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            linearLayoutBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}