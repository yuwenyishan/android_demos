package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/21.
 */

public class CombineDemo extends DemoManage {
    private static final String TAG = "CombineDemo";

    public void combineLatest() {
        //当两个Observables中的任何一个发射了数据时，使用一个函数结合每个Observable发射的最近数据项，
        // 并且基于这个函数的结果发射数据
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(20);
        Observable<Long> observable2 = Observable.interval(2, TimeUnit.SECONDS).take(10);
        Observable<Long> observable = Observable.combineLatest(observable1, observable2,
                (aLong, aLong2) -> {
                    Log.d(TAG, "call: a-->" + aLong + "b-->" + aLong2);
                    return aLong + aLong2;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "combineLatest: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("combineLatest onNext-> " + aLong);
                }, throwable -> Log.e(TAG, "combineLatest: ", throwable)
                , () -> Log.d(TAG, "combineLatest: onComplete ."));
        subscriptionMap.put("combineLatest", subscription);
    }
}
