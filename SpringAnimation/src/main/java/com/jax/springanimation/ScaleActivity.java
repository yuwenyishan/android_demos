package com.jax.springanimation;

import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created on 2017/3/27.
 */

public class ScaleActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final float INIT_SCALE = 1f;

    private SpringAnimation scaleX;
    private SpringAnimation scaleY;
    private ScaleGestureDetector scaleGestureDetector;

    private TextView tv_show;
    private ImageView iv_img;

    private Util util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        tv_show = (TextView) findViewById(R.id.scaleTextView);
        iv_img = (ImageView) findViewById(R.id.scalingView);
        updateScaleText();

        util = new Util();
        scaleX = util.createSpringAnimation(iv_img, SpringAnimation.SCALE_X, INIT_SCALE,
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        scaleY = util.createSpringAnimation(iv_img, SpringAnimation.SCALE_Y, INIT_SCALE,
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        scaleX.addUpdateListener((animation, value, velocity) -> updateScaleText());

        setupPinchToZoom();

        iv_img.setOnTouchListener(this);
    }

    float scaleFactory = 1f;

    private void setupPinchToZoom() {
        scaleGestureDetector = new ScaleGestureDetector(this,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        scaleFactory *= detector.getScaleFactor();
                        iv_img.setScaleX(iv_img.getScaleX() * scaleFactory);
                        iv_img.setScaleY(iv_img.getScaleY() * scaleFactory);
                        updateScaleText();
                        return true;
                    }
                });
    }

    private void updateScaleText() {
        tv_show.setText(String.format(Locale.CHINA, "%.3f", iv_img.getScaleX()));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            scaleX.start();
            scaleY.start();
        } else {
            scaleX.cancel();
            scaleY.cancel();
            scaleGestureDetector.onTouchEvent(event);
        }
        return true;
    }
}
