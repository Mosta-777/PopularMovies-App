package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com";
    private static final String YOUTUBE_WATCH_PATH = "watch";
    private static final String YOUTUBE_KEY_PARAM = "v";
    private static final String POPULAR_MOVIES_PATH = "popular";
    private static final String TOP_RATED_MOVIES_PATH = "top_rated";
    private static final int POPULAR_MOVIES = 0;
    public static final int TRAILERS = 1;
    public static final int REVIEWS = 202;
    private static final String API_KEY_PARAM = "api_key";
    private static final String TRAILERS_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";
    private static final String PRIVATE_API_KEY = "dd8164c7b1f5100f057622cbb8634282";
    private static String chosenPath;
    private static String TrailersOrReviews;

    public static URL buildURL(int theChosenPath) {
        if (theChosenPath == POPULAR_MOVIES) chosenPath = POPULAR_MOVIES_PATH;
        else {
            chosenPath = TOP_RATED_MOVIES_PATH;
        }
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(chosenPath)
                .appendQueryParameter(API_KEY_PARAM, PRIVATE_API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailersOrReviewsURL(String movieID, int theChosenPath) {
        if (theChosenPath == TRAILERS) TrailersOrReviews = TRAILERS_PATH;
        else {
            TrailersOrReviews = REVIEWS_PATH;
        }
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(movieID)
                .appendPath(TrailersOrReviews)
                .appendQueryParameter(API_KEY_PARAM, PRIVATE_API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Uri getTrailerUri(String trailerKey) {
        return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(YOUTUBE_WATCH_PATH)
                .appendQueryParameter(YOUTUBE_KEY_PARAM, trailerKey).build();
    }
}