package com.jax.lottiedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import com.airbnb.lottie.LottieAnimationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static String updateDir1, updateDir2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateDir1 = SdCardUtil.getAvailableFileDir(this) + "/bay/update/";
        updateDir2 = getFilesDir() + "/bay/update/";
        File file1 = new File(updateDir1);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file2 = new File(updateDir2);
        if (!file2.exists()) {
            file2.mkdirs();
        }
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
        print();

    }

    private void print() {
        try {
            String availableInternalMemorySize = String.valueOf(SdCardUtil.getFree(updateDir2) / (1024 * 1024));
            String availableExternalSdcardSize = String.valueOf(SdCardUtil.getFree(updateDir1) / (1024 * 1024));
            String totalInternalMemorySize = String.valueOf(SdCardUtil.getSdCardSize(updateDir2) / (1024 * 1024));
            String totalExternalSdcardSize = String.valueOf(SdCardUtil.getSdCardSize(updateDir1) / (1024 * 1024));
            StringBuffer storageInfoSb = new StringBuffer();
            storageInfoSb.append("availableDataInternalMemorySize:").append(availableInternalMemorySize)
                    .append("MB,totalInternalMemorySize:").append(totalInternalMemorySize)
                    .append("MB,availableExternalSdcardSize:").append(availableExternalSdcardSize)
                    .append("MB,totalExternalSdcardSize:").append(totalExternalSdcardSize).append("MB");
            Log.i(TAG, "print: " + storageInfoSb.toString());
        } catch (Exception e) {
            Log.w(TAG, "print: ", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown() called with: keyCode = [" + keyCode + "], event = [" + event + "]");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyLongPress() called with: keyCode = [" + keyCode + "], event = [" + event + "]");
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp() called with: keyCode = [" + keyCode + "], event = [" + event + "]");
        return super.onKeyUp(keyCode, event);
    }
}
