package com.jax.reactivex.util;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.jax.reactivex.AppApplication;

/**
 * Created on 2016/7/6.
 */
public class ToastUtil {
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    public @interface Duration {
    }

    private static Toast toast = null;

    public static void showToast(String text, @Duration int length) {
        //先检查是否在主线程中运行，在进行处理
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Context context = AppApplication.getInstance().getBaseContext();
            if (toast == null) {
                toast = Toast.makeText(context, text, length);
            } else {
                toast.setText(text);
                toast.setDuration(length);
            }
            toast.show();
        }
    }

    public static void showToast(int textResId, @Duration int length) {
        //先检查是否在主线程中运行，在进行处理
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Context context = AppApplication.getInstance().getBaseContext();
            if (toast == null) {
                toast = Toast.makeText(context, textResId, length);
            } else {
                toast.setText(textResId);
                toast.setDuration(length);
            }
            toast.show();
        }
    }

    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int textId) {
        showToast(textId, Toast.LENGTH_SHORT);
    }

}
