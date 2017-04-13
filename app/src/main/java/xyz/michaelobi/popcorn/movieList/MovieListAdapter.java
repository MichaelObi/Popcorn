package xyz.michaelobi.popcorn.movieList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Popcorn
 * Michael Obi
 * 11 04 2017 12:08 AM
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ImageView imageView;
            TextView textView;

        }
    }
}
