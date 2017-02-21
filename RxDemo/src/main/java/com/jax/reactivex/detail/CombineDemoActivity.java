package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.CombineDemo;


/**
 * Created on 2017/2/21.
 */

public class CombineDemoActivity extends RootActivity {

    private CombineDemo combineDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_demo);
        combineDemo = new CombineDemo();
        debounceOperation(findViewById(R.id.combinelatest), () -> combineDemo.combineLatest());
    }

    @Override
    protected void onDestroy() {
        if (combineDemo != null) {
            combineDemo.onDestroy();
        }
        super.onDestroy();
    }
}
