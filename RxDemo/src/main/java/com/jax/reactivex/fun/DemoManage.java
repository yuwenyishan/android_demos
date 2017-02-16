package com.jax.reactivex.fun;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created on 2017/2/15.
 */

public class DemoManage {

    public HashMap<String, Subscription> requestList = new HashMap<>();

    public void onDestroy() {
        for (Map.Entry<String, Subscription> stringSubscriptionEntry : requestList.entrySet()) {
            Subscription s = stringSubscriptionEntry.getValue();
            if (!s.isUnsubscribed()) {
                s.unsubscribe();
            }
        }
    }
}
