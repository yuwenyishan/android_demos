package com.jax.reactivex.fun;

import android.util.Log;
import android.widget.TextView;

import com.jax.reactivex.util.ToastUtil;

import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created on 2017/2/9.
 */

public class SubjectDemo {
    private static final String TAG = "SubjectDemo";
    //http://www.jianshu.com/p/1257c8ba7c0c
    //https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html
    //值得注意的是一定要用Subcect.create()的方式创建并使用，不要用just(T)、from(T)、create(T)创建，否则会导致失效...
    String toast = "";
    private TextView textView;

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void asyncSubject() {
        /**
         * 一个AsyncSubject只在原始Observable完成后，发射来自原始Observable的最后一个值。
         * （如果原始Observable没有发射任何值，AsyncObject也不发射任何值）它会把这最后一个值发射给任何后续的观察者。
         * 然而，如果原始的Observable因为发生了错误而终止，AsyncSubject将不会发射任何数据，只是简单的向前传递这个错误通知。
         */
        toast = "";
        AsyncSubject<String> subject = AsyncSubject.create();
        subject.onNext("1");
        subject.onNext("2");
        subject.onNext("3");
        result(subject);
        subject.onNext("4");
        subject.onNext("5");
        subject.onCompleted();
    }

    public void behaviorSubject() {
        /**
         * BehaviorSubject会发送离订阅最近的上一个值，没有上一个值的时候会发送默认值。
         */
        toast = "";
        BehaviorSubject<String> subject = BehaviorSubject.create();
        subject.onNext("1");
        subject.onNext("2");
        result(subject);
        subject.onNext("3");
        subject.onNext("4");
        subject.onNext("5");
        subject.onCompleted();
    }

    public void publishSubject() {
        /**
         * 从那里订阅就从那里开始发送数据
         */
        toast = "";
        PublishSubject<String> subject = PublishSubject.create();
        subject.onNext("1");
        subject.onNext("2");
        subject.onNext("3");
        result(subject);
        subject.onNext("4");
        subject.onNext("5");
        subject.onCompleted();
    }

    public void replaySubject() {
        /**
         * 无论何时订阅，都会将所有历史订阅内容全部发出
         */
        toast = "";
        ReplaySubject<String> subject = ReplaySubject.create();
        subject.onNext("1");
        subject.onNext("2");
        subject.onNext("3");
        subject.onNext("4");
        subject.onNext("5");
        subject.onCompleted();
        result(subject);
    }

    private void result(Subject<String, String> subject) {
        subject.subscribe(s -> {
                    toast += s + "、";
                    Log.d(TAG, "behaviorSubject: s:" + s);
                    if (textView != null) {
                        textView.setText(textView.getText() + "\n" +
                                subject.getClass().getSimpleName() + ":" + toast.substring(0, toast.length() - 1));
                    }
                }, throwable -> ToastUtil.showToast(throwable.getMessage())
                , () -> {
                    toast = toast.substring(0, toast.length() - 1);
                    ToastUtil.showToast(toast);
                });
    }
}
