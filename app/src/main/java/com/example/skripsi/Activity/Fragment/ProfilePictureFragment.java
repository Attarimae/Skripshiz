package com.example.skripsi.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.ServiceGenerator;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.API.SharedPreferencesCashier;
import com.example.skripsi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePictureFragment extends Fragment {

    ImageView imageProfilePic;
    String imagesB64;
    SessionManager sm;
    SharedPreferencesCashier spc;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_picture, container, false);
        sm = new SessionManager(requireContext());
        spc = new SharedPreferencesCashier(requireContext());
        imageProfilePic = view.findViewById(R.id.FR_profilepictureImages);

//        if(spc.fetchCashierPic() == null){
//            imageProfilePic.setImageResource(R.drawable.default_profile); //Default Profile Picture Images
//        } else {
//            setupTheImage(spc.fetchCashierPic()); //Load Profile Picture Images if already saved before
//        }
//            Using Glide to Display the Profile Picture
        Glide.with(this)
                .load(APIConstant.BASE_URL_DOWNLOAD + sm.fetchRestaurantID() + "_" + sm.fetchStaffID())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .error(R.drawable.default_profile)
                .into(imageProfilePic);

        imageProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImages();
            }
        });

        //behold the greates ID Naming, god, i want to kms so much
        TextView profilepicNames = view.findViewById(R.id.FR_profilepicturetxtViewUserName);
        profilepicNames.setText(sm.fetchStaffName());
        TextView profilepicRoles = view.findViewById(R.id.FR_profilepicturetxtViewUserRole);
        profilepicRoles.setText(sm.fetchStaffRole());

        Button imagesBtnSubmit = view.findViewById(R.id.FR_profilepictureSubmitButton);
        imagesBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saving the bitmap into SPC, maybe, that's what i think
//                spc.saveCashierPic(imagesB64);
//                Toast.makeText(requireContext(), "Berhasil menyimpan Profile Picture", Toast.LENGTH_SHORT).show();
//                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, new MenuFragment());
//                ft.commit();

                //Saving to API Photo
                Uri profilepictureUri = getProfilePictureUri(imageProfilePic);
                if(profilepictureUri != null){
                    File profilepictureFile = new File(profilepictureUri.getPath());

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), profilepictureFile);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", profilepictureFile.getName(), requestFile);

                    RequestBody typePart = RequestBody.create(MediaType.parse("text/plain"), "employee");
                    RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchStaffID());
                    RequestBody authorizationPart = RequestBody.create(MediaType.parse("text/plain"), sm.fetchAuthToken());

                    ServiceGenerator service = new ServiceGenerator();
                    Call<ResponseBody> call = service.getApiService(requireContext()).uploadFile(filePart, typePart, namePart, authorizationPart);
                    call.enqueue(new Callback<ResponseBody>() {
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                            if(response.isSuccessful()){
                                Toast.makeText(requireActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireActivity(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        public void onFailure(Call<ResponseBody> call, Throwable t){
                            Toast.makeText(requireActivity(), "Failed to upload Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Images uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void selectImages(){
//        Intent chooseImg = new Intent();
//        chooseImg.setType("image/*");
//        chooseImg.setAction(Intent.ACTION_PICK);
//
//        launchActivityResult.launch(chooseImg);
        //Select from Camera/Gallery
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Image Source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Gallery option clicked
                    // Clear Glide cache before selecting a new image
                    Glide.get(requireActivity()).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(requireActivity()).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else if (which == 1) {
                    // Camera option clicked
                    // Clear Glide cache before capturing a new image
                    Glide.get(requireActivity()).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(requireActivity()).clearDiskCache();
                        }
                    }).start();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
                }
            }
        });
        builder.show();
    }

//    ActivityResultLauncher<Intent> launchActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//        result -> {
//            if(result.getResultCode() == RESULT_OK){
//                Intent data = result.getData();
//                if(data != null && data.getData() != null){
//                    Uri selectedImgUri = data.getData();
//                    Bitmap selectedImgBitmap = null;
//                    try {
//                        //Get the Images and then Encode it into Base64
//                        selectedImgBitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().getContentResolver(), selectedImgUri);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        selectedImgBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                        byte[] bytes = stream.toByteArray();
//                        imagesB64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    setupTheImage();
//                }
//            }
//    });

//    private void setupTheImage(){ // Decode Base64 Images
//        byte[] bytes = Base64.decode(imagesB64, Base64.DEFAULT);
//        Bitmap setupBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imageProfilePic.setImageBitmap(setupBitmap);
//    }
//
//    private void setupTheImage(String storedBase64){
//        byte[] bytes = Base64.decode(storedBase64, Base64.DEFAULT);
//        Bitmap setupBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imageProfilePic.setImageBitmap(setupBitmap);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    .into(imageProfilePic);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(requireContext()).clearDiskCache();
                }
            }).start();
        } else if (requestCode == CAMERA_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Image captured from camera
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Process the captured image Bitmap as per your requirement

            // For example, you can display the captured image in the ImageView
            imageProfilePic.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(requireContext()).clearDiskCache();
                }
            }).start();
        }
    }

    private Uri getProfilePictureUri(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return getProfilePictureUriFromBitmap(bitmap);
        }
        return null;
    }

    private Uri getProfilePictureUriFromBitmap(Bitmap bitmap){
        try {
            File cachePath = new File(requireActivity().getCacheDir(), "images");
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
}