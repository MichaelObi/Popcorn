package xyz.michaelobi.popcorn.movieList;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;
import xyz.michaelobi.popcorn.utils.NetworkUtilities;

/**
 * Popcorn
 * Michael Obi
 * 11 04 2017 12:07 AM
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String sortTypePopular = "popular";
    private final String sortTypeTopRated = "top_rated";
    AlertDialog alertDialog;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMovies;
    private MovieListAdapter movieListAdapter;
    private String sortBy = sortTypePopular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkNetworkAvailability();
        progressBar = (ProgressBar) findViewById(R.id.progressbar_loading);
        recyclerViewMovies = (RecyclerView) findViewById(R.id.rv_movies);
        movieListAdapter = new MovieListAdapter(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        recyclerViewMovies.setLayoutManager(layoutManager);
        recyclerViewMovies.setAdapter(movieListAdapter);

        new MovieListTask().execute();
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    private void checkNetworkAvailability() {
        if (!NetworkUtilities.isNetworkEnabled(this)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Connection Unavailable")
                    .setMessage("Unable to establish internet connection")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.sort_type, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sortBy = sortTypePopular;
                        } else {
                            sortBy = sortTypeTopRated;
                        }
                        dialog.dismiss();
                        new MovieListTask().execute();
                    }
                });
        alertDialog = builder.create();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                alertDialog.show();
                break;
            default:
        }
        return true;
    }

    private class MovieListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewMovies.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                String s = sortBy.replace("_", " ");
                String sortedBy = s.substring(0, 1).toUpperCase() + s.substring(1); // Capitalize sorttype
                getSupportActionBar().setSubtitle(String.format("%s movies", sortedBy));
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(MainActivity.this, "A network error occurred", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Movie> movies = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieObject = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setPosterUrl(NetworkUtilities.buildImageUrl(movieObject.getString("poster_path")));
                    movie.setBackdropUrl(NetworkUtilities.buildImageUrl(movieObject.getString("backdrop_path")));
                    movie.setTitle(movieObject.getString("title"));
                    movie.setOverview(movieObject.getString("overview"));
                    movie.setRating(movieObject.getDouble("vote_average"));

                    String releaseDate = movieObject.getString("release_date");
                    String year = releaseDate.split("-")[0];
                    movie.setReleaseYear(year);
                    movies.add(movie);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            recyclerViewMovies.setVisibility(View.VISIBLE);
            movieListAdapter.setMovieList(movies);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... params) {
            URL url = NetworkUtilities.buildUrl(sortBy);
            try {
                return NetworkUtilities.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }
    }
}
