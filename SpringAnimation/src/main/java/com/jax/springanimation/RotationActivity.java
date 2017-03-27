package com.jax.springanimation;

import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created on 2017/3/27.
 */

public class RotationActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final float INIT_ROTATION = 0f;

    private ImageView iv_img;
    private TextView tv_show;

    private SpringAnimation rotationAnim;
    private Util util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        iv_img = (ImageView) findViewById(R.id.rotatingView);
        tv_show = (TextView) findViewById(R.id.rotationTextView);
        updateRotationText();

        util = new Util();
        rotationAnim = util.createSpringAnimation(iv_img, SpringAnimation.ROTATION, INIT_ROTATION,
                SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        rotationAnim.addUpdateListener((animation, value, velocity) -> updateRotationText());
        iv_img.setOnTouchListener(this);

    }

    private void updateRotationText() {
        tv_show.setText(String.format(Locale.CHINA, "%.3f", iv_img.getRotation()));
    }

    private float previousRotation = 0f;
    private float currentRotation = 0f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float centerX = v.getWidth() / 2f;
        float centerY = v.getHeight() / 2f;
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                rotationAnim.cancel();
                updateCurrentRotation(v, x, y, centerX, centerY);
                break;
            case MotionEvent.ACTION_MOVE:
                previousRotation = currentRotation;
                updateCurrentRotation(v, x, y, centerX, centerY);
                float angle = currentRotation - previousRotation;
                iv_img.setRotation(iv_img.getRotation() + angle);
                updateRotationText();
                break;
            case MotionEvent.ACTION_UP:
                rotationAnim.start();
                break;
        }
        return true;
    }

    private void updateCurrentRotation(View view, float x, float y, float centerX, float centerY) {
        currentRotation = (float) (view.getRotation() +
                Math.toDegrees(Math.atan2(x - centerX, centerY - y)));
    }
}
