package com.example.skripsi.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.API.APIConstant;
import com.example.skripsi.API.SessionManager;
import com.example.skripsi.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class QRActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private EditText editText;
    private Button generateButton;
    private Button saveButton;
    SessionManager sm;
    private String table;
    private TextView manageMenuTextView;

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
        String fullqr = APIConstant.FRONT_END_URL+"/"+sm.fetchAuthToken()+"/"+table;
        if (!TextUtils.isEmpty(table)) {
            if(Integer.parseInt(table)<1){
                Toast.makeText(this, "Nomor meja harus lebih besar dari 0", Toast.LENGTH_SHORT).show();
            }else{
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
        Bitmap qrCodeBitmap = ((BitmapDrawable) qrCodeImageView.getDrawable()).getBitmap();
        String fileName = "QRCode"+table+".png";
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "QR Code saved", Toast.LENGTH_SHORT).show();
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
