package com.ytxx.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jax.aidlservice.IChangeCallback;
import com.jax.aidlservice.IJaxService;
import com.jax.aidlservice.JaxInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private IJaxService iJaxService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bind).setOnClickListener(this);
        findViewById(R.id.unbind).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
        findViewById(R.id.startTime).setOnClickListener(this);
        findViewById(R.id.stopTime).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind:
                System.out.println("bind");
                //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
                //参数与服务器端的action要一致,即"服务器包名.aidl接口文件名"
                Intent intent = new Intent("com.jax.aidlservice.IJaxService");

                //Android5.0后无法只通过隐式Intent绑定远程Service
                //需要通过setPackage()方法指定包名
                intent.setPackage("com.jax.aidlservice");

                //绑定服务,传入intent和ServiceConnection对象
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                System.out.println("unbind");
                try {
                    iJaxService.unRegisterChangeListener(changeCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                unbindService(connection);
                System.out.println("解绑成功");
                break;
            case R.id.change:
                try {
                    iJaxService.changeInfo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.startTime:
                try {
                    iJaxService.startTimeChange();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stopTime:
                try {
                    iJaxService.stopTimeChange();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iJaxService = IJaxService.Stub.asInterface(iBinder);

            try {
                iJaxService.aidlTestFun();
                JaxInfo jaxInfo = iJaxService.getJaxInfo();
                System.out.println(jaxInfo.toString());
                iJaxService.registerChangeListener(changeCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IChangeCallback changeCallback = new IChangeCallback.Stub() {

        @Override
        public void notificationChanged(JaxInfo jaxInfo) throws RemoteException {
            System.out.println("收到jaxInfo更改通知，新的信息如下:\n" + jaxInfo.toString());
        }
    };

}
