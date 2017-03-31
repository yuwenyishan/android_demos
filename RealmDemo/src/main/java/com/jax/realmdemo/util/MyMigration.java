package com.jax.realmdemo.util;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created on 2017/3/31.
 */

public class MyMigration implements RealmMigration {
    private static final String TAG = "MyMigration";

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Log.d(TAG, "migrate() called with: oldVersion = [" + oldVersion + "], newVersion = [" + newVersion + "]");
        RealmSchema schema = realm.getSchema();
        if (newVersion == 2) {
            RealmObjectSchema operationUser = schema.get("OperationUser");
            boolean isHasAge = operationUser.hasField("age");
            if (isHasAge) {
                if (operationUser.isNullable("age"))
                    operationUser.setNullable("age", false);
            } else {
                operationUser.addField("age", Integer.class).setNullable("age", false);
            }
        }

        if (oldVersion == 2) {

        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MyMigration;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
