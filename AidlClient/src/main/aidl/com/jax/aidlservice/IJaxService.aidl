// IJaxService.aidl
package com.jax.aidlservice;

// Declare any non-default types here with import statements
import com.jax.aidlservice.JaxInfo;
import com.jax.aidlservice.IChangeCallback;

interface IJaxService {

    void aidlTestFun();

    JaxInfo getJaxInfo();

    void registerChangeListener(IChangeCallback callback);

    void unRegisterChangeListener(IChangeCallback callback);

    void changeInfo();

    void startTimeChange();

    void stopTimeChange();
}
