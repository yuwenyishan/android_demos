package com.jax.reactivex;

import android.app.Application;

/**
 * Created on 2017/2/9.
 */

public class AppApplication extends Application {

    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppApplication getInstance() {
        return instance;
    }
}
