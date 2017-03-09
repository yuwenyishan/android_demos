package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func3;
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
                .subscribe(longObservable ->
                                longObservable.subscribe(aLong -> {
                                    Log.d(TAG, "groupJoin: onNext->" + aLong + " ThreadName-> "
                                            + Thread.currentThread().getName());
                                    ToastUtil.showToast("groupJoin onNext-> " + aLong);
                                }), throwable -> Log.e(TAG, "groupJoin: ", throwable)
                        , () -> Log.d(TAG, "groupJoin: onComplete ."));
        subscriptionMap.put("groupJoin", subscription);
    }

    public void merge() {
        //合并多个Observables的发射物
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(20).filter(aLong -> aLong % 2 != 0);
        Observable<Long> observable2 = Observable.interval(1, TimeUnit.SECONDS).take(10).filter(aLong -> aLong % 2 == 0);
        Observable<Long> observable = Observable
                .merge(observable1, observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable
                .subscribe(aLong -> {
                            Log.d(TAG, "merge: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                            ToastUtil.showToast("merge onNext-> " + aLong);
                        }, throwable -> Log.e(TAG, "merge: ", throwable)
                        , () -> Log.d(TAG, "merge: onComplete ."));
        subscriptionMap.put("merge", subscription);
        mergeDelayError();
    }

    private void mergeDelayError() {
        //mergeDelayError操作符会把错误放到所有结果都合并完成之后才执行
        Observable<Long> error = Observable.error(new Exception("this is error ."));
        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS).take(10).filter(aLong -> aLong % 2 != 0)
                .concatWith(error.delay(10, TimeUnit.SECONDS));
        Observable<Long> observable2 = Observable.interval(1, TimeUnit.SECONDS).take(20).filter(aLong -> aLong % 2 == 0);
        Observable<Long> observable = Observable
                .mergeDelayError(observable1, observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable
                .subscribe(aLong -> {
                            Log.d(TAG, "mergeDelayError: onNext->" + aLong + " ThreadName-> " + Thread.currentThread().getName());
                            ToastUtil.showToast("mergeDelayError onNext-> " + aLong);
                        }, throwable -> Log.e(TAG, "mergeDelayError: ", throwable)
                        , () -> Log.d(TAG, "mergeDelayError: onComplete ."));
        subscriptionMap.put("mergeDelayError", subscription);
    }

    public void startWith() {
        //在数据序列的开头插入一条指定的项
        Observable<Integer> observable = Observable.just(4, 5, 6)
                .startWith(1, 2, 3)
                .map(integer -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "startWith: onNext->" + integer + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("startWith onNext-> " + integer);
                }, throwable -> Log.e(TAG, "startWith: ", throwable)
                , () -> Log.d(TAG, "startWith: onComplete ."));
        subscriptionMap.put("startWith", subscription);
    }

    public void concatWith() {
        //在数据的末尾插入指定项
        Observable<Integer> observable = Observable.just(4, 5, 6)
                .concatWith(Observable.just(7, 8, 9))
                .map(integer -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "concatWith: onNext->" + integer + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("concatWith onNext-> " + integer);
                }, throwable -> Log.e(TAG, "concatWith: ", throwable)
                , () -> Log.d(TAG, "concatWith: onComplete ."));
        subscriptionMap.put("concatWith", subscription);
    }

    public void switchOnNext() {
        //将一个发射多个Observables的Observable转换成另一个单独的Observable，后者发射那些Observables最近发射的数据项

        Observable<Observable<String>> observable1 = Observable.create(
                subscriber -> {
                    for (int i = 0; i < 3; i++) {
                        subscriber.onNext(createOb(i));
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                });

        Observable<String> observable = Observable.switchOnNext(observable1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(s -> {
                    Log.d(TAG, "switchOnNext: onNext->" + s + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("switchOnNext onNext-> " + s);
                }, throwable -> Log.e(TAG, "switchOnNext: ", throwable)
                , () -> Log.d(TAG, "switchOnNext: onComplete ."));

        subscriptionMap.put("switchOnNext", subscription);
    }

    private Observable<String> createOb(int index) {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            for (int j = 0; j < 5; j++) {
                subscriber.onNext(j + "-" + index);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw Exceptions.propagate(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public void zip() {
        //通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项。
        //Zip操作符返回一个Obversable，它使用这个函数按顺序结合两个或多个Observables发射的数据项，
        // 然后它发射这个函数返回的结果。它按照严格的顺序应用这个函数。
        // 它只发射与发射数据项最少的那个Observable一样多的数据。
        Observable<Integer> observable1 = Observable.just(1, 2, 3).map(integer -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw Exceptions.propagate(e);
            }
            Log.d(TAG, "zip: ob1->" + integer);
            return integer;
        });
        Observable<Integer> observable2 = Observable.just(1, 2, 3, 4, 5).map(integer -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw Exceptions.propagate(e);
            }
            Log.d(TAG, "zip: ob2->" + integer);
            return integer;
        });
        Observable<Integer> observable3 = Observable.just(1, 2, 3, 4, 5, 6, 7, 8).map(integer -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw Exceptions.propagate(e);
            }
            Log.d(TAG, "zip: ob3->" + integer);
            return integer;
        });

        Observable<Integer> observable = Observable.zip(observable1, observable3, observable2,
                (integer, integer2, integer3) -> {
                    Log.d(TAG, "call: i1:" + integer + "i2:" + integer + "i3:" + integer);
                    return integer + integer2 + integer3;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Log.d(TAG, "zip: start");
        Subscription subscription = observable.subscribe(
                s -> {
                    Log.d(TAG, "zip: onNext->" + s + " ThreadName-> " + Thread.currentThread().getName());
                    ToastUtil.showToast("zip onNext-> " + s);
                }, throwable -> Log.e(TAG, "zip: ", throwable)
                , () -> Log.d(TAG, "zip: onComplete ."));

        subscriptionMap.put("zip", subscription);
    }
}
