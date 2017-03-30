package com.jax.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jax.realmdemo.bean.NotificationType;
import com.jax.realmdemo.model.Notification;
import com.jax.realmdemo.model.NotificationHelper;
import com.jax.realmdemo.model.TestModel;

import java.util.Date;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private NotificationHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = NotificationHelper.getInstance();
        helper.getRealm();
        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_log).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                add();
                break;
            case R.id.button_log:
                helper.logAll();
                break;
        }
    }

    private void add() {
        TestModel testModel = new TestModel();
        testModel.setId("dynamic_0002");
        testModel.setType("type3");
        testModel.setName("小花3");
        testModel.setHeader("http://ddsdfd.jpg");

        helper.addOrUpdateNotification(testModel,
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "addOrUpdateNotification onSuccess: ");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d(TAG, "addOrUpdateNotification onError: ");
                    }
                });
    }
}
