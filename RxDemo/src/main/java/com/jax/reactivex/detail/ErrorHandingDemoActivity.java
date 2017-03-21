package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.ErrorHandingDemo;

/**
 * Created on 2017/3/15.
 */

public class ErrorHandingDemoActivity extends RootActivity {

    private ErrorHandingDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo = new ErrorHandingDemo();
        setContentView(R.layout.activity_error_handing_demo);
        debounceOperation(findViewById(R.id.onErrorReturn), () -> demo.onErrorReturn());
        debounceOperation(findViewById(R.id.onErrorResumeNext), () -> demo.onErrorResumeNext());
        debounceOperation(findViewById(R.id.onExceptionResumeNext), () -> demo.onExceptionResumeNext());
        debounceOperation(findViewById(R.id.retry), () -> demo.retry());
        debounceOperation(findViewById(R.id.retryWhen), () -> demo.retryWhen());
    }
}
