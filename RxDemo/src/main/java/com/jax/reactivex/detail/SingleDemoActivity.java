package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.jax.basedepend.BaseActivity;
import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.SingleDemo;

/**
 * Created on 2017/2/9.
 */

public class SingleDemoActivity extends RootActivity {

    SingleDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_demo);
        demo = new SingleDemo();
    }

    public void onSuccess(View view) {
        demo.testSingle(true);
    }

    public void onError(View view) {
        demo.testSingle(false);
    }
}
