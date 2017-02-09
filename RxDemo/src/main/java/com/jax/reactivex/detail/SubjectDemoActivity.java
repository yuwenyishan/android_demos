package com.jax.reactivex.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jax.basedepend.BaseActivity;
import com.jax.reactivex.R;
import com.jax.reactivex.fun.SubjectDemo;

/**
 * Created on 2017/2/9.
 */

public class SubjectDemoActivity extends BaseActivity {

    private SubjectDemo demo;
    TextView tv_logcat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_demo);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_logcat = (TextView) findViewById(R.id.tv_logcat);
        demo = new SubjectDemo();
        demo.setTextView(tv_logcat);
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

    public void asyncSubjectDemo(View view) {
        demo.asyncSubject();
    }

    public void behaviorSubjectDemo(View view) {
        demo.behaviorSubject();
    }

    public void publishSubjectDemo(View view) {
        demo.publishSubject();
    }

    public void replaySubjectDemo(View view) {
        demo.replaySubject();
    }
}
