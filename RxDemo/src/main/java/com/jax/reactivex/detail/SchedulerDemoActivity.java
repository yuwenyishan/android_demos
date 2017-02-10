package com.jax.reactivex.detail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.jax.reactivex.R;
import com.jax.reactivex.RootActivity;
import com.jax.reactivex.fun.SchedulerDemo;
import com.jax.reactivex.util.ToastUtil;

/**
 * Created on 2017/2/10.
 */

public class SchedulerDemoActivity extends RootActivity {

    private SchedulerDemo schedulerDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_demo);
        schedulerDemo = new SchedulerDemo();
        schedulerDemo.setHandler(handler);
    }

    private Handler handler = new Handler(message -> {
        switch (message.what) {
            case SchedulerDemo.NewThread:
            case SchedulerDemo.Computation:
            case SchedulerDemo.FormExecutor:
            case SchedulerDemo.Immediate:
            case SchedulerDemo.Io:
            case SchedulerDemo.Trampoline:
                String log = (String) message.obj;
                ToastUtil.showToast(log);
                return true;
            default:
                return false;
        }
    });

    public void newThread(View view) {
        schedulerDemo.newThreadScheduler();
    }

    public void computation(View view) {
        schedulerDemo.computationScheduler();
    }

    public void formExecutor(View view) {
        schedulerDemo.formExecutorScheduler();
    }

    public void changeExecutor(View view) {
        schedulerDemo.changeExecutor();
    }

    public void immediate(View view) {
        schedulerDemo.immediateScheduler();
    }

    public void io(View view) {
        schedulerDemo.ioScheduler();
    }

    public void trampoline(View view) {
        schedulerDemo.trampolineScheduler();
    }

    public void delay(View view) {
        schedulerDemo.delayScheduler();
    }

    public void periodically(View view) {
        schedulerDemo.periodicallyScheduler();
    }
}
