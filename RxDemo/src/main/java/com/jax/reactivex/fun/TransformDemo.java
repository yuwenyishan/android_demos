package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/13.
 */

public class TransformDemo extends DemoManage {
    private static final String TAG = "TransformDemo";

    public void buffer() {
        //定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
        Integer[] ds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        Observable<Integer> observable = Observable.from(ds);
        Observable<Integer> bufferControl = Observable.range(1, 5, Schedulers.io()).map(integer -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw Exceptions.propagate(e);
            }
            return integer;
        });
        observable
                .map(integer -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    Log.d(TAG, "buffer map1 call: " + integer);
                    return integer;
                })
                .buffer(3, 2)//根据个数间隔缓冲
//                .buffer(1500, TimeUnit.MILLISECONDS)//根据时间缓冲1.5s收集一次数据
//                .buffer(3, TimeUnit.SECONDS, 2)//也可以同时用数目和时间作为缓冲条件，任意一个条件满足了就发射缓冲到的数据
//                .buffer(Observable.timer(7, TimeUnit.SECONDS))//除了时间和数目以外，还可以使用另外一个 Observable 作为信号来缓冲数据，当信号 Observable 的 onNext 事件触发的时候，源 Observable 中就发射缓冲的数据。
//                .buffer(bufferControl)//使用一个函数来返回一个信号Observable，而上面是直接使用信号 Observable，区别是，只有当有Subscriber 订阅的时候，使用函数返回的信号 Observable 才开始执行。
//                .buffer(Observable.interval(500, TimeUnit.MILLISECONDS), along -> Observable.timer(5, TimeUnit.SECONDS))
//                .takeLastBuffer(3)//该函数返回最后 N 个数据。
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integers -> {
                    Log.d(TAG, "onNext: -->integer:" + integers + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("buffer onNext " + integers);
                }, throwable -> Log.e(TAG, "buffer onError: ", throwable),
                () -> Log.d(TAG, "buffer onCompleted: timer send completed ."));
