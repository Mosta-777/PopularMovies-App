package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY="com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME="moviesData";
        public static final String MOVIE_API_ID="api_id";
        public static final String MOVIE_TITLE="title";
        public static final String OVERVIEW="overview";
        public static final String RELEASE_DATE="releaseDate";
        public static final String AVERAGE_VOTE="averageVote";
    }
}
