package com.jax.reactivex.fun;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/3/21.
 */

public class AuxiliaryOperationDemo extends DemoManage {

    /**
     * 延迟一段指定的时间再发射来自Observable的发射物
     */
    public void delay() {
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
    }
}
