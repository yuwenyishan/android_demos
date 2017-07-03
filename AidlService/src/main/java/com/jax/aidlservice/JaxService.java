package com.jax.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class JaxService extends Service {
    JaxInfo jaxInfo;
    RemoteCallbackList<IChangeCallback> callbackList = new RemoteCallbackList<>();

    IJaxService.Stub mBinder = new IJaxService.Stub() {

        @Override
        public void aidlTestFun() throws RemoteException {
            System.out.println("客户端通过AIDL与远程后台成功通信");
        }

        @Override
        public JaxInfo getJaxInfo() throws RemoteException {
            return jaxInfo;
        }

        @Override
        public void registerChangeListener(IChangeCallback callback) throws RemoteException {
            if (callbackList != null) {
                System.out.println("客户端注册changeListener");
                callbackList.register(callback);
            }
        }

        @Override
        public void unRegisterChangeListener(IChangeCallback callback) throws RemoteException {
            if (callbackList != null) {
                System.out.println("客户端取消注册changeListener");
                callbackList.unregister(callback);
            }
        }

        @Override
        public void changeInfo() throws RemoteException {
            System.out.println("客户端手动触发数据更改");
            changeJaxInfo();
        }

        @Override
        public void startTimeChange() throws RemoteException {
            System.out.println("开启定时改变任务");
            if (!threadStared) {
                threadStared = true;
                timeChange();
            } else {
                System.out.println("定时改变任务已开启，不能重复开启哦");
            }
        }

        @Override
        public void stopTimeChange() throws RemoteException {
            System.out.println("结束定时改变任务");
            if (threadStared) {
                threadStared = false;
            } else {
                System.out.println("定时改变任务未开启，不能结束");
            }
        }
    };

    private void changeJaxInfo() {
        final int clients = callbackList.beginBroadcast();
        for (int i = 0; i < clients; i++) {
            IChangeCallback changeCallback = callbackList.getBroadcastItem(i);
            if (changeCallback != null) {
                jaxInfo.setAge(0);
                jaxInfo.setInfo("手动修改了0");
                try {
                    changeCallback.notificationChanged(jaxInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                callbackList.finishBroadcast();
            }
        }
    }

    private void changeJaxInfo(JaxInfo jaxInfo) {
        final int clients = callbackList.beginBroadcast();
        for (int i = 0; i < clients; i++) {
            IChangeCallback changeCallback = callbackList.getBroadcastItem(i);
            if (changeCallback != null) {
                try {
                    changeCallback.notificationChanged(jaxInfo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                callbackList.finishBroadcast();
            }
        }
    }

    Thread thread;
    private boolean threadStared = false;

    private void timeChange() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadStared) {
                    try {
                        Thread.sleep(3000);
                        synchronized (this) {
                            jaxInfo.setAge(jaxInfo.getAge() + 1);
                            jaxInfo.setInfo("jax 的年龄又增长了" + jaxInfo.getAge());
                            changeJaxInfo(jaxInfo);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("JaxService -> onCreate.");
        jaxInfo = new JaxInfo("jax", 24, "男", "程序员一个。");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("JaxService -> onStartCommand.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("JaxService -> onBind.");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("JaxService -> onUnbind.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        System.out.println("JaxService -> onDestroy.");
        super.onDestroy();
    }
}
