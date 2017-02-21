package com.jax.reactivex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jax.basedepend.BaseActivity;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created on 2017/2/10.
 */

public class RootActivity extends BaseActivity {
    private static final String TAG = "RootActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public void debounceOperation(View view, DoubleClick callback) {
        RxView.clicks(view).share()
                .throttleFirst(2, TimeUnit.SECONDS)//两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (callback != null) {
                        callback.result();
                    }
                }, throwable -> Log.e(TAG, "doubleClickDetect: onError-->", throwable));
    }
}
