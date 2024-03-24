package com.example.skripsi.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.Activity.ManagerMainActivity;
import com.example.skripsi.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private ImageView qrCodeImageView;
    private EditText editText;
    private Button generateButton;
    private Button saveButton;
    SessionManager sm;
    private String table;
    private TextView manageMenuTextView;
    private static final int REQUEST_CODE_SAVE_FILE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        sm = new SessionManager(this);
        generateButton = findViewById(R.id.generate_button);
        editText = findViewById(R.id.table_qr);
        qrCodeImageView = findViewById(R.id.qr_code_image_view);
        saveButton = findViewById(R.id.save_qr_button);
        manageMenuTextView = findViewById(R.id.manage_menu);
        manageMenuTextView.setText("QR-ACTIVITY");
        editText.setText("1");
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCode();
            }
        });
    }

    private void generateQRCode() {
        table = editText.getText().toString().trim();
        String fullqr = APIConstant.FRONT_END_URL + "/" + sm.fetchAuthToken() + "/" + table;
        if (!table.isEmpty()) {
            if (Integer.parseInt(table) < 1) {
                Toast.makeText(this, "Nomor meja harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap qrCodeBitmap = generateQRCodeBitmap(fullqr, 500, 500);
                qrCodeImageView.setImageBitmap(qrCodeBitmap);
            }
        } else {
            Toast.makeText(this, "Please enter table number", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap generateQRCodeBitmap(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveQRCode() {
        if (ContextCompat.checkSelfPermission(QRActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QRActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            launchFilePicker();
        }
    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TITLE, "QRCode" + table + ".png");
        startActivityForResult(intent, REQUEST_CODE_SAVE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                saveImageToUri(uri);
            }
        }
    }

    private void saveImageToUri(Uri uri) {
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
            if (pfd != null) {
                FileOutputStream outputStream = new FileOutputStream(pfd.getFileDescriptor());
                Bitmap qrCodeBitmap = ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close(); // Close the FileOutputStream
                pfd.close(); // Close the ParcelFileDescriptor
                Toast.makeText(this, "QR Code saved", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(QRActivity.this, ManagerMainActivity.class);
        startActivity(intent);
        finish(); // Optional: If you want to finish the current activity after navigating to ManageMenuActivity
    }
}
