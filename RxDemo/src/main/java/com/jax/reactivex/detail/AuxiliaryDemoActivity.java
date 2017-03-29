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
        debounceOperation(findViewById(R.id.delaySubscription), () -> demo.delaySubscription());
        debounceOperation(findViewById(R.id.doOnEach), () -> demo.doOnEach());
        debounceOperation(findViewById(R.id.doOnNextErrorComplete), () -> demo.doOnNextErrorComplete());
        debounceOperation(findViewById(R.id.doOnSubscribeOrNot), () -> demo.doOnSubscribeOrNot());
        debounceOperation(findViewById(R.id.doOnTerminateFinally), () -> demo.doOnTerminateFinally());
        debounceOperation(findViewById(R.id.materialize), () -> demo.materialize());
        debounceOperation(findViewById(R.id.dematerialize), () -> demo.dematerialize());
        debounceOperation(findViewById(R.id.serialize), () -> demo.serialize());
        debounceOperation(findViewById(R.id.timeInterval), () -> demo.timeInterval());
        debounceOperation(findViewById(R.id.timeout), () -> demo.timeout());
        debounceOperation(findViewById(R.id.timestamp), () -> demo.timestamp());
        debounceOperation(findViewById(R.id.using), () -> demo.using());
        debounceOperation(findViewById(R.id.to), () -> demo.to());
    }

    @Override
    protected void onDestroy() {
        if (demo != null) {
            demo.onDestroy();
        }
        super.onDestroy();
    }
}
