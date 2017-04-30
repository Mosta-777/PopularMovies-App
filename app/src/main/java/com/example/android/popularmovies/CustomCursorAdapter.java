package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.DatabaseContract;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    private Cursor cursor;
    private Context mContext;

    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favourite_movie_list_item, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        cursor.moveToPosition(position);
        int idIndex = cursor.getColumnIndex(DatabaseContract.MoviesEntry._ID);
        final int id = cursor.getInt(idIndex);
        holder.itemView.setTag(id);
        holder.favouriteMovieName.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.MoviesEntry.MOVIE_TITLE)));
        holder.FavouriteMovieOverview.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.MoviesEntry.OVERVIEW)));
        holder.FavouriteMovieDate.setText(MovieDetailsActivity.formatAndSetDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.MoviesEntry.RELEASE_DATE))));
        holder.ratingBar.setRating((Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseContract.MoviesEntry.AVERAGE_VOTE))))/2);
    }
    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }
    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c; // new cursor value assigned
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    class TaskViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.favourite_movie_name) TextView favouriteMovieName;
        @InjectView(R.id.favourite_movie_release) TextView FavouriteMovieDate;
        @InjectView(R.id.favourite_movie_overview) TextView FavouriteMovieOverview;
        @InjectView(R.id.ratingBar2) RatingBar ratingBar;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }
    }
}