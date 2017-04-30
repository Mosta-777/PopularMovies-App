package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 26/03/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final String DATA_BASE_NAME="MoviesDataBase2";
    private static final int DATA_BASE_VERSION=1;
    public MoviesDbHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "  + DatabaseContract.MoviesEntry.TABLE_NAME + " (" +
                DatabaseContract.MoviesEntry._ID         + " INTEGER PRIMARY KEY, " +
                DatabaseContract.MoviesEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                DatabaseContract.MoviesEntry.MOVIE_API_ID + " INTEGER NOT NULL, " +
                DatabaseContract.MoviesEntry.OVERVIEW + " TEXT NOT NULL, " +
                DatabaseContract.MoviesEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                DatabaseContract.MoviesEntry.AVERAGE_VOTE + " TEXT NOT NULL);" ;
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
