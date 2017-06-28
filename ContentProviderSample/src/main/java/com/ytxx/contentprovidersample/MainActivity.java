package com.ytxx.contentprovidersample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.insert)
    Button insert;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.query)
    Button query;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("contentProvideSample");
        contentResolver = getContentResolver();
    }

    @OnClick({R.id.insert, R.id.delete, R.id.update, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.insert:
                ContentValues contentValues = new ContentValues();
                contentValues.put(Profile.COLUMN_NAME, "testSample" + new Random().nextInt(100));
                Uri uri = contentResolver.insert(Profile.CONNECT_URI, contentValues);
                assert uri != null;
                Log.d(TAG, "insert: " + uri.toString());
                break;
            case R.id.delete:
                //测试删除第一个数据
                Cursor c = contentResolver.query(Profile.CONNECT_URI, null, null, null, null);
                assert c != null;
                if (c.getCount() <= 0) {
                    Log.d(TAG, "delete: data size is 0. ");
                    return;
                }
                c.moveToNext();
                int a = c.getInt(c.getColumnIndex(Profile.COLUMN_ID));
                String b = c.getString(c.getColumnIndex(Profile.COLUMN_NAME));
                c.close();
                Log.d(TAG, "delete: " + a + " name->" + b);
                int xi = contentResolver.delete(Uri.withAppendedPath(Profile.CONNECT_URI, "/" + a), null, null);
                Log.d(TAG, "delete success : " + xi);
                break;
            case R.id.update:
                //测试更改第二个数据
                Cursor update = contentResolver.query(Profile.CONNECT_URI, null, null, null, null);
                assert update != null;
                int id2;
                String name2;
                if (update.getCount() <= 0) {
                    Log.d(TAG, "delete: data size is 0. ");
                    return;
                }
                if (update.getCount() > 1) {
                    update.moveToPosition(1);
                    id2 = update.getInt(update.getColumnIndex(Profile.COLUMN_ID));
                    name2 = update.getString(update.getColumnIndex(Profile.COLUMN_NAME));
                } else {
                    update.moveToFirst();
                    id2 = update.getInt(update.getColumnIndex(Profile.COLUMN_ID));
                    name2 = update.getString(update.getColumnIndex(Profile.COLUMN_NAME));
                }
                update.close();
                Log.d(TAG, "update: " + id2 + " name->" + name2);
                ContentValues cv1 = new ContentValues();
                cv1.put(Profile.COLUMN_NAME, "updateSample" + new Random().nextInt(100));
                int sss = contentResolver.update(Uri.withAppendedPath(Profile.CONNECT_URI, "/" + id2), cv1, null, null);
                Log.d(TAG, "update success : " + sss);
                break;
            case R.id.query:
                Cursor cursor = contentResolver.query(Profile.CONNECT_URI, null, null, null, null);
                assert cursor != null;
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(Profile.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(Profile.COLUMN_NAME));
                    Log.d(TAG, "query: " + id + " name->" + name);
                }
                cursor.close();
                break;
        }
    }
}
