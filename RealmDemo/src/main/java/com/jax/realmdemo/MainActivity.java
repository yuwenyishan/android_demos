package com.jax.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jax.realmdemo.model.NotificationHelper;
import com.jax.realmdemo.model.TestModel;

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
        findViewById(R.id.button_delete).setOnClickListener(this);
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
            case R.id.button_delete:
                delete();
                break;
        }
    }

    private void add() {
        TestModel testModel = new TestModel();
        testModel.setId("dynamic_0006");
        testModel.setType("type5");
        testModel.setAge(12);
        testModel.setName("版本2");
        testModel.setHeader("http://版本2数聚.jpg");

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

    private void delete() {
        TestModel testModel = new TestModel();
        testModel.setId("dynamic_0006");
        testModel.setType("type5");
        testModel.setName("版本2");
        testModel.setAge(12);
        testModel.setHeader("http://版本2数聚.jpg");
        helper.deleteNotification(testModel, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "delete onSuccess: ");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "delete onError: ");
            }
        });
    }
}
