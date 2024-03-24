package com.example.skripsi.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Model.ErrorResponse;
import com.example.skripsi.Model.Menus.MenuItemModel;
import com.example.skripsi.Model.Menus.MenuItemModelWithoutId;
import com.example.skripsi.Model.Promotion.Discount;
import com.example.skripsi.Model.Promotion.PostSocialMedia;
import com.example.skripsi.Model.Promotion.PromotionItemModel;
import com.example.skripsi.Model.Promotion.SocialMediaAds;
import com.example.skripsi.Model.Promotion.WatchImageAds;
import com.example.skripsi.Model.SuccessResponse;
import com.example.skripsi.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPromotionActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    private ArrayList<WatchImageAds> watchImageAds;
    private ArrayList<PostSocialMedia> postSocialMedia;
    private ArrayList<SocialMediaAds> socialMediaAds;
    SessionManager sm;
    ImageView promotionImageView;

    List<String> conditionType, promoType, discountType, freeItem, buyItem;
    Spinner conditionSpinner, promoSpinner, discountSpinner, freeItemSpinner, buyItemSpinner;
    TableRow tableDiscount, tableDiscountFixed, tableDiscountPercentage, tableDiscountMaxPercentage, tableFreeItem;
    // Above for init Promos, Below for init Condition
    TableRow tableWatchAdsImage, tableWatchAdsDuration, tableFollowSocialMedia, tableBuyItem, tablePostSocialMedia;
    Button addMoreWatchAdsButton; // To init the button in Watch Ads Condition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sm = new SessionManager(this);
        setContentView(R.layout.activity_add_new_promotion);

        TextView toolbarAddPromotion = findViewById(R.id.manage_menu);
        toolbarAddPromotion.setText("Add Promotion");

        promotionImageView = findViewById(R.id.promtion_imageview);

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
    }

    private void readDataFromTableRow(TableRow tableRow, ArrayList<Integer> dataList) {
        // Iterate through the child views of the TableRow
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            View childView = tableRow.getChildAt(i);
            // Check the type of the child view
            if (childView instanceof EditText) {
                // If it's an EditText, you can read data from it
                EditText editText = (EditText) childView;
                Integer text = Integer.valueOf(editText.getText().toString());
                // Add the text to the ArrayList
                dataList.add(text);
            }
        }
    }

    private void readDataStringFromTableRow(TableRow tableRow, ArrayList<String> dataList) {
        // Iterate through the child views of the TableRow
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            View childView = tableRow.getChildAt(i);
            // Check the type of the child view
            if (childView instanceof EditText) {
                // If it's an EditText, you can read data from it
                EditText editText = (EditText) childView;
                String text = editText.getText().toString();
                // Add the text to the ArrayList
                dataList.add(text);
            }
        }
    }

    private void readDataImageFromTableRow(TableRow tableRow, ArrayList<ImageView> dataList) {
        // Iterate through the child views of the TableRow
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            View childView = tableRow.getChildAt(i);
            // Check the type of the child view
            if (childView instanceof ImageView) {
                // If it's an ImageView, you can convert it to Bitmap and add it to the ArrayList
                ImageView imageView = (ImageView) childView;
                dataList.add(imageView);
            }

        }
    }

    public void onSaveButtonClicked(View view) {
        // Get the references to the EditText fields
        EditText promotionName = findViewById(R.id.promotion_name_textview);
        EditText promotion_desc_textview = findViewById(R.id.promotion_desc_textview);
        EditText promotion_quota_textview = findViewById(R.id.promotion_quota_textview);
        Spinner promotionConditionSpinner = findViewById(R.id.promotion_conditiontype_spinner);
        String condition = promotionConditionSpinner.getSelectedItem().toString();
        String sPromotionName = promotionName.getText().toString();
        String sPromotionDesc = promotion_desc_textview.getText().toString();
        Integer sPromotionQuota = Integer.valueOf(promotion_quota_textview.getText().toString());
        switch (condition) {
            case "Watch Image Ads":
                condition = "WATCH_IMAGE_ADS";
                break;
            case "Follow Social Media":
                condition = "SOCIAL_IMAGE_ADS";
                break;
            case "Post Social Media":
                condition = "POST_SOCIAL_MEDIA";
                break;
        }
        ArrayList<Integer> dataList = new ArrayList<>();
        ArrayList<String> dataStringList = new ArrayList<>();
        ArrayList<ImageView> dataImageList = new ArrayList<>();

        PromotionItemModel apiModel = new PromotionItemModel();
        apiModel.setPromotionName(sPromotionName);
        apiModel.setPromotionDescription(sPromotionDesc);
        apiModel.setQuota(sPromotionQuota);
        apiModel.setCondition(condition);

        if (condition.equalsIgnoreCase("WATCH_IMAGE_ADS")) {
            TableRow watchAdsImageRow = findViewById(R.id.promotion_condition_watchadsimage);
            readDataFromTableRow(watchAdsImageRow, dataList);

            TableRow watchAdsDurationRow = findViewById(R.id.promotion_condition_watchadsduration);
            readDataImageFromTableRow(watchAdsDurationRow, dataImageList);
            List<WatchImageAds> watchImageAds1 = new ArrayList<>();
            int loop = 1;
            for (Integer x : dataList) {
                loop += 1;
                WatchImageAds watchImageAds2 = new WatchImageAds();
                watchImageAds2.setSecond(x);
                watchImageAds2.setImageUrl(sPromotionName + loop);
                watchImageAds1.add(watchImageAds2);
            }
            apiModel.setWatchImageAds(watchImageAds1);
        }
        if (condition.equalsIgnoreCase("SOCIAL_IMAGE_ADS")) {
            TableRow socialrow = findViewById(R.id.promotion_condition_postsocialmedia);
            readDataStringFromTableRow(socialrow, dataStringList);
            List<SocialMediaAds> socialMediaAds = new ArrayList<>();
            for (String x : dataStringList) {
                SocialMediaAds socialMediaAds1 = new SocialMediaAds();
                socialMediaAds1.setType("INSTAGRAM");
                socialMediaAds1.setUrl(x);
                socialMediaAds.add(socialMediaAds1);
            }
            apiModel.setSocialMediaAds(socialMediaAds);
        }
        if (condition.equalsIgnoreCase("POST_SOCIAL_MEDIA")) {
            TableRow postSocialmedia = findViewById(R.id.promotion_condition_postsocialmedia);
            readDataStringFromTableRow(postSocialmedia, dataStringList);
            List<PostSocialMedia> postSocialMedia1 = new ArrayList<>();
            for (String x : dataStringList) {
                PostSocialMedia socialMediaAds1 = new PostSocialMedia();
                socialMediaAds1.setUsername(x);
                socialMediaAds1.setUrl("https://instagram.com");
                postSocialMedia1.add(socialMediaAds1);
            }
            apiModel.setPostSocialMedia(postSocialMedia1);
        }
        Spinner discountSpinner = findViewById(R.id.promotion_discounttype_spinner);
        String discType = discountSpinner.getSelectedItem().toString();
        Discount discount1 = new Discount();
        if (discType.equalsIgnoreCase("Percentage")){
            ArrayList<Integer> dataDiscountPercentage = new ArrayList<>();
            ArrayList<Integer> dataMax = new ArrayList<>();
            TableRow discount = findViewById(R.id.promotion_table_discount_maxpercentage);
            readDataFromTableRow(discount, dataDiscountPercentage);
            discount1.setDiscountGiven(dataDiscountPercentage.get(0));
            TableRow maxDiscount = findViewById(R.id.promotion_table_discount_percentage);
            readDataFromTableRow(maxDiscount, dataMax);
            discount1.setDiscountGiven(dataMax.get(0));
            apiModel.setDiscounts(discount1);
        } else {
            ArrayList<Integer> dataMax = new ArrayList<>();
            TableRow discount = findViewById(R.id.promotion_table_discount_fixed);
            readDataFromTableRow(discount, dataMax);
            discount1.setDiscountGiven(dataMax.get(0));
            discount1.setDiscountGiven(100);
            apiModel.setDiscounts(discount1);
        }
        sendImageToAPI(apiModel, promotionImageView, dataImageList);
    }

    private void initTableRow() {
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

    private void initDiscountSpinner() {
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

                switch (position) {
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

    private void initConditionSpinner() {
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

                switch (position) {
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
    private void sendToApi(PromotionItemModel dataModel) {
        ServiceGenerator service = new ServiceGenerator();
        Call<SuccessResponse> call = service.getApiService(this).postPromotion(dataModel);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddPromotionActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddPromotionActivity.this, ManagePromotionActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        String responseMessage = errorResponse.getResponseMessage();
                        Toast.makeText(AddPromotionActivity.this, "Failed to save data: " + responseMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddPromotionActivity.this, "Failed to parse error response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPromotionActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                // Handle API call failure
                Toast.makeText(AddPromotionActivity.this, "Failed to save data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Uri getImageUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return getImageUriFromBitmap(bitmap);
        }
        return null;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream outputStream = new FileOutputStream(cachePath + "/image.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return Uri.fromFile(new File(cachePath + "/image.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendImageToAPI(PromotionItemModel apiModel, ImageView imageView, ArrayList<ImageView> imageViewArrayList) {
        sendToApi(apiModel);
        Uri imageUri = getImageUri(imageView);
        int loop = 0;
        if (!imageViewArrayList.isEmpty()) {
            for (ImageView imageview : imageViewArrayList) {
                loop += 1;
                Uri imageAllUri = getImageUri(imageview);
                // Convert the image URI to a File object
                File imageFile = new File(imageAllUri.getPath());

                // Create the request body for the image file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

                // Create the other request parameters
                RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "promo");
                RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), "promo" + apiModel.getPromotionName() + loop);
                RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

                // Make the upload request
                ServiceGenerator service = new ServiceGenerator();
                Call<ResponseBody> call = service.getApiService(this).uploadFile(filePart, typePart, namePart, authorizationPart);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Image uploaded successfully
                            Toast.makeText(AddPromotionActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle error response
                            Toast.makeText(AddPromotionActivity.this, "Failed to upload image, Reason: {}", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle network or other errors
                        Toast.makeText(AddPromotionActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (imageUri != null) {
            // Convert the image URI to a File object
            File imageFile = new File(imageUri.getPath());

            // Create the request body for the image file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

            // Create the other request parameters
            RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "promo");
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), "promo" + apiModel.getPromotionName());
            RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

            // Make the upload request
            ServiceGenerator service = new ServiceGenerator();
            Call<ResponseBody> call = service.getApiService(this).uploadFile(filePart, typePart, namePart, authorizationPart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Image uploaded successfully
                        Toast.makeText(AddPromotionActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        Toast.makeText(AddPromotionActivity.this, "Failed to upload image, Reason: {}", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle network or other errors
                    Toast.makeText(AddPromotionActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No image selected, continue with saving the data to the API
            sendToApi(apiModel);
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Image selected from gallery
            Uri imageUri = data.getData();
            // Process the selected image Uri as per your requirement

            // For example, you can display the selected image in the ImageView
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(promotionImageView);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        } else if (requestCode == CAMERA_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Image captured from camera
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Process the captured image Bitmap as per your requirement

            // For example, you can display the captured image in the ImageView
            promotionImageView.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(getApplicationContext()).clearDiskCache();
                }
            }).start();
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(AddPromotionActivity.this, ManagePromotionActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }
}