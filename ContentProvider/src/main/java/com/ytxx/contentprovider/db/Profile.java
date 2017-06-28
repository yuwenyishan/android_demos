package com.ytxx.contentprovider.db;

import android.net.Uri;

import com.ytxx.contentprovider.BuildConfig;

/**
 * Created on 2017/6/27.
 */

public class Profile {

    //定义表名
    public static final String TABLE_NAME = "jaxTestTable";
    //定义字段名主键↓
    public static final String COLUMN_ID = "_id";
    //定义字段名name
    public static final String COLUMN_NAME = "name";
    //这个要相同与manifest中得provide的authorities
    public static final String AUTHORITIES = BuildConfig.APPLICATION_ID + ".JaxProvider";

    public static final Uri CONNECT_URI = Uri.parse("content://" + AUTHORITIES + "/" + TABLE_NAME);

    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/test";
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.dir/test";
}
