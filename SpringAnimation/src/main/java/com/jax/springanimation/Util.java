package com.jax.springanimation;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

/**
 * Created on 2017/3/27.
 */

public class Util {

    public SpringAnimation createSpringAnimation(View view, DynamicAnimation.ViewProperty property,
                                                 float finalPosition, float stiffness, float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce force = new SpringForce(finalPosition);
        force.setStiffness(stiffness);
        force.setDampingRatio(dampingRatio);
        animation.setSpring(force);
        return animation;
    }
}
