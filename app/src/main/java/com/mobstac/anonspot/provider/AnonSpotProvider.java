package com.mobstac.anonspot.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.mobstac.beaconstac.provider.MSContentProvider;

/**
 * Created by aakash on 13/1/16.
 */
public class AnonSpotProvider extends MSContentProvider {
    public AnonSpotProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return super.getType(uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return super.insert(uri, values);
    }

    @Override
    public boolean onCreate() {
        return super.onCreate();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return super.update(uri, values, selection, selectionArgs);
    }
}
