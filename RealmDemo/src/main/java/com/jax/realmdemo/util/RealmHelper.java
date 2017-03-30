package com.jax.realmdemo.util;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.jax.realmdemo.BuildConfig;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created on 2017/3/30.
 */

public class RealmHelper {

    public static void initRealm(Context context) {

        Realm.init(context);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("realmDemo.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(context)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(context).build())
                            .build());
        }
    }
}
