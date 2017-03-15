package com.jax.reactivex.fun;

import android.util.Log;

import com.jax.reactivex.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
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
     *
     */
    public void retryWhen() {

    }

    private void logRx(Observable<Integer> observable, String tag) {
        Subscription subscription = observable.subscribe(integer -> {
                    Log.d(TAG, tag + ": " + integer);
                    ToastUtil.showToast(tag + " onNext -> " + integer);
                }, throwable -> Log.e(TAG, tag + " : onError -> ", throwable)
                , () -> Log.d(TAG, tag + " : onComplete -> "));
        subscriptionMap.put(tag, subscription);
    }
}
