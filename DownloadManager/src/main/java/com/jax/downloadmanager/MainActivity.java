package com.jax.downloadmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Downloader.Listener {
    public static final String TEST_DOWNLOAD_URL = "http://www.cbu.edu.zm/downloads/pdf-sample.pdf";

    private Button btn_download;

    private Downloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_download = (Button) findViewById(R.id.downloadButton);
        btn_download.setOnClickListener(this);
        downloader = Downloader.newInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.downloadButton:
                downloadOrCancel();
                break;
        }
    }

    private void downloadOrCancel() {
        if (downloader.isDownloading()) {
            cancel();
        } else {
            download();
        }
        updateUi();
    }

    private void cancel() {
        downloader.cancel();
    }

    private void download() {
        Uri uri = Uri.parse(TEST_DOWNLOAD_URL);
        downloader.download(uri);
    }

    private void updateUi() {
        if (downloader.isDownloading()) {
            btn_download.setText("cancel download .");
        } else {
            btn_download.setText("start download .");
        }
    }

    private static final String TAG = "MainActivity";

    @Override
    public void fileDownloaded(Uri uri, String mimeType) {
        Log.d(TAG, "fileDownloaded: " + uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//android 7.0 以后需要添加临时权限flag.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "not exists App to Open.", Toast.LENGTH_SHORT).show();
        }
        updateUi();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        downloader.unregister();
        super.onDestroy();
    }
}
