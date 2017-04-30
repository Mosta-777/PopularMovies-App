package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.DatabaseContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter  extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    public static final String IMAGES_BASE_URL="http://image.tmdb.org/t/p/w185/";
    private ArrayList<Movies> moviesDataArrayList;
    private String[] moviePosters;
    private final MoviesAdapterOnClickHandler mClickHandler;
    public interface MoviesAdapterOnClickHandler {
        void onClick(Movies clickedMovieDetails);
    }
    public MoviesAdapter(MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_poster_in_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
            String missingPartOfTheUrl = moviePosters[position];
            String completeImageUrl = IMAGES_BASE_URL + missingPartOfTheUrl;
            // then we use picasso
            Picasso.with(holder.poster.getContext()).load(completeImageUrl).into(holder.poster);
    }
    @Override
    public int getItemCount() {
        if (null == moviePosters) return 0;
        return moviesDataArrayList.size();
    }
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView poster;
        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            poster=(ImageView)itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movies clickedMovie = moviesDataArrayList.get(adapterPosition);
            mClickHandler.onClick(clickedMovie);
        }
    }
    public void setMoviesData(ArrayList<Movies> MoviesData) {
        if (MoviesData!=null){
            moviesDataArrayList = MoviesData;

                moviePosters = new String[MoviesData.size()];
                for (int i = 0; i < MoviesData.size(); i++) {
                    Movies movie = MoviesData.get(i);
                    moviePosters[i] = movie.getPosterPath();
                }
            notifyDataSetChanged();
            }
        else {
            moviePosters=null;}
    }
}