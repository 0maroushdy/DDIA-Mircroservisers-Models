package com.moviecatalogservice.models;

public class TrendingMovieItem {

    private String movieId;
    private double averageRating;
    private int ratingCount;

    public TrendingMovieItem() {
    }

    public TrendingMovieItem(String movieId, double averageRating, int ratingCount) {
        this.movieId = movieId;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
