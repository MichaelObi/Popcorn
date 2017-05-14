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

import io.realm.Realm;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;
import xyz.michaelobi.popcorn.data.Video;
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
    LinearLayout trailersLayout;
    FloatingActionButton fabFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);

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
    }


    public void toggleFavorite(View view) {
        try (Realm realm = Realm.getDefaultInstance()) {
            Movie m = realm.where(Movie.class).equalTo("id", movie.getId()).findFirst();
            if (m == null) {
                //Movie isn't favorite
                realm.beginTransaction();
                realm.copyToRealm(movie);
                realm.commitTransaction();
                updateFavoriteFab();
                return;
            }
            // All changes to data must happen in a transaction
            realm.executeTransaction(realm1 -> RealmObject.deleteFromRealm(m));
            updateFavoriteFab();
        }
    }

    private void updateFavoriteFab() {
        try (Realm realm = Realm.getDefaultInstance()) {
            Movie m = realm.where(Movie.class).equalTo("id", movie.getId()).findFirst();
            if (m == null) {
                fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border));
                return;
            }
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
