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
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

/**
 * Created on 2017/2/10.
 */

public class CreateDemo extends DemoManage {
    private static final String TAG = "CreateDemo";

    public void create() {
        //https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Create.html
        //create方法默认不在任何特定的调度器上执行。默认在main线程
        Observable<Integer> create = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    try {
                        Log.d(TAG, "create: thread name -->" + Thread.currentThread().getName());
                        if (!subscriber.isUnsubscribed()) {
                            for (int i = 1; i <= 5; i++) {
                                subscriber.onNext(i);
                                Thread.sleep(2000);
                            }
                            subscriber.onCompleted();
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = create.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "OnCompleted: complete" + " thread name:-->" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + " thread name:-->" + Thread.currentThread().getName(), e);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer + " thread name:-->" + Thread.currentThread().getName());
                ToastUtil.showToast("onNext:" + integer);
            }
        });
        subscriptionMap.put("create", subscription);
    }

    private int testInteger = 12;

    public void defer() {
        //直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
        //defer方法默认不在任何特定的调度器上执行。
        Observable<Integer> observable = Observable.defer(() -> Observable.just(testInteger));
        testInteger = 15;
        Subscription subscription = observable.subscribe(integer -> {
            Log.d(TAG, "call: defer:-->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
            ToastUtil.showToast("onNext:" + integer);
        });
        subscriptionMap.put("defer", subscription);
    }

    private boolean sendEmpty = false;
    private boolean sendNever = false;
    private boolean sendError = false;

    public void empty() {
        //创建一个不发射任何数据但是正常终止的Observable
        sendEmpty = true;
        Observable
                .defer(() -> sendEmpty ? Observable.empty() : Observable.just(testInteger))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: over");
                        ToastUtil.showToast("onComplete:不发射任何数据,正常结束");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: -->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
                        ToastUtil.showToast("onNext" + integer);
                    }
                });
    }

    public void never() {
        //创建一个不发射数据也不终止的Observable
        //这里将收不到日志
        sendNever = true;
        Observable
                .defer(() -> sendNever ? Observable.never() : Observable.just(testInteger))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: over");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: error");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: -->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
                        ToastUtil.showToast("onNext" + integer);
                    }
                });
    }

    public void throwE() {
        //创建一个不发射数据以一个错误终止的Observable
        sendError = true;
        Observable
                .defer(() -> sendError ? Observable.error(new Throwable("This is error test .")) : Observable.just(testInteger))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: over");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: error" + e.getMessage());
                        ToastUtil.showToast("不发送数据，且以错误终止，onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: -->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
                        ToastUtil.showToast("onNext" + integer);
                    }
                });
    }

    public void from() {
        //将其它种类的对象和数据类型转换为Observable
        //在RxJava中，from操作符可以转换Future、Iterable和数组。对于Iterable和数组，产生的Observable会发射Iterable或数组的每一项数据。
        //from默认不在任何特定的调度器上执行。然而你可以将Scheduler作为可选的第二个参数传递给Observable，它会在那个调度器上管理这个Future。
        Integer[] items = {0, 1, 2, 3, 4, 5};
        Observable<Integer> observable = Observable.from(items)
                .delay(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return Observable.just(integer);
                })//延迟发射下，否则看不到效果直接输出最后一个了。
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: formDemo send completed .");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "formDemo onError: ", e);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: -->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
                ToastUtil.showToast("onNext" + integer);
            }
        });
        subscriptionMap.put("from", subscription);
    }

    public void interval() {
        //创建一个按固定时间间隔发射整数序列的Observable
        //interval默认在computation调度器上执行。你也可以传递一个可选的Scheduler参数来指定调度器。
        Observable<Long> observable = Observable.interval(2, TimeUnit.SECONDS, Schedulers.io())
                .map(aLong -> {
                    Log.d(TAG, "interval map call: " + aLong + " thread name:-->" + Thread.currentThread().getName());
                    return aLong;
                }).observeOn(AndroidSchedulers.mainThread());
        Subscription su = observable.subscribe(aLong -> {
                    Log.d(TAG, "onNext: -->testInteger:" + aLong + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("interval onNext" + aLong);
                },
                throwable -> Log.e(TAG, "interval onError: ", throwable),
                () -> Log.d(TAG, "onCompleted: interval send completed ."));
        subscriptionMap.put("interval", su);//interval 需要手动取消订阅，否则回一直发射整数！
    }

    public void just() {
        //创建一个发射指定值的Observable
        //Just类似于From，但是From会将数组或Iterable的数据取出然后逐个发射，而Just只是简单的原样发射，将数组或Iterable当做单个数据。
        //默认在当前线程运行
        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5)
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    Log.d(TAG, "interval map call: " + integer + " thread name:-->" + Thread.currentThread().getName());
                    return integer;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "onNext: -->integer:" + integer + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("just onNext" + integer);
                }, throwable -> Log.e(TAG, "just onError: ", throwable)
                , () -> Log.d(TAG, "onCompleted: just send completed ."));
        subscriptionMap.put("just", subscription);
    }

    public void range() {
        //创建一个发射特定整数序列的Observable
        //Range操作符发射一个范围内的有序整数序列，你可以指定范围的起始和长度。
        //range默认不在任何特定的调度器上执行。有一个变体可以通过可选参数指定Scheduler。
        Observable<Integer> observable = Observable.range(0, 5, Schedulers.io())
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    Log.d(TAG, "range map call: " + integer + " thread name:-->" + Thread.currentThread().getName());
                    return integer;
                })
