package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(20).map(aLong -> aLong * 5);
        Observable<Long> observable2 = Observable.interval(3, TimeUnit.SECONDS).take(10).map(aLong -> aLong * 10);
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

    public void join() {
        //任何时候，只要在另一个Observable发射的数据定义的时间窗口内，这个Observable发射了一条数据，就结合两个Observable发射的数据。
        //join默认不在任何特定的调度器上执行。
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(20).map(aLong -> {
            Log.d(TAG, "join: ob1-->" + aLong * 5);
            return aLong * 5;
        });
        Observable<Long> observable2 = Observable.interval(3, TimeUnit.SECONDS).take(10).map(aLong -> {
            Log.d(TAG, "join: ob2-->" + aLong * 10);
            return aLong * 10;
        });
        Subscription subscription = observable1.join(observable2,
                aLong -> {
                    Log.d(TAG, "join: left-->" + aLong);
                    return Observable.just(aLong).delay(1, TimeUnit.SECONDS);
                },
                tRight -> {
                    Log.d(TAG, "join: right-->" + tRight);
                    return Observable.just(tRight).delay(2, TimeUnit.SECONDS);
                },
                (aLong, tRight) -> {
                    Log.d(TAG, "join: left-->" + aLong + "--right-->" + tRight);
                    return aLong + tRight;
                })
                .subscribe(aLong -> {
                            Log.d(TAG, "join: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                            ToastUtil.showToast("join onNext-> " + aLong);
                        }, throwable -> Log.e(TAG, "join: ", throwable)
                        , () -> Log.d(TAG, "join: onComplete ."));
        subscriptionMap.put("join", subscription);
        groupJoin();
    }

    private void groupJoin() {
//        groupJoin操作符非常类似于join操作符，区别在于join操作符中第四个参数的传入函数不一致
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(20).map(aLong -> {
            Log.d(TAG, "groupJoin: ob1-->" + aLong * 5);
            return aLong * 5;
        });
        Observable<Long> observable2 = Observable.interval(3, TimeUnit.SECONDS).take(10).map(aLong -> {
            Log.d(TAG, "groupJoin: ob2-->" + aLong * 10);
            return aLong * 10;
        });
        Subscription subscription = observable1.groupJoin(observable2,
                aLong -> {
                    Log.d(TAG, "groupJoin: left-->" + aLong);
                    return Observable.just(aLong).delay(1, TimeUnit.SECONDS);
                },
                tRight -> {
                    Log.d(TAG, "groupJoin: right-->" + tRight);
                    return Observable.just(tRight).delay(2, TimeUnit.SECONDS);
                },
                (aLong, longObservable) -> longObservable.map(a -> a + aLong))
                .subscribe(longObservable -> {
                            longObservable.subscribe(aLong -> {
                                Log.d(TAG, "groupJoin: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                                ToastUtil.showToast("groupJoin onNext-> " + aLong);
                            });
                        }, throwable -> Log.e(TAG, "groupJoin: ", throwable)
                        , () -> Log.d(TAG, "groupJoin: onComplete ."));
        subscriptionMap.put("groupJoin", subscription);
    }
}
