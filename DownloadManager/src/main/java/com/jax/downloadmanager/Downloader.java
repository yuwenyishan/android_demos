package com.jax.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created on 2017/4/5.
 */

public class Downloader implements DownloadReceiver.Listener {

    private final DownloadManager downloadManager;
    private final Listener listener;

    private DownloadReceiver receiver = null;
    private long downloadId = -1;

    public static Downloader newInstance(Listener listener) {
        DownloadManager downloadManager = (DownloadManager) listener.getContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        return new Downloader(downloadManager, listener);
    }

    public Downloader(DownloadManager downloadManager, Listener listener) {
        this.downloadManager = downloadManager;
        this.listener = listener;
    }

    public void download(Uri uri) {
        if (uri == null) {
            Toast.makeText(listener.getContext(), "Uri Error. Can`t be null. ", Toast.LENGTH_SHORT).show();
            return;
        }
        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
            Toast.makeText(listener.getContext(), "Can only download HTTP/HTTPS URIs: " + uri, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isDownloading()) {
            register();
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("TestDownload.pdf");
            request.setDescription("I am download description. ");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadId = downloadManager.enqueue(request);
        }
    }

    public boolean isDownloading() {
        return downloadId >= 0;
    }

    private void register() {
        if (receiver == null && !isDownloading()) {
            receiver = new DownloadReceiver(this);
            receiver.register(listener.getContext());
        }
    }

    public void unregister() {
        if (receiver != null) {
            receiver.unregister(listener.getContext());
        }
        receiver = null;
    }

    @Override
    public void downloadComplete(long downloadId) {
        if (this.downloadId == downloadId) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(this.downloadId);
            this.downloadId = -1;
            unregister();
            Cursor cursor = downloadManager.query(query);
            while (cursor.moveToNext()) {
                getFileInfo(cursor);
            }
            cursor.close();
        }
    }

    private void getFileInfo(Cursor cursor) {
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            Uri uri = downloadManager.getUriForDownloadedFile(id);
            String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            listener.fileDownloaded(uri, mimeType);
        }
    }

    public void cancel() {
        if (isDownloading()) {
            downloadManager.remove(downloadId);
            downloadId = -1;
            unregister();
        }
    }

    public interface Listener {

        void fileDownloaded(Uri uri, String mimeType);

        Context getContext();
    }
}
