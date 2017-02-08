package com.jax.demos.constraintlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.view.View;

import com.jax.basedepend.BaseActivity;
import com.jax.demos.R;

/**
 * Created on 2017/2/8.
 */

public class DoubleLayoutTransition extends BaseActivity {

    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet1 = new ConstraintSet();
    private ConstraintSet constraintSet2 = new ConstraintSet();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_layout_transition_1);
        constraintLayout = (ConstraintLayout) findViewById(R.id.main);
        constraintSet1.clone(constraintLayout);
        constraintSet2.clone(this, R.layout.activity_double_layout_transition_2);
    }

    public void onApplyClick(View view) {
        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSet2.applyTo(constraintLayout);
    }

    public void onResetClick(View view) {
        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSet1.applyTo(constraintLayout);
    }
}
