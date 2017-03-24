package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/3/21.
 */

public class AuxiliaryOperationDemo extends DemoManage {

    private static final String TAG = "AuxiliaryOperationDemo";

    /*
     * Part1 delay delaySubscription
     *
     * delay 延时发射Observable的结果
     * delaySubscription 延时处理订阅请求
     */

    /**
     * 延迟一段指定的时间再发射来自Observable的发射物
     */
    public void delay() {
        Log.d(TAG, "delay: start-->");
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return Observable.just(integer).delay(integer, TimeUnit.SECONDS);
                    }
                })
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "delay");
        Log.d(TAG, "delay: end-->");
    }

    public void delaySubscription() {
        Log.d(TAG, "delaySubscription: start-->");
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
                .delaySubscription(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "delay");
        Log.d(TAG, "delaySubscription: end-->");
    }

      /*
     * Part2 Do 注册一个动作作为原始Observable生命周期事件的一种占位符
     * doOnEach doOnNextErrorComplete doOnSubscribe doOnUnsubscribe doOnComplete doOnError doOnTerminate finallyDo
     *
     * doOnEach 注册一个动作，对Observable发射的每个数据项使用
     * doOnNextErrorComplete doOnNext操作符类似于doOnEach(Action1)，但是它的Action不是接受一个Notification参数，而是接受发射的数据项。
     * doOnSubscribe操作符注册一个动作，当观察者订阅它生成的Observable它就会被调用
     * doOnUnsubscribe操作符注册一个动作，当观察者取消订阅它生成的Observable它就会被调用。
     * doOnTerminate 操作符注册一个动作，当它产生的Observable终止之前会被调用，无论是正常还是异常终止。
     * finallyDo 操作符注册一个动作，当它产生的Observable终止之后会被调用，无论是正常还是异常终止。
     */

    /**
     * doOnEach操作符让你可以注册一个回调，它产生的Observable每发射一项数据就会调用它一次。
     * 你可以以Action的形式传递参数给它，这个Action接受一个onNext的变体Notification作为它的唯一参数，
     * 你也可以传递一个Observable给doOnEach，这个Observable的onNext会被调用，就好像它订阅了原始的Observable一样。
     */
    public void doOnEach() {

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "observe onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "observe onError: ", e);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "observe onNext: " + integer);
                if (integer > 3) {
                    throw new RuntimeException("数据大于3了-->" + integer);
                }
            }
        };
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
//                .doOnEach(notification -> {
//                    Log.d(TAG, "doOnEach call: notification--> " + notification.getValue()
//                            + " type -> " + notification.getKind());
//                    if (notification.getKind() == Notification.Kind.OnNext) {
//                        if ((Integer) notification.getValue() > 3) {
//                            throw new RuntimeException("数据大于3了");
//                        }
//                    }
//                })
                .doOnEach(observer)
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "doOnEach");
    }

    /**
     * doOnNext操作符类似于doOnEach(Action1)，但是它的Action不是接受一个Notification参数，而是接受发射的数据项。
     * doOnCompleted 操作符注册一个动作，当它产生的Observable正常终止调用onCompleted时会被调用
     * doOnError 操作符注册一个动作，当它产生的Observable异常终止调用onError时会被调用。
     */
    public void doOnNextErrorComplete() {
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return integer;
                })
                .doOnNext(integer -> {
                    Log.d(TAG, "doOnNextErrorComplete call: --> " + integer);
                    if (integer > 3) {
                        throw new RuntimeException("item exceeds maximum value.");
                    }
                })
                .doOnError(throwable -> Log.e(TAG, "doOnError: ", throwable))
                .doOnCompleted(() -> Log.d(TAG, "doOnCompleted:"))
                .onErrorReturn(throwable -> 233)
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "doOnNextErrorComplete");
    }

    /**
     * doOnSubscribe操作符注册一个动作，当观察者订阅它生成的Observable它就会被调用。
     * doOnUnsubscribe操作符注册一个动作，当观察者取消订阅它生成的Observable它就会被调用。
     */
    public void doOnSubscribeOrNot() {
        Log.d(TAG, "doOnSubscribeOrNot: start-->");
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
                .doOnSubscribe(() -> Log.d(TAG, "doOnSubscribeOrNot: doOnSubscribe --> Subscribe"))
                .doOnUnsubscribe(() -> Log.d(TAG, "doOnSubscribeOrNot: doOnUnsubscribe --> Unsubscribe"))
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        handler.postDelayed(() -> logRx(observable, "doOnSubscribeOrNot"), 3000);
        Log.d(TAG, "doOnSubscribeOrNot: end-->");
    }

    /**
     * doOnTerminate 操作符注册一个动作，当它产生的Observable终止之前会被调用，无论是正常还是异常终止。
     * finallyDo 操作符注册一个动作，当它产生的Observable终止之后会被调用，无论是正常还是异常终止。
     */
    public void doOnTerminateFinally() {
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4, 5, 6)
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return integer;
                })
                .doOnSubscribe(() -> Log.d(TAG, "doOnTerminateFinally: doOnSubscribe --> Subscribe"))
                .doOnUnsubscribe(() -> Log.d(TAG, "doOnTerminateFinally: doOnUnsubscribe --> Unsubscribe"))
                .doOnTerminate(() -> Log.d(TAG, "doOnTerminateFinally: doOnTerminate --> terminate "))
                .doAfterTerminate(() -> Log.d(TAG, "doOnTerminateFinally: doAfterTerminate(finallyDo) -->finallyDo"))
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "doOnTerminateFinally");
    }

    /*
     * Part 3 Materialize/Dematerialize
     * Materialize将数据项和事件通知都当做数据项发射，Dematerialize刚好相反。
     */

    /**
     * RxJava的materialize将来自原始Observable的通知转换为Notification对象，然后它返回的Observable会发射这些数据。
     */
    public void materialize() {
        Observable<Notification<Integer>> observable = Observable
                .just(1, 2, 3, 4, 5)
                .map(integer -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return integer;
                })
                .materialize()
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable
                .subscribe(integerNotification -> {
                            String s = "call: " + integerNotification.getValue()
                                    + " key --> " + integerNotification.getKind();
                            Log.d(TAG, s);
                            ToastUtil.showToast(s);
                        },
                        throwable -> Log.e(TAG, "materialize: onError-> ", throwable),
                        () -> Log.d(TAG, "materialize: onComplete ."));
        subscriptionMap.put("Materialize", subscription);
    }

    /**
     * Dematerialize操作符是Materialize的逆向过程，它将Materialize转换的结果还原成它原本的形式。
     * 在调用dematerialize()之前必须先调用materialize()。
     */
    public void dematerialize() {
        Observable<Object> observable = Observable
                .just(1, 2, 3, 4, 5)
                .map(integer -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return integer;
                })
                .materialize()
                .dematerialize()
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable
                .subscribe(o -> {
                            Log.d(TAG, "dematerialize: onNext" + o);
                            ToastUtil.showToast("dematerialize onNext " + o);
                        },
                        throwable -> Log.e(TAG, "dematerialize: onError-> ", throwable),
                        () -> Log.d(TAG, "dematerialize: onComplete ."));
        subscriptionMap.put("dematerialize", subscription);
    }

    /*
     * Part 4 Serialize
     * 强制一个Observable连续调用并保证行为正确
     */

    public void serialize() {
        serializeTest1();
        serializeTest2();
        Observable<Integer> observable = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onCompleted();
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                })
                .doOnUnsubscribe(() -> Log.d(TAG, "serialize: unsubscribe"))
                .serialize()
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        Subscription sub = observable
                .unsafeSubscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "serialize onCompleted .");
                        ToastUtil.showToast("serialize onCompleted .");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "serialize onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        String s = "serialize onNext: " + integer;
                        Log.d(TAG, s);
                        ToastUtil.showToast(s);
                    }
                });
        subscriptionMap.put("serialize", sub);
    }

    private void serializeTest1() {
        Observable<Integer> observable = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onCompleted();
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                })
                .doOnUnsubscribe(() -> Log.d(TAG, "serializeTest1: unsubscribe"))
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "serializeTest1");
    }

    private void serializeTest2() {
        Observable<Integer> observable = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onCompleted();
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                })
                .doOnUnsubscribe(() -> Log.d(TAG, "serializeTest2: unsubscribe"))
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        Subscription sub = observable
                .unsafeSubscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "serializeTest2 onCompleted .");
                        ToastUtil.showToast("serializeTest2 onCompleted .");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "serializeTest2 onError: ", e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        String s = "serializeTest2 onNext: " + integer;
                        Log.d(TAG, s);
                        ToastUtil.showToast(s);
                    }
                });
        subscriptionMap.put("serializeTest2", sub);
    }


}
