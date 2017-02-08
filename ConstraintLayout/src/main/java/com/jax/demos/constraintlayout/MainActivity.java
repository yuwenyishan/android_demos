package com.jax.demos.constraintlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jax.basedepend.BaseActivity;
import com.jax.demos.R;

/**
 * Created on 2017/2/7.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        findViewById(R.id.whatButton).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ConstraintAnimator.class)));
        findViewById(R.id.button1).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DoubleLayoutTransition.class)));
    }
}
