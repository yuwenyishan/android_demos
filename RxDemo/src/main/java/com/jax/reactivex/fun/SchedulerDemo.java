package com.jax.reactivex.fun;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/2/10.
 */

public class SchedulerDemo {
    private static final String TAG = "SchedulerDemo";
    public static final int NewThread = 0x01;
    public static final int Computation = 0x02;
    public static final int FormExecutor = 0x03;
    public static final int Immediate = 0x04;
    public static final int Io = 0x05;
    public static final int Trampoline = 0x06;

    private Handler handler;
    private Executor single = Executors.newSingleThreadExecutor();
    private Executor cache = Executors.newCachedThreadPool();
    private Executor fixed = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Executor scheduler = Executors.newScheduledThreadPool(3);
    private Executor[] executors = new Executor[]{single, cache, fixed, scheduler};

    private Executor current;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void newThreadScheduler() {
        //为每个任务创建一个新线程
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(() -> {
            String log = "I am Schedulers.newThread().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, NewThread);
            worker.unsubscribe();
        });
    }

    public void computationScheduler() {
        //用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
        Scheduler.Worker worker = Schedulers.computation().createWorker();
        worker.schedule(() -> {
            String log = "I am Schedulers.computation().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, Computation);
            worker.unsubscribe();
        });
    }

    public void changeExecutor() {
        //随机更换Executor
        int random = (int) (Math.random() * 4);
        Log.d(TAG, "changeExecutor: random" + random);
        try {
            current = executors[random];
        } catch (Exception e) {
            current = single;
        }
    }

    public void formExecutorScheduler() {
        //使用指定的Executor作为调度器
        if (current == null) {
            changeExecutor();
        }
        Scheduler.Worker worker = Schedulers.from(current).createWorker();
        worker.schedule(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String log = "I am Schedulers.form(Executor).-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, FormExecutor);
            worker.unsubscribe();
        });
    }

    public void immediateScheduler() {
        //在当前线程立即开始执行任务
        Scheduler.Worker worker = Schedulers.immediate().createWorker();
        worker.schedule(() -> {
            String log = "I am Schedulers.immediate().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, Immediate);
            worker.unsubscribe();
        });
    }

    public void ioScheduler() {
        //用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；对于普通的计算任务，
        // 请使用Schedulers.computation()；Schedulers.io( )默认是一个CachedThreadScheduler，
        // 很像一个有线程缓存的新线程调度器
        Scheduler.Worker worker = Schedulers.io().createWorker();
        worker.schedule(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String log = "I am Schedulers.io().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, Io);
            worker.unsubscribe();
        });
    }

    public void trampolineScheduler() {
        //当其它排队的任务完成后，在当前线程排队开始执行
        Scheduler.Worker worker = Schedulers.trampoline().createWorker();
        worker.schedule(() -> {
            String log = "I am Schedulers.trampoline().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, Trampoline);
            if (!worker.isUnsubscribed()) {
                worker.unsubscribe();
            }
        });
    }

    public void delayScheduler() {
        //你可以使用schedule(action,delayTime,timeUnit)在指定的调度器上延时执行你的任务
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(() -> {
            String log = "I am Schedulers.immediate().-->" + Thread.currentThread().getName();
            Log.d(TAG, "call: " + log);
            toastLog(log, NewThread);
            worker.unsubscribe();
        }, 2, TimeUnit.SECONDS);
    }

    private int x = 0;

    public void periodicallyScheduler() {
        if (x != 0) {
            return;
        }
        x = 0;
        //使用另一个版本的schedule，
        // schedulePeriodically(action,initialDelay,period,timeUnit)方法让你可以安排一个定期执行的任务，
        // 下面例子的任务将在2秒之后执行，然后每2秒执行一次：
        Scheduler.Worker worker = Schedulers.from(single).createWorker();
        worker.schedulePeriodically(() -> {
            String log = "I am Schedulers.immediate().-->" + Thread.currentThread().getName() + "-num-:" + x;
            Log.d(TAG, "call: " + log);
            toastLog(log, FormExecutor);
            x += 1;
            if (x > 5 && !worker.isUnsubscribed()) {
                x = 0;
                worker.unsubscribe();
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    private void toastLog(CharSequence charSequence, int messageWhat) {
        if (handler != null) {
            handler.obtainMessage(messageWhat, charSequence).sendToTarget();
        }
    }
}
