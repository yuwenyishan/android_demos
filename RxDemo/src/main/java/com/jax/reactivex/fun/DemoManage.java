package com.jax.reactivex.fun;

import android.os.Handler;
import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Subscription;

/**
 * Created on 2017/2/15.
 */

public class DemoManage {
    public Executor single = Executors.newSingleThreadExecutor();
    public Handler handler = new Handler();

    public HashMap<String, Subscription> subscriptionMap = new HashMap<>();

    public void onDestroy() {
        for (Map.Entry<String, Subscription> stringSubscriptionEntry : subscriptionMap.entrySet()) {
            Subscription s = stringSubscriptionEntry.getValue();
            if (!s.isUnsubscribed()) {
                s.unsubscribe();
            }
        }
    }

    public void logRx(Observable<Integer> observable, String tag) {
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(getClass().getSimpleName(), tag + " onNext -> : " + integer + " threadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast(tag + " onNext -> " + integer);
                }, throwable -> Log.e(getClass().getSimpleName(), tag + " : onError -> ", throwable)
                , () -> Log.d(getClass().getSimpleName(), tag + " : onComplete -> "));
        subscriptionMap.put(tag, subscription);
    }
}
