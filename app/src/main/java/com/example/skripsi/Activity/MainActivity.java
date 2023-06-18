package com.example.skripsi.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skripsi.Activity.Fragment.MenuFragment;
import com.example.skripsi.R;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN =3000;

    Animation topAnim,topAnimLong,bottoAnim;

    ImageView imageView;
    TextView textPointofsales,textDot,textSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottoAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        topAnimLong= AnimationUtils.loadAnimation(this,R.anim.bottom_animatio_long);

        imageView = findViewById(R.id.imageView);
        textDot = findViewById(R.id.textDot);
        textPointofsales = findViewById(R.id.textPointofsales);
        textSales = findViewById(R.id.textSales);

        imageView.setAnimation(topAnim);
        textPointofsales.setAnimation(topAnimLong);
        textSales.setAnimation(bottoAnim);
        textDot.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ManageEmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }
}
