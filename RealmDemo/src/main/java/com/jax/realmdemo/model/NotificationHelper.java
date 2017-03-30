package com.jax.realmdemo.model;

import android.util.Log;

import com.jax.realmdemo.bean.NotificationType;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created on 2017/3/30.
 */

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";

    private volatile static NotificationHelper instance;

    private Realm realm;

    public static NotificationHelper getInstance() {
        if (instance == null) {
            synchronized (NotificationHelper.class) {
                if (instance == null) {
                    instance = new NotificationHelper();
                }
            }
        }
        return instance;
    }

    public void getRealm() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    public void addOrUpdateNotification(final TestModel testModel, Realm.Transaction.OnSuccess onSuccess,
                                         Realm.Transaction.OnError onError) {
        @NotificationType.Type final String type = getType(testModel.getType());
        final String primary_id = testModel.getId() + type;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Notification> results = realm.where(Notification.class)
                        .equalTo("primaryId", primary_id).findAll();
                if (results.size() > 0) {
                    Notification notification = results.get(0);
                    OperationUser user = new OperationUser();
                    user.setName(testModel.getName());
                    user.setHeaderUrl(testModel.getHeader());
                    notification.addOperator(user);
                    notification.setTime(new Date().getTime());
                    realm.copyToRealmOrUpdate(notification);
                } else {
                    Notification notification = realm.createObject(Notification.class, primary_id);
                    notification.setTime(new Date().getTime());
                    notification.setId(testModel.getId());
                    notification.setType(type);
                    OperationUser user = new OperationUser();
                    user.setName(testModel.getName());
                    user.setHeaderUrl(testModel.getHeader());
                    notification.addOperator(user);
                }
            }
        }, onSuccess, onError);
    }

    public void logAll() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Notification> results = realm.where(Notification.class).findAll();
                for (Notification n : results) {
                    Log.d(TAG, "execute: " + n.toString());
                }
            }
        });
    }

    private String getType(String type) {
        switch (type) {
            case "type1":
                return NotificationType.TYPE_D_L;
            case "type2":
                return NotificationType.TYPE_D_C;
            case "type3":
                return NotificationType.TYPE_D_C_C;
            case "type4":
                return NotificationType.TYPE_A_L;
            case "type5":
                return NotificationType.TYPE_A_C;
            case "type6":
                return NotificationType.TYPE_A_C_C;
            case "type7":
                return NotificationType.TYPE_A_R;
            default:
                return NotificationType.TYPE_D_L;
        }
    }
}
