package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/15.
 */

public class FilterDemo extends DemoManage {
    private static final String TAG = "FilterDemo";

    public void debounce() {
        //仅在过了一段指定的时间还没发射数据时才发射一个数据
        //Debounce操作符会过滤掉发射速率过快的数据项。
        Observable<Long> observable = Observable
                .create(new Observable.OnSubscribe<Long>() {
                    @Override
                    public void call(Subscriber<? super Long> subscriber) {
                        if (subscriber.isUnsubscribed()) {
                            return;
                        }
                        try {
                            for (long i = 1; i < 10; i++) {
                                subscriber.onNext(i);
                                Thread.sleep(i * 100);
                            }
                            subscriber.onCompleted();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw Exceptions.propagate(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
//                .throttleWithTimeout(1, TimeUnit.SECONDS, Schedulers.io());
                .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io());//超时时间为500毫秒
        Subscription subscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                            Log.d(TAG, "debounce: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                            ToastUtil.showToast("debounce onNext-> " + aLong);
                        }
                        , throwable -> Log.e(TAG, "debounce: onError", throwable)
                        , () -> Log.d(TAG, "debounce onComplete ."));
        requestList.put("debounce", subscription);
    }
}
