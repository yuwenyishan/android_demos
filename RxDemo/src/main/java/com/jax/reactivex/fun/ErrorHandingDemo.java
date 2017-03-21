package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/3/15.
 */

public class ErrorHandingDemo extends DemoManage {

    private static final String TAG = "ErrorHandingDemo";

    /*
     * Part 1
     * Catch操作符拦截原始Observable的onError通知，将它替换为其它的数据项或数据序列，
     * 让产生的Observable能够正常终止或者根本不终止。
     */

    /**
     * 让Observable遇到错误时发射一个特殊的项并且正常终止。
     */
    public void onErrorReturn() {
        List<String> tex = new ArrayList<>();
        tex.add("1");
        tex.add("2");
        tex.add("3");
        tex.add("4");
        tex.add("q");
        tex.add("6");
        tex.add("w");
        tex.add("7");
        Observable<Integer> observable = Observable.from(tex)
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .onErrorReturn(throwable -> -1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "onErrorReturn");
    }

    /**
     * 让Observable在遇到错误时开始发射第二个Observable的数据序列。
     */
    public void onErrorResumeNext() {
        List<String> tex = new ArrayList<>();
        tex.add("1");
        tex.add("2");
        tex.add("3");
        tex.add("4");
        tex.add("q");
        tex.add("6");
        tex.add("w");
        tex.add("7");
        List<String> tex2 = new ArrayList<>();
        tex2.add("11");
        tex2.add("12");
        tex2.add("13");
        tex2.add("14");
        tex2.add("1q");
        tex2.add("16");
        tex2.add("w");
        tex2.add("7");
        Observable<Integer> observable2 = Observable.from(tex2)
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).onErrorReturn(throwable -> -1);
        Observable<Integer> observable = Observable.from(tex)
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .onErrorResumeNext(observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "onErrorResumeNext");
    }

    /**
     * 让Observable在遇到错误时继续发射后面的数据项。
     */
    public void onExceptionResumeNext() {
        List<String> tex = new ArrayList<>();
        tex.add("1");
        tex.add("2");
        tex.add("3");
        tex.add("4");
        tex.add("q");
        tex.add("6");
        tex.add("w");
        tex.add("7");
        List<String> tex2 = new ArrayList<>();
        tex2.add("11");
        tex2.add("12");
        tex2.add("13");
        tex2.add("14");
        tex2.add("1q");
        tex2.add("16");
        tex2.add("w");
        tex2.add("7");
        Observable<Integer> observable2 = Observable.from(tex2)
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                        return Integer.valueOf(s);
                    } catch (Exception e) {
                        throw Exceptions.propagate(e);
                    }
                }).onErrorReturn(throwable -> -1);
        Observable<Integer> observable = Observable.from(tex)
                .map(s -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Integer.valueOf(s);

                })
                .onExceptionResumeNext(observable2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "onExceptionResumeNext");
    }

    /*
     * Part 2
     * Retry 如果原始Observable遇到错误，重新订阅它期望它能正常终止
     * Retry操作符不会将原始Observable的onError通知传递给观察者，它会订阅这个Observable，
     * 再给它一次机会无错误地完成它的数据序列。Retry总是传递onNext通知给观察者，由于重新订阅，可能会造成数据项重复
     * RxJava中的实现为retry和retryWhen。
     */

    /**
     * 无论收到多少次onError通知，无参数版本的retry都会继续订阅并发射原始Observable。
     * <p>
     * 接受单个count参数的retry会最多重新订阅指定的次数，如果次数超了，它不会尝试再次订阅，
     * 它会把最新的一个onError通知传递给它的观察者。
     * <p>
     * 还有一个版本的retry接受一个谓词函数作为参数，这个函数的两个参数是：重试次数和导致发射onError通知的Throwable。
     * 这个函数返回一个布尔值，如果返回true，retry应该再次订阅和镜像原始的Observable，如果返回false，
     * retry会将最新的一个onError通知传递给它的观察者。
     * <p>
     * retry操作符默认在trampoline调度器上执行。
     */
    public void retry() {
        Observable<Integer> observable = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    Log.d(TAG, "retry: send error. ");
                    subscriber.onNext(123);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onError(new Throwable("send error. "));
                })
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observable, "retry");
    }

    /**
     * retryWhen将onError中的Throwable传递给一个函数，这个函数产生另一个Observable，
     * retryWhen观察它的结果再决定是不是要重新订阅原始的Observable。
     * 如果这个Observable发射了一项数据，它就重新订阅，
     * 如果这个Observable发射的是onError通知，它就将这个通知传递给观察者然后终止。
     * <p>
     * retryWhen操作符默认在trampoline调度器上执行。
     */
    public void retryWhen() {
        Observable<Integer> observablet = Observable
                .create((Observable.OnSubscribe<Integer>) subscriber -> {
                    Log.d(TAG, "retryWhen: send error. ");
                    subscriber.onNext(123);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onError(new Throwable("这是一个错误"));
                })
                .retryWhen(observable -> observable
                        .zipWith(Observable.range(1, 3), (Func2<Throwable, Integer, Integer>) (throwable, integer) -> {
                            Log.d(TAG, "call: " + integer + "次重试");
                            return integer;
                        })
                        .flatMap((Func1<Integer, Observable<?>>) s -> Observable.timer(s, TimeUnit.SECONDS)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        logRx(observablet, "retryWhen");
    }

}