//                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "onNext: -->integer:" + integer + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("range onNext" + integer);
                }, throwable -> Log.e(TAG, "range onError: ", throwable)
                , () -> Log.d(TAG, "onCompleted: range send completed ."));
        subscriptionMap.put("range", subscription);
    }

    public void repeat() {
        //创建一个发射特定数据重复多次的Observable
        //repeat操作符默认在trampoline调度器上执行。有一个变体可以通过可选参数指定Scheduler。
        Observable<Integer> observableRepeat = Observable
                .just(0, 1, 2)
//                .repeat(3, Schedulers.io())//-重复3次
                .repeatWhen(observable -> {
//                        return observable.delay(2, TimeUnit.SECONDS);//相隔2s无限重复
                    return observable//相隔1s重复3次
                            .zipWith(Observable.range(1, 3), (Func2<Void, Integer, Integer>) (aVoid, integer) -> integer)
                            .flatMap((Func1<Integer, Observable<?>>) integer -> {
                                Log.d(TAG, "repeatWhen call: repeatCount-->" + integer + " thread name:-->" + Thread.currentThread().getName());
                                return Observable.timer(1, TimeUnit.SECONDS);
                            });
                })
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    Log.d(TAG, "repeat map call: " + integer + " thread name:-->" + Thread.currentThread().getName());
                    return integer;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observableRepeat.subscribe(integer -> {
                    Log.d(TAG, "onNext: -->integer:" + integer + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("repeat onNext" + integer);
                }, throwable -> Log.e(TAG, "repeat onError: ", throwable)
                , () -> Log.d(TAG, "onCompleted: repeat send completed ."));
        subscriptionMap.put("repeat", subscription);
    }

    public void start() {
        //返回一个Observable，它发射一个类似于函数声明的值
//        ToastUtil.showToast("需要引入io.reactivex:rxjava-async-util:0.21.0");
        Observable<Integer> observable = Async
//                .start(() -> {//2s 后发射20
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        throw Exceptions.propagate(e);
//                    }
//                    return 20;
//                })
                .toAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    return 20;
                })
                .call()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "onNext: -->integer:" + integer + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("start onNext" + integer);
                }, throwable -> Log.e(TAG, "start onError: ", throwable)
                , () -> Log.d(TAG, "onCompleted: start send completed ."));
        subscriptionMap.put("start", subscription);
    }

    public void timer() {
        //创建一个Observable，它在一个给定的延迟后发射一个特殊的值。
        //timer返回一个Observable，它在延迟一段给定的时间后发射一个简单的数字0。
        //timer操作符默认在computation调度器上执行。有一个变体可以通过可选参数指定Scheduler。
        Observable<Long> observable = Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
                .map(integer -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw Exceptions.propagate(e);
                    }
                    Log.d(TAG, "timer map call: " + integer + " thread name:-->" + Thread.currentThread().getName());
                    return integer;
                })
//                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, "onNext: -->integer:" + integer + " thread name:-->" + Thread.currentThread().getName());
                    ToastUtil.showToast("timer onNext" + integer);
                }, throwable -> Log.e(TAG, "timer onError: ", throwable)
                , () -> Log.d(TAG, "onCompleted: timer send completed ."));
        subscriptionMap.put("timer", subscription);
    }
}
