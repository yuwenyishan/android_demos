package com.jax.lottiedemo;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class InternalMemoryUtil {

    /**
     * 获取手机内部空间总大小
     *
     * @return 大小，字节为单位
     */
    public static long getTotalInternalMemorySize() {
        try {
            //获取内部存储根目录
            File path = Environment.getDataDirectory();
            //系统的空间描述类
            StatFs stat = new StatFs(path.getPath());
            //每个区块占字节数
            long blockSize = stat.getBlockSize();
            //区块总数
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return 大小，字节为单位
     */
    public static long getAvailableInternalMemorySize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            //获取可用区块数量
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
