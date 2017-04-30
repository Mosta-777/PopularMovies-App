package com.example.android.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.data.DatabaseContract;

public class Movies implements Parcelable{

    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private String releaseData;
    private String averageVote;
    private int id;


    public Movies(String original_title,String poster_path,String backdrop_path,String movie_overview,String release_date
                    ,String average_vote,int movie_id ){
        setOriginalTitle(original_title);
        setPosterPath(poster_path);
        setBackdropPath(backdrop_path);
        setOverview(movie_overview);
        setReleaseData(release_date);
        setAverageVote(average_vote);
        setId(movie_id);

    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getOriginalTitle());
        parcel.writeString(getPosterPath());
        parcel.writeString(getBackdropPath());
        parcel.writeString(getOverview());
        parcel.writeString(getReleaseData());
        parcel.writeString(getAverageVote());
        parcel.writeInt(getId());

    }

    protected Movies(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseData = in.readString();
        averageVote = in.readString();
        id = in.readInt();

    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public String getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(String averageVote) {
        this.averageVote = averageVote;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }


}