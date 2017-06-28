package com.ytxx.contentprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2017/6/27.
 */

public class DBHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String DATABASE_NAME = "jaxTest.db";
    //数据库版本
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Profile.TABLE_NAME + "(" +
                Profile.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Profile.COLUMN_NAME + " VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
