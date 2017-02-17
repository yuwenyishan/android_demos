package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.FilterDemo;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

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
        debounce(findViewById(R.id.debounce));
        distinct(findViewById(R.id.distinct));
        elementAt(findViewById(R.id.elementAt));
        filter(findViewById(R.id.filter));
        first(findViewById(R.id.first));
        ignoreElements(findViewById(R.id.ignoreElements));
    }

    public void debounce(View view) {
        doubleClickDetect(view, () -> demo.debounce());
    }

    public void distinct(View view) {
        debounceOperation(view, () -> demo.distinct());
    }

    public void elementAt(View view) {
        debounceOperation(view, () -> demo.elementAt());
    }

    public void filter(View view) {
        debounceOperation(view, () -> demo.filter());
    }

    public void first(View view) {
        debounceOperation(view, () -> demo.first());
    }

    public void ignoreElements(View view) {
        debounceOperation(view, () -> demo.ignoreElements());
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

    public void debounceOperation(View view, DoubleClick click) {
        RxView.clicks(view).share().throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (click != null) {////两秒钟之内只取一个点击事件，防抖操作
                        click.result();
                    }
                }, throwable -> Log.e(TAG, "doubleClickDetect: onError-->", throwable));
    }

    interface DoubleClick {
        void result();
    }
}
