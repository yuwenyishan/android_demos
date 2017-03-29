package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

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

    /*
     * Part 5 TimeInterval
     *
     * 将一个发射数据的Observable转换为发射那些数据发射时间间隔的Observable
     */

    /**
     * TimeInterval操作符拦截原始Observable发射的数据项，替换为发射表示相邻发射物时间间隔的对象
     * <p>
     * timeInterval默认在immediate调度器上执行，你可以通过传参数修改。
     */
    public void timeInterval() {
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4)
                .map(integer -> {
                    try {
                        Thread.sleep(integer * 1000);
                    } catch (InterruptedException e) {
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        Observable<TimeInterval<Integer>> longObservable = observable.timeInterval();
        Subscription subscription = longObservable
                .subscribe(integerTimeInterval -> {
                    String s = "timeInterval onNext: " + integerTimeInterval.getValue() +
                            " time: -> " + integerTimeInterval.getIntervalInMilliseconds();
                    Log.d(TAG, s);
                    ToastUtil.showToast(s);
                }, throwable -> Log.e(TAG, "timeInterval: ", throwable), () -> {
                    Log.d(TAG, "timeInterval onCompleted .");
                    ToastUtil.showToast("timeInterval onCompleted .");
                });
        subscriptionMap.put("timeInterval", subscription);
    }

    /*
     * Part 6 Timeout
     *
     * 对原始Observable的一个镜像，如果过了一个指定的时长仍没有发射数据，它会发一个错误通知
     */

    /**
     * 如果原始Observable过了指定的一段时长没有发射任何数据，Timeout操作符会以一个onError通知终止这个Observable。
     */
    public void timeout() {
        Observable<Integer> observable = Observable
                .just(1, 2, 3, 4)
                .map(integer -> {
                    try {
                        Thread.sleep(integer * 1000);
                    } catch (InterruptedException e) {
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
//                .timeout(2500, TimeUnit.MILLISECONDS)
                .timeout(2500, TimeUnit.MILLISECONDS, Observable.range(10, 15))
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "timeout");
    }

    /*
     * Part 7 Timestamp
     *
     * 给Observable发射的数据项附加一个时间戳
     */

    /**
     * 它将一个发射T类型数据的Observable转换为一个发射类型为Timestamped<T>的数据的Observable，
     * 每一项都包含数据的原始发射时间。
     */
    public void timestamp() {
        Observable<Timestamped<Integer>> observable = Observable
                .just(1, 2, 3, 4)
                .map(integer -> {
                    try {
                        Thread.sleep(integer * 1000);
                    } catch (InterruptedException e) {
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                })
                .timestamp()
                .subscribeOn(Schedulers.from(single))
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = observable
                .subscribe(integerTimestamped -> {
                    String s = "timestamp onNext: " + integerTimestamped.getValue() +
                            " timestamp: -> " + integerTimestamped.getTimestampMillis();
                    Log.d(TAG, s);
                    ToastUtil.showToast(s);
                }, throwable -> Log.e(TAG, "timestamp: ", throwable), () -> {
                    Log.d(TAG, "timestamp onCompleted .");
                    ToastUtil.showToast("timestamp onCompleted .");
                });
        subscriptionMap.put("timestamp", subscription);
    }

    /*
     * Part 8 using
     *
     * 创建一个只在Observable生命周期内存在的一次性资源
     */

    /**
     * 当一个观察者订阅using返回的Observable时，
     * using将会使用Observable工厂函数创建观察者要观察的Observable，
     * 同时使用资源工厂函数创建一个你想要创建的资源。当观察者取消订阅这个Observable时，
     * 或者当观察者终止时（无论是正常终止还是因错误而终止），using使用第三个函数释放它创建的资源。
     * <p>
     * using默认不在任何特定的调度器上执行。
     */
    public void using() {
        Observable<Integer> observable =
                Observable.using(() -> {
                            Log.d(TAG, "using: resFactory -> ");
                            return 233;
                        },
                        new Func1<Integer, Observable<Integer>>() {
                            @Override
                            public Observable<Integer> call(Integer s) {
                                Log.d(TAG, "call: observable -> factory ->" + s);
                                return Observable.just(s);
                            }
                        }, s -> {
                            Log.d(TAG, "using: s-> " + s);
                            s = 0;
                            Log.d(TAG, "using: s-> -> " + s);
                        })
                        .subscribeOn(Schedulers.from(single))
                        .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "using");
    }

    /*
     * Part 9 To
     *
     * 将Observable转换为另一个对象或数据结构
     */


    public void to() {
        Observable<Integer> observable = Observable.just(1, 3, 5, 2, 4)
                .map(integer -> {
                    try {
                        Thread.sleep(integer * 100);
//                        Log.d(TAG, "to: map -> " + integer);
                    } catch (InterruptedException e) {
                        throw Exceptions.propagate(e);
                    }
                    return integer;
                });
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "to");
        getIterator(observable);
        toFuture(observable);
        toIterable(observable);
        toList(observable);
        toMap(observable);
        toMultiMap(observable);
        toSortedList(observable);
        nest(observable);
    }

    /**
     * 这个操作符将Observable转换为一个Iterator，你可以通过它迭代原始Observable发射的数据集。
     * <p>
     * getIterator操作符只能用于BlockingObservable的子类，要使用它，
     * 你首先必须把原始的Observable转换为一个BlockingObservable。
     * 可以使用这两个操作符：BlockingObservable.from或the Observable.toBlocking。
     */
    private void getIterator(Observable<Integer> observable) {
        Iterator<Integer> iterator = observable.toBlocking().getIterator();
        while (iterator.hasNext()) {
            Log.d(TAG, "getIterator: -> " + iterator.next());
        }
    }

    /**
     * toFuture操作符也是只能用于BlockingObservable
     * <p>
     * 这个操作符将Observable转换为一个返回单个数据项的Future，
     * 如果原始Observable发射多个数据项，Future会收到一个IllegalArgumentException；
     * 如果原始Observable没有发射任何数据，Future会收到一个NoSuchElementException。
     */
    private void toFuture(Observable<Integer> observable) {
        Future<List<Integer>> future = BlockingObservable.from(observable.toList()).toFuture();
        try {
            Log.d(TAG, "toFuture: --> " + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个操作符将Observable转换为一个Iterable，你可以通过它迭代原始Observable发射的数据集。
     */
    private void toIterable(Observable<Integer> observable) {
        Iterable<Integer> iterable = observable.toBlocking().toIterable();
        for (Integer i : iterable) {
            Log.d(TAG, "toIterable: -> " + i);
        }
    }

    /**
     * 通常，发射多项数据的Observable会为每一项数据调用onNext方法。你可以用toList操作符改变这个行为，
     * 让Observable将多项数据组合成一个List，然后调用一次onNext方法传递整个列表。
     */
    private void toList(Observable<Integer> observable) {
        Observable<List<Integer>> listObservable = observable.toList();
        Subscription subscription = listObservable
                .subscribe(integers -> Log.d(TAG, "call: toList -> " + integers),
                        throwable -> Log.e(TAG, "toList: onError ->", throwable),
                        () -> Log.d(TAG, "toList: onComplete ."));
        subscriptionMap.put("toList", subscription);
    }

    /**
     * toMap收集原始Observable发射的所有数据项到一个Map（默认是HashMap）然后发射这个Map
     * 你可以提供一个用于生成Map的Key的函数，还可以提供一个函数转换数据项到Map存储的值（默认数据项本身就是值）。
     * <p>
     * ps:相同的键所对于的值会被覆盖
     */
    private void toMap(Observable<Integer> observable) {
        Observable<Map<String, Integer>> mapObservable = observable
//                .toMap(integer -> integer > 3 ? ">3" : (integer == 3 ? "=3" : "<3"));
                .toMap(integer -> integer > 3 ? ">3" : (integer == 3 ? "=3" : "<3"),
                        integer -> integer * integer);

        Subscription subscription = mapObservable
                .subscribe(stringIntegerMap -> {
                            Set<Map.Entry<String, Integer>> entries = stringIntegerMap.entrySet();
                            for (Map.Entry<String, Integer> entry : entries) {
                                Log.d(TAG, "toMap: onNext -> " + "key -> " + entry.getKey() + " value -> " + entry.getValue());
                            }
                        }, throwable -> Log.e(TAG, "toMap: onError ->", throwable),
                        () -> Log.d(TAG, "toMap: onComplete ."));
        subscriptionMap.put("toMap", subscription);
    }

    /**
     * toMultiMap类似于toMap
     * 不同的是，它生成的这个Map同时还是一个ArrayList（默认是这样，你可以传递一个可选的工厂方法修改这个行为）。
     * <p>
     * 相同的键，不同的值会存储再collection中
     */
    private void toMultiMap(Observable<Integer> observable) {
        Observable<Map<String, Collection<Integer>>> listObservable = observable
                .toMultimap(integer -> integer > 3 ? ">3" : (integer == 3 ? "=3" : "<3")
                        , integer -> integer * integer);
        Subscription subscription = listObservable
                .subscribe(stringCollectionMap -> {
                            Set<Map.Entry<String, Collection<Integer>>> sets = stringCollectionMap.entrySet();
                            for (Map.Entry<String, Collection<Integer>> s : sets) {
                                Log.d(TAG, "toMultiMap: onNext -> " + "key: " + s.getKey() + " value: " + s.getValue().toString());
                            }
                        },
                        throwable -> Log.e(TAG, "toMultiMap: onError ->", throwable),
                        () -> Log.d(TAG, "toMultiMap: onComplete ."));
        subscriptionMap.put("toMultiMap", subscription);
    }

    /**
     * toSortedList类似于toList
     * 不同的是，它会对产生的列表排序，默认是自然升序，如果发射的数据项没有实现Comparable接口，会抛出一个异常。
     */
    private void toSortedList(Observable<Integer> observable) {
        Observable<List<Integer>> list = observable.toSortedList((integer, integer2) -> {
            //倒序
            return integer2 < integer ? -1 : 1;
        });
        Subscription subscription = list
                .subscribe(integers -> Log.d(TAG, "toSortedList: onNext -> " + integers),
                        throwable -> Log.e(TAG, "toSortedList: onError ->", throwable),
                        () -> Log.d(TAG, "toSortedList: onComplete ."));
        subscriptionMap.put("toSortedList", subscription);
    }

    private void nest(Observable<Integer> observable) {
        Observable<Observable<Integer>> nest = observable.nest();
        Subscription subscription = nest
                .subscribe(observable1 -> observable1.subscribe(
                        integers -> Log.d(TAG, "nest nest: onNext -> " + integers),
                        throwable -> Log.e(TAG, "nest nest: onError ->", throwable),
                        () -> Log.d(TAG, "nest nest: onComplete .")),
                        throwable -> Log.e(TAG, "nest: onError ->", throwable),
                        () -> Log.d(TAG, "nest: onComplete ."));
        subscriptionMap.put("nest", subscription);
    }
}
