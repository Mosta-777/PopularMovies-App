package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.android.popularmovies.data.DatabaseContract.MoviesEntry.TABLE_NAME;

public class MoviesDataContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private MoviesDbHelper dbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {

            case MOVIES /* which is the only case*/:
                long id = database.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
