package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.AuxiliaryOperationDemo;

/**
 * Created on 2017/3/21.
 */

public class AuxiliaryDemoActivity extends RootActivity {

    private AuxiliaryOperationDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auxiliary_operation_demo);
        demo = new AuxiliaryOperationDemo();
        debounceOperation(findViewById(R.id.delay), () -> demo.delay());
    }

    @Override
    protected void onDestroy() {
        if (demo != null) {
            demo.onDestroy();
        }
        super.onDestroy();
    }
}
