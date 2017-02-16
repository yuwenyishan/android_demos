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
        subscriptionMap.put("debounce", subscription);
    }

    public void distinct() {
        //抑制（过滤掉）重复的数据项,Distinct的过滤规则是：只允许还没有发射过的数据项通过。
        Observable<Integer> observable = Observable.just(1, 2, 3, 1, 4, 3, 5, 6, 3)
                .map(integer -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                }).distinct()
                .subscribeOn(Schedulers.io());
        Subscription subscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                            Log.d(TAG, "distinct: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                            ToastUtil.showToast("distinct onNext-> " + aLong);
                        }
                        , throwable -> Log.e(TAG, "distinct: onError", throwable)
                        , () -> Log.d(TAG, "distinct onComplete ."));
        subscriptionMap.put("distinct", subscription);
        distinctFun();
        distinctUntilChanged();
    }

    private void distinctFun() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .distinct(integer -> "key:" + integer % 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
            Log.d(TAG, "call: " + integer);
        });
        subscriptionMap.put("distinctFun", subscription);
    }

    private void distinctUntilChanged() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 4, 5, 6, 7, 8, 9)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
            Log.d(TAG, "call: " + integer);
        });
        subscriptionMap.put("distinctUntilChanged", subscription);
    }

    public void elementAt() {
        //只发射第N项数据
        //elementAt和elementAtOrDefault默认不在任何特定的调度器上执行。
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 4, 5, 6, 7, 8, 9)
                .distinctUntilChanged()
                .elementAt(4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "elementAt: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("elementAt onNext-> " + aLong);
                }
                , throwable -> Log.e(TAG, "elementAt: onError", throwable)
                , () -> Log.d(TAG, "elementAt onComplete ."));
        subscriptionMap.put("elementAt", subscription);
        elementAtOrDefault();
    }

    private void elementAtOrDefault() {
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 4, 5, 6, 7, 8, 9)
                .distinctUntilChanged()
                .elementAtOrDefault(10, 666)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "elementAtOrDefault: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("elementAtOrDefault onNext-> " + aLong);
                }
                , throwable -> Log.e(TAG, "elementAtOrDefault: onError", throwable)
                , () -> Log.d(TAG, "elementAtOrDefault onComplete ."));
        subscriptionMap.put("elementAtOrDefault", subscription);
    }
}
