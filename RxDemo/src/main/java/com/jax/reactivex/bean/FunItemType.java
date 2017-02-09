package com.jax.reactivex.bean;

import android.support.annotation.IntDef;


/**
 * Created on 2017/2/8.
 */

public class FunItemType {
    public static final int TYPE_INTRODUCE = 0x10;
    public static final int TYPE_DEMO = 0x11;

    @IntDef({TYPE_DEMO, TYPE_INTRODUCE})
    public @interface TYPE {
    }
}
