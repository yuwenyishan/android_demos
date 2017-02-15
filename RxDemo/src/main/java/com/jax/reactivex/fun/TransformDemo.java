package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/13.
 */

public class TransformDemo {
    private static final String TAG = "TransformDemo";
    private HashMap<String, Subscription> subscriptionList = new HashMap<>();

    public void onDestroy() {
        for (Map.Entry<String, Subscription> stringSubscriptionEntry : subscriptionList.entrySet()) {
            Subscription s = stringSubscriptionEntry.getValue();
            if (!s.isUnsubscribed()) {
                s.unsubscribe();
            }
        }
    }

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
        subscriptionList.put("buffer", subscription);
    }

    public void flatMap() {
        flatConcatMap();
//        flatMapIterable();
    }

    private void flatConcatMap() {
        Observable.just(1, 2, 3, 4, 5, 6)
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            Log.d(TAG, "flatMap onNext: -->integer:" + s + " thread name:-->" + Thread.currentThread().getName());
                            ToastUtil.showToast("flatMap onNext " + s);
                        }, throwable -> Log.e(TAG, "flatMap onError: ", throwable),
                        () -> Log.d(TAG, "flatMap onCompleted: flatMap send completed ."));
    }

    private void flatMapIterable() {
        //flatMapIterable这个变体成对的打包数据，然后生成Iterable而不是原始数据和生成的Observables，但是处理方式是相同的。
        Observable.just(1, 2, 3, 4, 5)
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            Log.d(TAG, "flatMapIterable onNext: -->integer:" + s + " thread name:-->" + Thread.currentThread().getName());
                            ToastUtil.showToast("flatMapIterable onNext " + s);
                        }, throwable -> Log.e(TAG, "flatMapIterable onError: ", throwable),
                        () -> Log.d(TAG, "flatMapIterable onCompleted: flatMapIterable send completed ."));
    }


}
