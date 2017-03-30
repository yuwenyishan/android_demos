package com.jax.realmdemo.bean;

import android.support.annotation.StringDef;

/**
 * Created on 2017/3/30.
 */

public class NotificationType {

    public static final String TYPE_D_L = "DynamicLike";
    public static final String TYPE_D_C = "DynamicComment";
    public static final String TYPE_D_C_C = "DynamicCommentComment";
    public static final String TYPE_A_L = "AudioLike";
    public static final String TYPE_A_C = "AudioComment";
    public static final String TYPE_A_C_C = "AudioCommentComment";
    public static final String TYPE_A_R = "AudioReward";

    @StringDef({TYPE_D_L, TYPE_D_C, TYPE_D_C_C, TYPE_A_L, TYPE_A_C, TYPE_A_C_C, TYPE_A_R})
    public @interface Type {
    }
}
