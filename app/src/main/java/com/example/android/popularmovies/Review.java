package com.example.android.popularmovies;

public class Review {
    private String reviewAuthor;
    private String reviewContent;
    public Review(String author,String content){
        this.setReviewAuthor(author);
        this.setReviewContent(content);
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
