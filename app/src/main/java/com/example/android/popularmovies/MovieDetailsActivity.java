package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;

import com.example.android.popularmovies.data.DatabaseContract;
import com.example.android.popularmovies.utilities.MoviesDataJasonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity implements TrailersAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<TrailersAndReviews> {
    private Movies movies;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    private final int TRAILERS_AND_REVIEWS_LOADER_CONSTANT = 777;
    // We consider using butterknife to prevent using too much findViewById()s
    // we could have also used data binding class .
    @InjectView(R.id.movie_cover)
    ImageView movieCover;
    @InjectView(R.id.the_poster)
    ImageView moviePoster;
    @InjectView(R.id.movie_title)
    TextView movieTitle;
    @InjectView(R.id.rating)
    TextView movieRating;
    @InjectView(R.id.overview)
    TextView movieOverview;
    @InjectView(R.id.ratingBar)
    RatingBar ratingBar;
    @InjectView(R.id.release_date)
    TextView releaseDate;
    @InjectView(R.id.recyclerView_trailers)
    RecyclerView trailersRecyclerView;
    @InjectView(R.id.recyclerView_reviews)
    RecyclerView overViewsRecyclerView;
    @InjectView(R.id.error_message_display_details_activity)
    TextView errorTrailers;
    @InjectView(R.id.error_message_display_details_activity_1)
    TextView errorReviews;
    @InjectView(R.id.list_progress_bar)
    ProgressBar trailersProgressBar;
    @InjectView(R.id.list_progress_bar_1)
    ProgressBar reviewsProgressBar;
    @InjectView(R.id.no_reviews_text)
    TextView noReviewsText;
    @InjectView(R.id.no_trailers_text)
    TextView noTrailersText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        getSupportActionBar().hide();
        LinearLayoutManager trailersLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager overviewsLAyoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ButterKnife.inject(this);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);
        overViewsRecyclerView.setLayoutManager(overviewsLAyoutManager);
        overViewsRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setHasFixedSize(true);
        trailersAdapter = new TrailersAdapter(this);
        reviewsAdapter = new ReviewsAdapter();
        overViewsRecyclerView.setAdapter(reviewsAdapter);
        trailersRecyclerView.setAdapter(trailersAdapter);
        populateData();

    }

    @OnClick(R.id.add_to_favourites)
    public void addToFavourites() {
        if (theTaskExistsInTheFavourites(movies.getId())) {
            Toast.makeText(this, "The Movie is already saved in the favourites !", Toast.LENGTH_SHORT).show();
        }
        else {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.MoviesEntry.MOVIE_TITLE, movies.getOriginalTitle());
        contentValues.put(DatabaseContract.MoviesEntry.MOVIE_API_ID, movies.getId());
        contentValues.put(DatabaseContract.MoviesEntry.RELEASE_DATE, movies.getReleaseData());
        contentValues.put(DatabaseContract.MoviesEntry.OVERVIEW, movies.getOverview());
        contentValues.put(DatabaseContract.MoviesEntry.AVERAGE_VOTE, movies.getAverageVote());
        Uri uri = getContentResolver().insert(DatabaseContract.MoviesEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, "Movie saved to favourites !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Problem saving the movie .", Toast.LENGTH_SHORT).show();
        }
    }
}

    private boolean theTaskExistsInTheFavourites(final int taskApiID) {
        /*
        * The query for that single movie could be made in a background thread .
        * */
        Cursor cursor;
        String mSelection= DatabaseContract.MoviesEntry.MOVIE_API_ID+"=?";
        String [] mSelectionArgs=new String []{String.valueOf(taskApiID)};
        cursor=getContentResolver().query(DatabaseContract.MoviesEntry.CONTENT_URI,
                        null,
                        mSelection,
                        mSelectionArgs,
                        null);
        if (cursor.getCount()==0) {
            return false;
        }
        return true;
    }

    private void populateData() {
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            movies = b.getParcelable(MainActivity.PARCELABLE_KEY);
            ButterKnife.inject(this);
            loadImages();
            movieTitle.setText(movies.getOriginalTitle());
            setRatings();
            movieOverview.setText(movies.getOverview());
            releaseDate.setText(formatAndSetDate(movies.getReleaseData()));
            fillTheRecyclerViews();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void fillTheRecyclerViews() {
        showRecyclerViews();
        initializeOreRestartLoader();
    }

    private void initializeOreRestartLoader() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<TrailersAndReviews> loader = loaderManager.getLoader(TRAILERS_AND_REVIEWS_LOADER_CONSTANT);
        if (loader == null) {
            loaderManager.initLoader(TRAILERS_AND_REVIEWS_LOADER_CONSTANT, null, this);
        } else {
            loaderManager.restartLoader(TRAILERS_AND_REVIEWS_LOADER_CONSTANT, null, this);
        }
    }

    private void showRecyclerViews() {
        trailersRecyclerView.setVisibility(View.VISIBLE);
        overViewsRecyclerView.setVisibility(View.VISIBLE);
        errorTrailers.setVisibility(View.INVISIBLE);
        errorReviews.setVisibility(View.INVISIBLE);
    }

    private void showErrorMesages() {
        trailersRecyclerView.setVisibility(View.INVISIBLE);
        overViewsRecyclerView.setVisibility(View.INVISIBLE);
        errorTrailers.setVisibility(View.VISIBLE);
        errorReviews.setVisibility(View.VISIBLE);
    }

    public static String formatAndSetDate(String date1) {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        return formattedDate;
    }

    private void setRatings() {
        movieRating.setText(movies.getAverageVote());
        float rating = Float.parseFloat(movies.getAverageVote());
        ratingBar.setRating(rating / 2); //scaling rating
    }

    private void loadImages() {
        Picasso.with(this).load(MoviesAdapter.IMAGES_BASE_URL + movies.getBackdropPath()).into(movieCover);
        Picasso.with(this).load(MoviesAdapter.IMAGES_BASE_URL + movies.getPosterPath()).into(moviePoster);
    }

    @Override
    public void onClick(String key) {
        Intent openTrailer = new Intent(Intent.ACTION_VIEW, NetworkUtils.getTrailerUri(key));
        if (openTrailer.resolveActivity(getPackageManager()) != null) {
            // To check if there is an available app to perform our task
            startActivity(openTrailer);
        }
    }

    @Override
    public Loader<TrailersAndReviews> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<TrailersAndReviews>(this) {
            @Override
            protected void onStartLoading() {
                reviewsProgressBar.setVisibility(View.VISIBLE);
                trailersProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public TrailersAndReviews loadInBackground() {
                if (isOnline()) {
                    URL trailerDataRequestURL = NetworkUtils.buildTrailersOrReviewsURL(String.valueOf(movies.getId()), NetworkUtils.TRAILERS);
                    URL reviewDataRequestURL = NetworkUtils.buildTrailersOrReviewsURL(String.valueOf(movies.getId()), NetworkUtils.REVIEWS);
                    try {
                        String trailersJsonResponse = NetworkUtils.getResponseFromHttpUrl(trailerDataRequestURL);
                        String reviewJsonResponse = NetworkUtils.getResponseFromHttpUrl(reviewDataRequestURL);
                        return new TrailersAndReviews(MoviesDataJasonUtils.getMovieTrailerData(trailersJsonResponse)
                                , MoviesDataJasonUtils.getMovieReviewData(reviewJsonResponse));
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<TrailersAndReviews> loader, TrailersAndReviews data) {
        reviewsProgressBar.setVisibility(View.INVISIBLE);
        trailersProgressBar.setVisibility(View.INVISIBLE);
        if (MoviesDataJasonUtils.noReviews) noReviewsSetup();
        if (MoviesDataJasonUtils.noTrailers) noTrailersSetup();
        if (data != null) {
            trailersAdapter.setTrailersData(data.getTrailers());
            reviewsAdapter.setReviewsData(data.getReviews());
        } else {
            showErrorMesages();
        }
    }

    @Override
    public void onLoaderReset(Loader<TrailersAndReviews> loader) {

    }

    private void noTrailersSetup() {
        overViewsRecyclerView.setVisibility(View.INVISIBLE);
        noTrailersText.setVisibility(View.VISIBLE);
    }

    private void noReviewsSetup() {
        overViewsRecyclerView.setVisibility(View.INVISIBLE);
        noReviewsText.setVisibility(View.VISIBLE);
    }
}
