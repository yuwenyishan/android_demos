package com.jax.downloadmanager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created on 2017/4/5.
 * <p>
 * Register or unregister downloadManger download complete broadcast .
 */

public class DownloadReceiver extends BroadcastReceiver {

    private final Listener listener;

    public DownloadReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        listener.downloadComplete(downloadId);
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public interface Listener {

        void downloadComplete(long downloadId);

    }
}
