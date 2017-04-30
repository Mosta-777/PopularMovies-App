package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private String[] Authors;
    private String[] reviewContent;

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        holder.reviewTitle.setText("Review " + (position + 1));
        holder.reviewAuthor.setText("by " + Authors[position]);
        holder.theReview.setText(reviewContent[position]);
    }

    @Override
    public int getItemCount() {

        if (Authors == null) return 0;
        return Authors.length;
    } // or reviewContent.length it's the same


    public void setReviewsData(ArrayList<Review> reviewsData) {
        if (reviewsData != null) {
            Authors = new String[reviewsData.size()];
            reviewContent = new String[reviewsData.size()];
            for (int i = 0; i < reviewsData.size(); i++) {
                Review currentReview = reviewsData.get(i);
                Authors[i] = currentReview.getReviewAuthor();
                reviewContent[i] = currentReview.getReviewContent();
            }
            notifyDataSetChanged();
        } else {
            Authors = null;
            reviewContent = null;
        }
    }


    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.review_title)
        TextView reviewTitle;
        @InjectView(R.id.review_author)
        TextView reviewAuthor;
        @InjectView(R.id.review)
        TextView theReview;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
