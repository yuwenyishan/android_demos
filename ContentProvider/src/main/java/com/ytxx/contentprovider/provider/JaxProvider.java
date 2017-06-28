package com.ytxx.contentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ytxx.contentprovider.db.DBHelper;
import com.ytxx.contentprovider.db.Profile;

/**
 * Created on 2017/6/27.
 */

public class JaxProvider extends ContentProvider {

    DBHelper dbHelper;
    SQLiteDatabase database;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Profile.AUTHORITIES, Profile.TABLE_NAME, Profile.ITEM);
        uriMatcher.addURI(Profile.AUTHORITIES, Profile.TABLE_NAME + "/#", Profile.ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        database = dbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case Profile.ITEM:
                cursor = database.query(Profile.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case Profile.ITEM_ID:
                cursor = database.query(Profile.TABLE_NAME, strings, Profile.COLUMN_ID + "=" + uri.getLastPathSegment(), strings1, null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Profile.ITEM:
                return Profile.CONTENT_TYPE;
            case Profile.ITEM_ID:
                return Profile.CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowId;
        if (uriMatcher.match(uri) != Profile.ITEM) {
            throw new IllegalArgumentException("Not Items Uri -> " + uri);
        }
        rowId = database.insert(Profile.TABLE_NAME, null, contentValues);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Profile.CONNECT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (uriMatcher.match(uri) != Profile.ITEM_ID) {
            throw new IllegalArgumentException("Can`t delete items. uri error :" + uri);
        }
        int id = database.delete(Profile.TABLE_NAME, Profile.COLUMN_ID + "=" + uri.getLastPathSegment(), strings);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (uriMatcher.match(uri) != Profile.ITEM_ID) {
            throw new IllegalArgumentException("Can`t delete items. uri error :" + uri);
        }
        int id = database.update(Profile.TABLE_NAME, contentValues, Profile.COLUMN_ID + "=" + uri.getLastPathSegment(), strings);
        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return id;
    }
}
