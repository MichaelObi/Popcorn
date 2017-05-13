package xyz.michaelobi.popcorn.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import xyz.michaelobi.popcorn.data.Video;

/**
 * Popcorn
 * Michael Obi
 * 12 05 2017 5:53 PM
 */

public interface MovieDbService {
    @GET("/movie/{movieId}/videos")
    public Call<List<Video>> getMovieTrailers(@Path("movieId") int movieId);
}