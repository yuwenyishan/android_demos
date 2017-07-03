// IChangeCallback.aidl
package com.jax.aidlservice;

// Declare any non-default types here with import statements
import com.jax.aidlservice.JaxInfo;

interface IChangeCallback {

   void notificationChanged(inout JaxInfo jaxInfo);

}