//        bufferControl.subscribe(integer -> {
//            Log.d(TAG, "buffer: control : " + integer);
//        });
        subscriptionMap.put("buffer", subscription);
    }

    public void flatMap() {
        flatConcatMap();
//        flatMapIterable();
    }

    private void flatConcatMap() {
        //FlatMap将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable

        Observable<String> observable = Observable.just(1, 2, 3, 4, 5, 6)
//                .flatMap(new Func1<Integer, Observable<String>>() {//FlatMap对这些Observables发射的数据做的是合并(merge)操作，因此它们可能是交错的。
//                .concatMap(new Func1<Integer, Observable<String>>() {//concatMap操作符，它类似于最简单版本的flatMap，但是它按次序连接而不是合并那些生成的Observables，然后产生自己的数据序列。
                .switchMap(new Func1<Integer, Observable<String>>() {//：当原始Observable发射一个新的数据（Observable）时，它将取消订阅并停止监视产生执之前那个数据的Observable，只监视当前这一个。
                    @Override
                    public Observable<String> call(Integer integer) {
                        int delay = 1500;
                        if (integer > 2) {
                            delay = 900;
                        }
                        return Observable.just(integer * integer + "").delay(delay, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(s -> {
                    Log.d(TAG, "flatMap onNext: -->integer:" + s + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("flatMap onNext " + s);
                }, throwable -> Log.e(TAG, "flatMap onError: ", throwable),
                () -> Log.d(TAG, "flatMap onCompleted: flatMap send completed ."));
        subscriptionMap.put("flatConcatMap", subscription);
    }

    private void flatMapIterable() {
        //flatMapIterable这个变体成对的打包数据，然后生成Iterable而不是原始数据和生成的Observables，但是处理方式是相同的。
        Observable<String> observable = Observable.just(1, 2, 3, 4, 5)
                .flatMapIterable(new Func1<Integer, Iterable<String>>() {
                    @Override
                    public Iterable<String> call(Integer integer) {
                        ArrayList<String> integers = new ArrayList<>();
                        integers.add(integer + " 1^");
                        integers.add(integer * integer + " 2^");
                        integers.add(integer * integer * integer + " 3^");
                        return integers;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(s -> {
                    Log.d(TAG, "flatMapIterable onNext: -->integer:" + s + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("flatMapIterable onNext " + s);
                }, throwable -> Log.e(TAG, "flatMapIterable onError: ", throwable),
                () -> Log.d(TAG, "flatMapIterable onCompleted: flatMapIterable send completed ."));
        subscriptionMap.put("flatMapIterable", subscription);
    }

    public void groupBy() {
        //将一个Observable分拆为一些Observables集合，它们中的每一个发射原始Observable的一个子序列
        Observable<GroupedObservable<Long, Long>> observable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .take(20)
                .groupBy(aLong -> {// //按照key为0,1,2分为3组
                    Log.d(TAG, "groupBy call: Thread Name:-->" + Thread.currentThread().getName());
                    return aLong % 3;
                });
        Subscription groupS = observable.subscribe(longLongGroupedObservable -> {
            Subscription s = longLongGroupedObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                                Log.d(TAG, "groupBy onNext:key-->" + longLongGroupedObservable.getKey()
                                        + " -->integer:" + aLong + " thread name:-->" + Thread.currentThread().getName());
                                ToastUtil.showToast("groupBy onNext " + aLong + " key-->" + longLongGroupedObservable.getKey());
                            }, throwable -> Log.e(TAG, "groupBy onError: ", throwable),
                            () -> Log.d(TAG, "groupBy onCompleted: groupBy send completed ."));
            subscriptionMap.put(longLongGroupedObservable.getKey() + "", s);
        });
        subscriptionMap.put("groupBy", groupS);
    }

    public void map() {
        //对Observable发射的每一项数据应用一个函数，执行变换操作
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5)
                .map(integer -> integer * 3)//每个数乘以3
//                .cast(Integer.class)//操作符将原始Observable发射的每一项数据都强制转换为一个指定的类型，然后再发射数据，它是map的一个特殊版本。
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
        Subscription s = observable.subscribe(integer -> {
                    Log.d(TAG, "map: onNext-->" + integer + " ThreadName--> " + Thread.currentThread().getName());
                    ToastUtil.showToast("map: onNext-->" + integer);
                }, throwable -> Log.e(TAG, "map: OnError-->", throwable),
                () -> Log.d(TAG, "map: onComplete--> map Demo complete ."));
        subscriptionMap.put("map", s);
    }

    public void scan() {
        //连续地对数据序列的每一项应用一个函数，然后连续发射结果
        //scan操作符通过遍历源Observable产生的结果，依次对每一个结果项按照指定规则进行运算，
        // 计算后的结果作为下一个迭代项参数，每一次迭代项都会把计算结果输出给订阅者。
        //这个操作符默认不在任何特定的调度器上执行。
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5)
//                .scan((sum, item) -> sum + item)// //参数sum就是上一次的计算结果
                .scan(10, (sum, item) -> sum + item)// //10,为种子值参数sum就是上一次的计算结果
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
                    Log.d(TAG, "scan: onNext-->" + integer + " ThreadName--> " + Thread.currentThread().getName());
                    ToastUtil.showToast("scan: onNext-->" + integer);
                }, throwable -> Log.e(TAG, "scan: OnError-->", throwable),
                () -> Log.d(TAG, "scan: onComplete--> scan Demo complete ."));
        subscriptionMap.put("Scan", subscription);
    }

    public void window() {
        //定期将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，而不是每次发射一项数据
        //window操作符非常类似于buffer操作符，区别在于buffer操作符产生的结果是一个List缓存，
        // 而window操作符产生的结果是一个Observable，订阅者可以对这个结果Observable重新进行订阅处理。

        Observable<Observable<Long>> observable =
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                        .take(10)
                        .window(3, TimeUnit.SECONDS);
        Subscription subscription = observable.subscribe(longObservable -> {
                    Log.d(TAG, "window: item begin");
                    Subscription itemS = longObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                        Log.d(TAG, "window item: onNext-->" + aLong + " ThreadName--> " + Thread.currentThread().getName());
                                        ToastUtil.showToast("window item: onNext-->" + aLong);
                                    }, throwable -> Log.e(TAG, "window item: OnError-->", throwable),
                                    () -> Log.d(TAG, "window item: onComplete--> window Demo complete ."));
                    subscriptionMap.put(longObservable.hashCode() + "", itemS);
                }, throwable -> Log.e(TAG, "window: OnError-->", throwable),
                () -> Log.d(TAG, "window: onComplete--> window Demo complete ."));
        subscriptionMap.put("window", subscription);
    }
}
