package com.example.skripsi.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.skripsi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPromotionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    List<String> conditionType, promoType, discountType, freeItem, buyItem;
    Spinner conditionSpinner, promoSpinner, discountSpinner, freeItemSpinner, buyItemSpinner;
    TableRow tableDiscount, tableDiscountFixed, tableDiscountPercentage, tableDiscountMaxPercentage, tableFreeItem;
    // Above for init Promos, Below for init Condition
    TableRow tableWatchAdsImage, tableWatchAdsDuration, tableFollowSocialMedia, tableBuyItem, tablePostSocialMedia;
    Button addMoreWatchAdsButton; // To init the button in Watch Ads Condition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_promotion);

        TextView toolbarAddPromotion = findViewById(R.id.manage_menu);
        toolbarAddPromotion.setText("Add Promotion");

        addMoreWatchAdsButton = findViewById(R.id.addmore_watchads_button);
        initTableRow();

        discountSpinner = findViewById(R.id.promotion_discounttype_spinner);
        initDiscountSpinner();

        conditionSpinner = findViewById(R.id.promotion_conditiontype_spinner);
        initConditionSpinner();

//        promoSpinner = findViewById(R.id.promotion_type_spinner);
//        initPromoSpinner();
//
//        freeItemSpinner = findViewById(R.id.promotion_freeitem_spinner);
//        initFreeItemSpinner();
//
//        buyItemSpinner = findViewById(R.id.promotion_conditiontype_buyitem_spinner);
//        initBuyItemSpinner();

        TextView expiredDateTextView = findViewById(R.id.promotion_date_textview);
        SimpleDateFormat expiredDate = new SimpleDateFormat("dd/MM/yyyy");
        expiredDateTextView.setText(expiredDate.format(new Date()));

        expiredDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        expiredDateTextView.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void initTableRow(){
        tableDiscount = findViewById(R.id.promotion_table_discount);
        tableDiscountPercentage = findViewById(R.id.promotion_table_discount_percentage);
        tableDiscountMaxPercentage = findViewById(R.id.promotion_table_discount_maxpercentage);
        tableDiscountFixed = findViewById(R.id.promotion_table_discount_fixed);
        //tableFreeItem = findViewById(R.id.promotion_table_freeitem);

        tableWatchAdsImage = findViewById(R.id.promotion_condition_watchadsimage);
        tableWatchAdsDuration = findViewById(R.id.promotion_condition_watchadsduration);
        tableFollowSocialMedia = findViewById(R.id.promotion_condition_followsocialmedia);
        //tableBuyItem = findViewById(R.id.promotion_condition_buyitem);
        tablePostSocialMedia = findViewById(R.id.promotion_condition_postsocialmedia);
    }

    private void initDiscountSpinner(){
        discountType = new ArrayList<>();
        discountType.add(0, "Select");
        discountType.add("Percentage");
        discountType.add("Fixed Price");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPromotionActivity.this, android.R.layout.simple_spinner_item, discountType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountSpinner.setAdapter(adapter);

        discountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddPromotionActivity.this, "Selected:" + discountType.get(position), Toast.LENGTH_SHORT).show();

                switch (position){
                    case 1: //Case: Discount Percentage Selected
                        tableDiscountPercentage.setVisibility(View.VISIBLE);
                        tableDiscountMaxPercentage.setVisibility(View.VISIBLE);
                        tableDiscountFixed.setVisibility(View.GONE);
                        break;

                    case 2: //Case: Discount Fixed Price Selected
                        tableDiscountPercentage.setVisibility(View.GONE);
                        tableDiscountMaxPercentage.setVisibility(View.GONE);
                        tableDiscountFixed.setVisibility(View.VISIBLE);
                        break;

                    default:
                        tableDiscountPercentage.setVisibility(View.GONE);
                        tableDiscountMaxPercentage.setVisibility(View.GONE);
                        tableDiscountFixed.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initConditionSpinner(){
        conditionType = new ArrayList<>();
        conditionType.add(0, "Select");
        conditionType.add("Watch Image Ads");
        conditionType.add("Follow Social Media");
        conditionType.add("Post Social Media");
        //conditionType.add("Buy Item");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPromotionActivity.this, android.R.layout.simple_spinner_item, conditionType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter);

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddPromotionActivity.this, "Selected:" + conditionType.get(position), Toast.LENGTH_SHORT).show();

                switch (position){
                    case 1: //Case: Watch Image Ads Selected
                        tableWatchAdsImage.setVisibility(View.VISIBLE);
                        tableWatchAdsDuration.setVisibility(View.VISIBLE);
                        addMoreWatchAdsButton.setVisibility(View.VISIBLE);

                        tableFollowSocialMedia.setVisibility(View.GONE);
                        //tableBuyItem.setVisibility(View.GONE);
                        tablePostSocialMedia.setVisibility(View.GONE);
                        break;

                    case 2: //Case: Follow Social Media Selected
                        tableWatchAdsImage.setVisibility(View.GONE);
                        tableWatchAdsDuration.setVisibility(View.GONE);
                        addMoreWatchAdsButton.setVisibility(View.GONE);

                        tableFollowSocialMedia.setVisibility(View.VISIBLE);
                        //tableBuyItem.setVisibility(View.GONE);
                        tablePostSocialMedia.setVisibility(View.GONE);
                        break;

                    case 3: //Case: Post Social Media Selected
                        tableWatchAdsImage.setVisibility(View.GONE);
                        tableWatchAdsDuration.setVisibility(View.GONE);
                        addMoreWatchAdsButton.setVisibility(View.GONE);

                        tableFollowSocialMedia.setVisibility(View.GONE);
                        //tableBuyItem.setVisibility(View.GONE);
                        tablePostSocialMedia.setVisibility(View.VISIBLE);
                        break;

//                    case 4: //Case: Buy Item Selected
//                        tableWatchAdsImage.setVisibility(View.GONE);
//                        tableWatchAdsDuration.setVisibility(View.GONE);
//                        addMoreWatchAdsButton.setVisibility(View.GONE);
//
//                        tableFollowSocialMedia.setVisibility(View.GONE);
//                        tableBuyItem.setVisibility(View.VISIBLE);
//                        tablePostSocialMedia.setVisibility(View.GONE);
//                        break;

                    default:
                        tableWatchAdsImage.setVisibility(View.GONE);
                        tableWatchAdsDuration.setVisibility(View.GONE);
                        addMoreWatchAdsButton.setVisibility(View.GONE);

                        tableFollowSocialMedia.setVisibility(View.GONE);
                        //tableBuyItem.setVisibility(View.GONE);
                        tablePostSocialMedia.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void initPromoSpinner(){
//        promoType = new ArrayList<>();
//        promoType.add(0, "Select");
//        promoType.add("Discount");
//        promoType.add("Free Item");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPromotionActivity.this, android.R.layout.simple_spinner_item, promoType);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        promoSpinner.setAdapter(adapter);
//
//        promoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddPromotionActivity.this, "Selected:" + promoType.get(position), Toast.LENGTH_SHORT).show();
//
//                switch (position){
//                    case 1: //Case: Discount Selected
//                        tableDiscount.setVisibility(View.VISIBLE);
//                        tableFreeItem.setVisibility(View.GONE);
//                        break;
//
//                    case 2: //Case: Free Item Selected
//                        tableDiscount.setVisibility(View.GONE);
//                        tableFreeItem.setVisibility(View.VISIBLE);
//                        break;
//
//                    default:
//                        tableDiscount.setVisibility(View.GONE);
//                        tableFreeItem.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

//    private void initFreeItemSpinner(){
//        freeItem = new ArrayList<>();
//        freeItem.add(0, "Select");
//        freeItem.add("Ayam Goreng Cheese");
//        freeItem.add("Ayam Goreng Balado");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPromotionActivity.this, android.R.layout.simple_spinner_item, freeItem);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        freeItemSpinner.setAdapter(adapter);
//    }
//
//    private void initBuyItemSpinner(){
//        buyItem = new ArrayList<>();
//        buyItem.add(0, "Select");
//        buyItem.add("Ayam Goreng Cheese");
//        buyItem.add("Ayam Goreng Balado");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPromotionActivity.this, android.R.layout.simple_spinner_item, buyItem);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        buyItemSpinner.setAdapter(adapter);
//    }

    public void onImageClicked(View view) {
        // Show options for image selection (gallery or camera)
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Gallery option clicked
                    // Clear Glide cache before selecting a new image
                    Glide.get(AddPromotionActivity.this).clearMemory();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Glide.get(AddPromotionActivity.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (which == 1) {
                    // Camera option clicked
                    // Clear Glide cache before capturing a new image
                    Glide.get(AddPromotionActivity.this).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(AddPromotionActivity.this).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
                }
            }
        });
        builder.show();
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(AddPromotionActivity.this, ManagePromotionActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }
}