package com.jax.reactivex.fun;

import com.jax.reactivex.util.ToastUtil;

import rx.Single;
import rx.SingleSubscriber;
import rx.exceptions.Exceptions;

/**
 * Created on 2017/2/9.
 */

public class SingleDemo {
    private static final String TAG = "SingleDemo";

    public void testSingle(boolean success) {
        Single.just(createErrorResult(success))
                .map(s -> {
                    if (s == null) {
                        throw Exceptions.propagate(new Throwable("Test single onError."));
                    }
                    return s;
                })
                .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String value) {
                        ToastUtil.showToast(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        ToastUtil.showToast(error.getMessage());
                    }
                });
    }

    private String createErrorResult(boolean success) {
        if (success) {
            return "Hello Single.";
        } else {
            return null;
        }
    }
}
