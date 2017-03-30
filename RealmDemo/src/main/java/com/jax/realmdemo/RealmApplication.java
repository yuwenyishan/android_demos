package com.jax.realmdemo;

import android.app.Application;

import com.jax.realmdemo.util.RealmHelper;

/**
 * Created on 2017/3/30.
 */

public class RealmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmHelper.initRealm(this);
    }
}
