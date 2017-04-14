package xyz.michaelobi.popcorn.data;

import java.io.Serializable;

/**
 * Popcorn
 * Michael Obi
 * 11 04 2017 12:44 AM
 */

public class Movie implements Serializable {
    private String posterUrl;
    private String overview;
    private String title;
    private String backdropUrl;
    private double rating;
    private String releaseYear;

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseYear() {
        return releaseYear;
    }
}
