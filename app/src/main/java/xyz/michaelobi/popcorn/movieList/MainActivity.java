package xyz.michaelobi.popcorn.movieList;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Collections;
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

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkNetworkAvailability();
        progressBar = (ProgressBar) findViewById(R.id.progressbar_loading);
        new MovieListTask().execute();

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

    private class MovieListTask extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            Toast.makeText(MainActivity.this, movies.size() + " movies found", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            return Collections.emptyList();
        }
    }
}
