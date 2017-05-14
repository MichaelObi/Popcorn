package xyz.michaelobi.popcorn.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import xyz.michaelobi.popcorn.data.Review;
import xyz.michaelobi.popcorn.data.Video;

/**
 * Popcorn
 * Michael Obi
 * 12 05 2017 5:53 PM
 */

public interface MovieDbService {
    @GET("movie/{movieId}/videos")
    public Call<ApiResponse<Video>> getMovieTrailers(@Path("movieId") int movieId);

    @GET("movie/{movieId}/reviews")
    public Call<ApiResponse<Review>> getMovieReviews(@Path("movieId") int movieId);
}
