package com.jax.realmdemo.model;

import io.realm.RealmObject;

/**
 * Created on 2017/3/30.
 */

public class OperationUser extends RealmObject {

    private String name;
    private String headerUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    @Override
    public String toString() {
        return "name : " + name + " headerUrl : " + headerUrl;
    }
}
