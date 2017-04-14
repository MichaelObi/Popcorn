package xyz.michaelobi.popcorn.movieDetails;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;

public class DetailsActivity extends AppCompatActivity{

    Movie movie;
    ImageView backdrop, poster;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;
    TextView title, year, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        if (movie == null) {
            finish();
        }
        backdrop = (ImageView) findViewById(R.id.backdrop);
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
        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .into(backdrop);
        Picasso.with(this)
                .load(movie.getPosterUrl())
                .into(poster);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
