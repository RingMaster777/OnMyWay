package com.example.tourguide.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tourguide.R;


public class splash_screen extends AppCompatActivity {

    private TextView logoName;
    ImageView logo;
    LottieAnimationView lottie;

    Animation logoNameAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);



        logo  = findViewById(R.id.logo);
        logoName  = findViewById(R.id.logo_name);
        lottie   = findViewById(R.id.animationView);


        logoNameAnim = AnimationUtils.loadAnimation(this,R.anim.logo_and_name_anim);
        logoName.setAnimation(logoNameAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =  new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }
}
