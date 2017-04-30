package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.Movies;
import com.example.android.popularmovies.Review;
import com.example.android.popularmovies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MoviesDataJasonUtils {
    private static final String MOVIE_RESULTS_KEY="results";
    private static final String ORIGINAL_TITLE_KEY="original_title";
    private static final String POSTER_PATH_KEY="poster_path";
    private static final String PLOT_SYNOPSIS_KEY="overview";
    private static final String USER_RATING_KEY="vote_average";
    private static final String RELEASE_DATE_KEY="release_date";
    private static final String MOVIE_ID_KEY="id";
    private static final String MOVIE_COVER_PHOTO_KEY="backdrop_path";
    private static final String TRAILER_NAME_KEY="name";
    private static final String TRAILER_KEY="key";
    private static final String REVIEW_AUTHOR_KEY="author";
    private static final String REVIEW_CONTENT_KEY="content";
    private static final String TOTAL_REVIEW_RESULTS="total_results";
    public static boolean noReviews=false;
    public static boolean noTrailers=false;

    public static ArrayList<Movies> getSimpleMoviesDataFromJson(Context context, String moviesDataJsonStr)
            throws JSONException {
        JSONObject moviesDataJsonObject =new JSONObject(moviesDataJsonStr);
        JSONArray movieResultsArray =moviesDataJsonObject.getJSONArray(MOVIE_RESULTS_KEY);
        ArrayList<Movies> moviesDataArray=new ArrayList<Movies>();
        for (int i=0;i<movieResultsArray.length();i++)
        {
            JSONObject currentMovie=movieResultsArray.getJSONObject(i);
            moviesDataArray.add(new Movies(currentMovie.getString(ORIGINAL_TITLE_KEY)
                        ,currentMovie.getString(POSTER_PATH_KEY)
                        ,currentMovie.getString(MOVIE_COVER_PHOTO_KEY)
                        ,currentMovie.getString(PLOT_SYNOPSIS_KEY)
                        ,currentMovie.getString(RELEASE_DATE_KEY)
                        ,currentMovie.getString(USER_RATING_KEY)
                        ,currentMovie.getInt(MOVIE_ID_KEY)));
        }
        return moviesDataArray;
    }
    public static ArrayList<Trailer> getMovieTrailerData(String trailerJsonString) throws JSONException{
        JSONObject trailerDataJsonObject=new JSONObject(trailerJsonString);
        JSONArray jsonArray=trailerDataJsonObject.getJSONArray(MOVIE_RESULTS_KEY);

        // We simply check if there is no available trailers by checking the length of the result array


        if (jsonArray.length()==0)noTrailers=true;
        else {
            noTrailers=false;
        }
        ArrayList<Trailer> trailerData=new ArrayList<Trailer>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject currentTrailer=jsonArray.getJSONObject(i);
            trailerData.add(new Trailer(currentTrailer.getString(TRAILER_NAME_KEY),currentTrailer.getString(TRAILER_KEY)));
        }
        return trailerData;
    }
    public static ArrayList<Review> getMovieReviewData(String reviewJsonString) throws JSONException{
        JSONObject reviewDataJsonObject=new JSONObject(reviewJsonString);
        String totalResults=reviewDataJsonObject.getString(TOTAL_REVIEW_RESULTS);
        if (totalResults.equals("0")) noReviews=true;
        else {
            noReviews=false;
        }
        JSONArray jsonArray=reviewDataJsonObject.getJSONArray(MOVIE_RESULTS_KEY);
        ArrayList<Review> reviewData=new ArrayList<Review>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject currentReview=jsonArray.getJSONObject(i);
            reviewData.add(new Review(currentReview.getString(REVIEW_AUTHOR_KEY),currentReview.getString(REVIEW_CONTENT_KEY)));
        }
        return reviewData;
    }
}