package com.jax.shortcutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created on 2017/3/29.
 */

public class Shortcut4Activity extends AppCompatActivity {
    public static final String ACTION = "shortcut4Action";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        TextView tv_tips = (TextView) findViewById(R.id.shortcut_tips);
        tv_tips.setText(getClass().getName());
    }
}
