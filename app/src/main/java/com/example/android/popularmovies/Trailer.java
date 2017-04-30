package com.example.android.popularmovies;

public class Trailer {
    private String trailerName;
    private String trailerKey;
    public Trailer(String name,String key){
        this.setTrailerName(name);
        this.setTrailerKey(key);
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }
}
