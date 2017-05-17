package xyz.michaelobi.popcorn.movieDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;
import xyz.michaelobi.popcorn.data.Review;
import xyz.michaelobi.popcorn.data.Video;
import xyz.michaelobi.popcorn.data.provider.DbMovieRepository;
import xyz.michaelobi.popcorn.data.provider.MovieRepository;
import xyz.michaelobi.popcorn.data.remote.ApiResponse;
import xyz.michaelobi.popcorn.data.remote.Client;
import xyz.michaelobi.popcorn.data.remote.MovieDbService;
import xyz.michaelobi.popcorn.utils.NetworkUtilities;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    Movie movie;
    ImageView backdrop, poster;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;
    TextView title, year, overview;
    MovieDbService movieDbService;
    List<Video> trailers = new ArrayList<>();
    LinearLayout trailersLayout, reviewsLayout;
    FloatingActionButton fabFavorite;
    List<Review> reviews;

    MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieRepository = new DbMovieRepository(this);
        setContentView(R.layout.details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra("movie");
        if (movie == null) {
            finish();
        }
        backdrop = (ImageView) findViewById(R.id.backdrop);
        fabFavorite = (FloatingActionButton) findViewById(R.id.fab_favorite);
        trailersLayout = (LinearLayout) findViewById(R.id.trailers_layout);
        reviewsLayout = (LinearLayout) findViewById(R.id.reviews_layout);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        poster = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        overview = (TextView) findViewById(R.id.overview);
        title.setText(movie.getTitle());
        year.setText(movie.getReleaseYear());
        overview.setText(movie.getOverview());
        ratingBar.setRating((float) (movie.getRating() / 2));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("");
        updateFavoriteFab();
        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdrop);
        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(poster);

        loadTrailers(movie.getId());
        loadReviews(movie.getId());
    }

    private void loadReviews(int id) {
        movieDbService = Client.getApiService();
        movieDbService.getMovieReviews(id).enqueue(new Callback<ApiResponse<Review>>() {
            @Override
            public void onResponse(Call<ApiResponse<Review>> call, Response<ApiResponse<Review>> response) {
                reviews = response.body().getResults();
                displayReviews();
            }

            @Override
            public void onFailure(Call<ApiResponse<Review>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Toast.makeText(DetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void toggleFavorite(View view) {
        Movie m = movieRepository.findByMovieId(movie.getId());
        if (m == null) {
            movieRepository.addMovie(movie);
            updateFavoriteFab();
            return;
        }
        movieRepository.deleteMovie(movie.getId());
        updateFavoriteFab();
    }

    private void updateFavoriteFab() {
        Movie m = movieRepository.findByMovieId(movie.getId());
        if (m == null) {
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
            return;
        }
        fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));

    }

    private void loadTrailers(int id) {
        movieDbService = Client.getApiService();
        movieDbService.getMovieTrailers(id).enqueue(new Callback<ApiResponse<Video>>() {
            @Override
            public void onResponse(Call<ApiResponse<Video>> call, Response<ApiResponse<Video>> response) {
                trailers = response.body().getResults();
                displayTrailers();
            }

            @Override
            public void onFailure(Call<ApiResponse<Video>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Toast.makeText(DetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTrailers() {
        trailers.forEach(video -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.trailer_list_item, null);
            TextView trailerName = (TextView) v.findViewById(R.id.trailer_text);
            trailerName.setText(video.getName());
            trailersLayout.addView(v);
            v.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(NetworkUtilities.YOUTUBE_URL + video.getKey()))));
        });
    }


    private void displayReviews() {
        reviews.forEach(review -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.review_list_item, null);
            TextView author = (TextView) v.findViewById(R.id.author);
            TextView reviewText = (TextView) v.findViewById(R.id.review_text);
            author.setText(review.getAuthor());
            reviewText.setText(review.getContent());
            reviewsLayout.addView(v);
            v.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(review.getUrl()))));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
