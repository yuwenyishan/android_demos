package com.jax.lottiedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LottieAnimationView animationView = findViewById(R.id.animation_view3);
//        animationView.useHardwareAcceleration(true);
//        animationView.setAnimation("zip_anim.zip");
        animationView.setAnimation("loading.zip");

        LottieAnimationView animationView1 = findViewById(R.id.animation_view);
        animationView1.useHardwareAcceleration(true);
        animationView1.setAnimation(R.raw.on);
//        animationView1.setAnimation("loading.zip");

        LottieAnimationView animationView2 = findViewById(R.id.animation_view2);
        animationView2.useHardwareAcceleration(true);
        animationView2.setAnimation(R.raw.off);
//        animationView2.setAnimation("loading.zip");
    }
}
