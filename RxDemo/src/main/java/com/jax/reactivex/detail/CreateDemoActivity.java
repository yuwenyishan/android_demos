package com.jax.reactivex.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.CreateDemo;
import com.jax.reactivex.util.ToastUtil;

/**
 * Created on 2017/2/10.
 */

public class CreateDemoActivity extends RootActivity {

    private CreateDemo createDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_demo);
        createDemo = new CreateDemo();
    }

    @Override
    protected void onDestroy() {
        if (createDemo != null) {
            createDemo.onDestroy();
        }
        super.onDestroy();
    }

    private Handler handler = new Handler(message -> {
        try {
            String str = (String) message.obj;
            ToastUtil.showToast(str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    });

    public void create(View view) {
        createDemo.create();
    }

    public void defer(View view) {
        createDemo.defer();
    }

    public void empty(View view) {
        createDemo.empty();
    }

    public void never(View view) {
        createDemo.never();
    }

    public void throwE(View view) {
        createDemo.throwE();
    }

    public void from(View view) {
        createDemo.from();
    }

    public void interval(View view) {
        createDemo.interval();
    }

    public void just(View view) {
        createDemo.just();
    }

    public void range(View view) {
        createDemo.range();
    }

    public void repeat(View view) {
        createDemo.repeat();
    }

    public void start(View view) {
        createDemo.start();
    }

    public void timer(View view) {
        createDemo.timer();
    }
}
