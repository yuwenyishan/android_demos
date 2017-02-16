package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.FilterDemo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created on 2017/2/15.
 */

public class FilterDemoActivity extends RootActivity {
    private static final String TAG = "FilterDemoActivity";

    private FilterDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_demo);
        demo = new FilterDemo();
    }

    public void debounce(View view) {
//        demo.debounce();
        doubleClickDetect(view, () -> demo.debounce());
    }

    public void doubleClickDetect(View view, DoubleClick click) {//双击检测
        Observable<Void> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(1, TimeUnit.SECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voids -> {
                    if (voids.size() >= 2) {
                        Log.d(TAG, "call: Double Click ." + voids.size());
                        if (click != null) {
                            click.result();
                        }
                    } else {
                        Log.d(TAG, "doubleClickDetect: single .");
                        if (click != null) {
                            click.result();
                        }
                    }
                }, throwable -> Log.e(TAG, "doubleClickDetect: onError-->", throwable));
    }

    interface DoubleClick {
        void result();
    }
}
