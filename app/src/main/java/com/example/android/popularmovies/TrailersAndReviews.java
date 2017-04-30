package com.example.android.popularmovies;

import java.util.ArrayList;



public class TrailersAndReviews {
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    public TrailersAndReviews (ArrayList<Trailer> trailers1 ,ArrayList<Review> reviews1 ) {
        this.setTrailers(trailers1);
        this.setReviews(reviews1);
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
