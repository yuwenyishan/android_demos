package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
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

    public void filter() {
        //只发射通过了谓词测试的数据项
        //filter默认不在任何特定的调度器上执行。
        Observable<Integer> observable = Observable.just(1, 2, 4, 8, 65, 8, 4, 26, 58)
                .map(integer -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
                .filter(integer -> integer > 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "filter: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("filter onNext-> " + aLong);
                }
                , throwable -> Log.e(TAG, "filter: onError", throwable)
                , () -> Log.d(TAG, "filter onComplete ."));
        subscriptionMap.put("filter", subscription);
    }

    public void first() {
        //只发射第一项（或者满足某个条件的第一项）数据
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5)
//                .first(integer -> integer > 3);
                .firstOrDefault(10, integer -> integer > 5);//firstOrDefault与first类似，但是在Observagle没有发射任何数据时发射一个你在参数中指定的默认值。
//                .takeFirst(integer -> integer > 10);//如果原始Observable没有发射任何满足条件的数据，first会抛出一个NoSuchElementException，takeFist会返回一个空的Observable（不调用onNext()但是会调用onCompleted）。
//                .single(integer -> integer > 3);//single操作符也与first类似，但是如果原始Observable在完成之前不是正好发射一次数据，它会抛出一个NoSuchElementException。
//                .singleOrDefault(10, integer -> integer > 5);
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "first: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("first onNext-> " + aLong);
                }
                , throwable -> Log.e(TAG, "first: onError", throwable)
                , () -> Log.d(TAG, "filter onComplete ."));
        subscriptionMap.put("first", subscription);
    }

    public void ignoreElements() {
        //不发射任何数据，只发射Observable的终止通知
        //ignoreElements默认不在任何特定的调度器上执行。
        //这里将不会调用onNext.
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5)
                .first(integer -> integer > 3)
                .ignoreElements();//忽略onNext结果，只通知完成和错误。
        Subscription subscription = observable.subscribe(aLong -> {
                    Log.d(TAG, "ignoreElements: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("ignoreElements onNext-> " + aLong);
                }
                , throwable -> Log.e(TAG, "ignoreElements: onError", throwable)
                , () -> Log.d(TAG, "ignoreElements onComplete ."));
        subscriptionMap.put("ignoreElements", subscription);
    }
}
