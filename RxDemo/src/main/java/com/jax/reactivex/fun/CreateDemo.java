package com.jax.reactivex.fun;

import android.os.Handler;
import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/10.
 */

public class CreateDemo {
    private static final String TAG = "CreateDemo";

    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void create() {
        //https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Create.html
        //create方法默认不在任何特定的调度器上执行。默认在main线程
        Observable
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
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
    }

    private int testInteger = 12;

    public void defer() {
        //直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
        //defer方法默认不在任何特定的调度器上执行。
        Observable<Integer> observable = Observable.defer(() -> Observable.just(testInteger));
        testInteger = 15;
        observable.subscribe(integer -> {
            Log.d(TAG, "call: defer:-->testInteger:" + integer + " thread name:-->" + Thread.currentThread().getName());
            ToastUtil.showToast("onNext:" + integer);
        });
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
}
