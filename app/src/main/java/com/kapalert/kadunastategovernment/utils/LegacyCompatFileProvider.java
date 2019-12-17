package com.kapalert.kadunastategovernment.utils;

import android.database.Cursor;
import android.net.Uri;

import androidx.core.content.FileProvider;


public class LegacyCompatFileProvider extends FileProvider {
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//    return(new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }
}