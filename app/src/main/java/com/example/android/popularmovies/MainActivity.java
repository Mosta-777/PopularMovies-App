package com.example.android.popularmovies;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.popularmovies.utilities.MoviesDataJasonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.MoviesAdapter.MoviesAdapterOnClickHandler;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movies>> {
    private TextView errorMessageDisplay;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MoviesAdapter moviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private int currentScrollPosition;
    private static final int LOAD_POPULAR_MOVIES = 0;
    private static final int LOAD_MOST_RATED_MOVIES = 1;
    public static final String PARCELABLE_KEY = "parcelable_key";
    private static final int MOVIE_LOADER_CONSTANT = 203;
    private static final String LOADER_BUNDLE_KEY = "69";
    private static final String BUNDLE_KEY="key";
    private static final String CURRENT_SCROLL_POSITION_KEY="position_key";
    private static int whichWay=LOAD_POPULAR_MOVIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_movies);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        gridLayoutManager = new GridLayoutManager(getBaseContext(), calculateNoOfColumns(this));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        if(savedInstanceState!=null){
            //currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL_POSITION_KEY);
            if (savedInstanceState.containsKey(BUNDLE_KEY)){
                whichWay=savedInstanceState.getInt(BUNDLE_KEY);
            }
        }
        loadMoviePosters(whichWay);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null)
        currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL_POSITION_KEY,0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY,whichWay);
        outState.putInt(CURRENT_SCROLL_POSITION_KEY,gridLayoutManager.findFirstVisibleItemPosition());
    }

    private void loadMoviePosters(int whichType) {
        showMoviePosters();
        Bundle bundle = new Bundle();
        bundle.putInt(LOADER_BUNDLE_KEY, whichType);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Movies>> movieLoader = loaderManager.getLoader(MOVIE_LOADER_CONSTANT);
        if (movieLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER_CONSTANT, bundle, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER_CONSTANT, bundle, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popular_or_most_rated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.go_to_popular : whichWay=LOAD_POPULAR_MOVIES; break;
            case R.id.go_to_most_rated : whichWay=LOAD_MOST_RATED_MOVIES ; break;
            case R.id.go_to_favourite : Intent intent=new Intent(this,FavouriteMovies.class);
            startActivity(intent);
            return true;
        }
        loadMoviePosters(whichWay);
        return true;
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMoviePosters() {
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(Movies clickedMovieDetails) {
        // We then use parcelable interface to pass the movies object containing the clicked
        // movie details to be shown in the next activity .
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(PARCELABLE_KEY, clickedMovieDetails);
        i.putExtras(b);
        i.setClass(this, MovieDetailsActivity.class);
        startActivity(i);
    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Movies>>(this) {
            @Override
            protected void onStartLoading() {
                if (bundle == null) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public ArrayList<Movies> loadInBackground() {
                int indicator = bundle.getInt(LOADER_BUNDLE_KEY);
                    // We check if we are online then we load the data from the api
                    if (isOnline()) {
                        URL dataRequestURL = NetworkUtils.buildURL(indicator);
                        try {
                            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(dataRequestURL);
                            ArrayList<Movies> moviesData = MoviesDataJasonUtils.getSimpleMoviesDataFromJson(MainActivity.this, jsonResponse);
                            return moviesData;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showMoviePosters();
            moviesAdapter.setMoviesData(movies);
            recyclerView.smoothScrollToPosition(currentScrollPosition);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
    }
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}