package xyz.michaelobi.popcorn.data.provider;

import java.util.List;

import xyz.michaelobi.popcorn.data.Movie;

/**
 * Popcorn
 * Michael Obi
 * 16 05 2017 11:43 PM
 */

public interface MovieRepository {
    Movie findByMovieId(int movieId);

    boolean deleteMovie(int movieId);

    List<Movie> getAll();

    boolean addMovie(Movie movie);
}
