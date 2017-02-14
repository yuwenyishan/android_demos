package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.TransformDemo;

/**
 * Created on 2017/2/13.
 */

public class TransformDemoActivity extends RootActivity {

    private TransformDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform_demo);
        demo = new TransformDemo();
    }

    public void buffer(View view) {
        demo.buffer();
    }

    public void flatMap(View view) {
        demo.flatMap();
    }
}
