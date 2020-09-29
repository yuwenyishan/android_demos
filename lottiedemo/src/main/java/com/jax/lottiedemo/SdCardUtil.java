package com.jax.lottiedemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;

import java.io.File;

public class SdCardUtil {

    public static File getAvailableFileDir(Context context) {
        if (isSdCardAvailable()) {
            return Environment.getExternalStorageDirectory();
        }
        //目前放在/data/data/包名/files下(分模块可放置不同的位置,自行修改扩充方法)
        return context.getFilesDir();
    }

    /**
     * is sd card available.
     *
     * @return true if available
     */
    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取外部总空间大小
     *
     * @return 总大小，字节为单位
     */
    public static long getTotalExternalSdcardSize() {
        if (isSdCardAvailable()) {
            //获取SDCard根目录
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                return totalBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableExternalSdcardSize() {
        long freeSpace = 0L;
        if (isSdCardAvailable()) {
            try {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long blockSize, availableBlocks;
                availableBlocks = stat.getAvailableBlocksLong();
                blockSize = stat.getBlockSizeLong();
                freeSpace = availableBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return freeSpace;
    }

    /**
     * @return sdcard size
     */
    @SuppressLint("NewApi")
    public static long getSdCardSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = null;
        try {
            stat = new StatFs(path.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stat == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockCountLong = stat.getBlockCountLong();
            long blockSizeLong = stat.getBlockSizeLong();
            return blockCountLong * blockSizeLong;
        }
        long blockCount = stat.getBlockCount();
        long blockSize = stat.getBlockSize();
        return blockCount * blockSize;
    }

    /**
     * @return sdcard size
     */
    @SuppressLint("NewApi")
    public static long getSdCardSize(String path) {
        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stat == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockCountLong = stat.getBlockCountLong();
            long blockSizeLong = stat.getBlockSizeLong();
            return blockCountLong * blockSizeLong;
        }
        long blockCount = stat.getBlockCount();
        long blockSize = stat.getBlockSize();
        return blockCount * blockSize;
    }

    @TargetApi(value = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getFree(String path) {
        long freeSpace = 0;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize, availableBlocks;
            availableBlocks = stat.getAvailableBlocksLong();
            blockSize = stat.getBlockSizeLong();
            freeSpace = availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freeSpace;
    }
}
