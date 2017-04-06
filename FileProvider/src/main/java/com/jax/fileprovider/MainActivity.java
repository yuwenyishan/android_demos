package com.jax.fileprovider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    String fileName = "providerTest.png";
    private Uri uri;
    private ArrayList<File> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        findViewById(R.id.take_photo_1).setOnClickListener(this);
        findViewById(R.id.take_photo_2).setOnClickListener(this);
        findViewById(R.id.take_photo_3).setOnClickListener(this);
    }

    private void createFolders() {
        File folder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/x/d/");
        File folder2 = new File(getExternalCacheDir(), "/x/d/");
        File folder3 = new File(Environment.getExternalStorageDirectory(), "/Pictures/providerTest/");
        folders.add(folder);
        folders.add(folder2);
        folders.add(folder3);
        for (File file : folders) {
            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.d(TAG, "createFolders: " + file.getAbsolutePath() + " create success .");
                } else {
                    Log.e(TAG, "createFolders: " + file.getAbsolutePath() + " create fail.");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_1:
                takePhoto1();
                break;
            case R.id.take_photo_2:
                takePhoto2(folders.get(1));
                break;
            case R.id.take_photo_3:
                takePhoto2(folders.get(2));
                break;
        }
    }

    /**
     * it will be ok below 23
     */
    private void takePhoto1() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file = new File(folders.get(0), fileName);
            uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            try {
                startActivityForResult(intent, 0);
            } catch (Exception e) {
                Toast.makeText(this, "Error in android SDK -> " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePhoto2(File folder) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file = new File(folder, fileName);
            uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // not work in android 4.2/4.3 ,may be all not work below android 6.0, so,that is ok.
                // -> http://stackoverflow.com/questions/33650632/fileprovider-not-working-with-camera
                //---------------------↓
                List<ResolveInfo> resInfoList =
                        getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                //---------------------↑
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + resultCode);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: takePhoto1 -> " + uri.toString());
            openFile(uri);
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: takePhoto2 -> " + uri.toString());
            openFile(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        checkPermission();
                        break;
                    }
                    if (i == grantResults.length - 1) {
                        createFolders();
                    }
                }
            }
        }
    }

    private void checkPermission() {
        String[] ps = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        ArrayList<String> deniedPm = new ArrayList<>();
        for (String s : ps) {
            if (ContextCompat.checkSelfPermission(this, s)
                    != PackageManager.PERMISSION_GRANTED) {
                deniedPm.add(s);
            }
        }
        if (deniedPm.size() > 0) {
            String[] rqPm = new String[deniedPm.size()];
            for (int i = 0; i < deniedPm.size(); i++) {
                rqPm[i] = deniedPm.get(i);
            }
            ActivityCompat.requestPermissions(this, rqPm, 2);
        } else {
            createFolders();
        }
    }

    private void openFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                List<ResolveInfo> resolveInfos
                        = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo info : resolveInfos) {
                    String packName = info.activityInfo.packageName;
                    Log.d(TAG, "openFile: " + packName);
                    grantUriPermission(packName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
            startActivity(intent);
        }
    }
}
