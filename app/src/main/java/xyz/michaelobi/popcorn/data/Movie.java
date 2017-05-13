package xyz.michaelobi.popcorn.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Popcorn
 * Michael Obi
 * 11 04 2017 12:44 AM
 */

public class Movie implements Parcelable {
    private String posterUrl;
    private String overview;
    private String title;
    private String backdropUrl;
    private double rating;
    private String releaseYear;
    private int id;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterUrl);
        dest.writeString(this.overview);
        dest.writeString(this.title);
        dest.writeString(this.backdropUrl);
        dest.writeDouble(this.rating);
        dest.writeString(this.releaseYear);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.posterUrl = in.readString();
        this.overview = in.readString();
        this.title = in.readString();
        this.backdropUrl = in.readString();
        this.rating = in.readDouble();
        this.releaseYear = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
