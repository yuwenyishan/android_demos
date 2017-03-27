package com.jax.springanimation;

import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created on 2017/3/27.
 */

public class PositionActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView iv_img;
    private TextView tv_show;

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private Util util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        iv_img = (ImageView) findViewById(R.id.movingView);
        tv_show = (TextView) findViewById(R.id.positionTextView);
        util = new Util();
        iv_img.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            xAnimation = util.createSpringAnimation(iv_img, SpringAnimation.X, iv_img.getX(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
            yAnimation = util.createSpringAnimation(iv_img, SpringAnimation.Y, iv_img.getY(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        });
        iv_img.setOnTouchListener(this);
    }

    private float dx = 0f;
    private float dy = 0f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dx = iv_img.getX() - event.getRawX();
                dy = iv_img.getY() - event.getRawY();

                xAnimation.cancel();
                yAnimation.cancel();
                break;
            case MotionEvent.ACTION_MOVE:
                iv_img.setX(event.getRawX() + dx);
                iv_img.setY(event.getRawY() + dy);
                break;
            case MotionEvent.ACTION_UP:
                xAnimation.start();
                yAnimation.start();
                break;
        }
        return true;
    }
}
