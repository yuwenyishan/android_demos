package com.jax.springanimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.positionAnimator).setOnClickListener(v -> startActivity(new Intent(this, PositionActivity.class)));
        findViewById(R.id.scaleAnimator).setOnClickListener(v -> startActivity(new Intent(this, ScaleActivity.class)));
        findViewById(R.id.rotationAnimator).setOnClickListener(v -> startActivity(new Intent(this, RotationActivity.class)));
    }
}
