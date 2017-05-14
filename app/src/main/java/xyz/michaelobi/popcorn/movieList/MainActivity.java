package xyz.michaelobi.popcorn.movieList;

import android.databinding.DataBindingUtil;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;
import xyz.michaelobi.popcorn.databinding.MainBinding;
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
    private final String sortTypeFavorites = "favorite";
    AlertDialog alertDialog;
    View errorView;
    TextView error;
    Button retryButton;
    ArrayList<Movie> movies = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMovies;
    private MovieListAdapter movieListAdapter;
    private String sortBy = sortTypePopular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        MainBinding binding = DataBindingUtil.setContentView(this, R.layout.main);
        errorView = binding.errorLayout;
        error = binding.error;
        retryButton = binding.btnRetry;
        progressBar = binding.progressbarLoading;
        recyclerViewMovies = binding.rvMovies;
        recyclerViewMovies.setHasFixedSize(false);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
        movieListAdapter = new MovieListAdapter(this);
        recyclerViewMovies.setAdapter(movieListAdapter);
        int checkedItemPosition = 0;
        if (savedInstanceState == null) {
            new MovieListTask().execute();
        } else {
            sortBy = savedInstanceState.getString("sortBy");
            movies = savedInstanceState.getParcelableArrayList("movies");
            setActionBarSubtitle();
            displayMovies();
            checkedItemPosition = savedInstanceState.getInt("sort_checked_item_position");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.sort_type, checkedItemPosition, (dialog, which) -> {
                    if (which == 0) {
                        sortBy = sortTypePopular;
                        new MovieListTask().execute();
                    } else if (which == 1) {
                        sortBy = sortTypeTopRated;
                        new MovieListTask().execute();
                    } else if (which == 2) {
                        sortBy = sortTypeFavorites;
                        getFavorites();
                    }
                    dialog.dismiss();
                });
        alertDialog = builder.create();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", movies);
        outState.putString("sortBy", sortBy);
        outState.putInt("sort_checked_item_position", alertDialog.getListView().getCheckedItemPosition());
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    private boolean checkNetworkAvailability() {
        if (!NetworkUtilities.isNetworkEnabled(this)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.internet_connection_error)
                    .setOnDismissListener(dialog1 -> {
                        error.setText(R.string.internet_connection_error);
                        recyclerViewMovies.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        errorView.setVisibility(View.VISIBLE);
                    }).setPositiveButton("Okay", (dialog12, which) -> dialog12.dismiss()).create();
            dialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    private void getFavorites() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<Movie> movieResults = realm.where(Movie.class).findAll();
            movies.clear();
            movies.addAll(realm.copyFromRealm(movieResults));
        }
        displayMovies();
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

    public void onClickReload(View view) {
        new MovieListTask().execute();
    }

    private void displayMovies() {
        recyclerViewMovies.setVisibility(View.VISIBLE);
        movieListAdapter.setMovieList(movies);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        setActionBarSubtitle();
    }

    private void setActionBarSubtitle() {
        if (getSupportActionBar() != null) {
            String s = sortBy.replace("_", " ");
            String sortedBy = s.substring(0, 1).toUpperCase() + s.substring(1); // Capitalize sorttype
            getSupportActionBar().setSubtitle(String.format("%s movies", sortedBy));
        }
    }

    private class MovieListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!checkNetworkAvailability()) {
                return;
            }
            errorView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewMovies.setVisibility(View.GONE);
            setActionBarSubtitle();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) {
                errorView.setVisibility(View.VISIBLE);
                return;
            }
            try {
                Log.e(TAG, response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                movies.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieObject = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(movieObject.getInt("id"));
                    movie.setPosterUrl(NetworkUtilities.buildImageUrl(movieObject.getString("poster_path")));
                    movie.setBackdropUrl(NetworkUtilities.buildImageUrl(movieObject.getString("backdrop_path"),
                            NetworkUtilities.LARGE_IMAGE_SIZE));
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
            displayMovies();
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
