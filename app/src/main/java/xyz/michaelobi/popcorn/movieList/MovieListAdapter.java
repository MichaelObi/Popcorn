package xyz.michaelobi.popcorn.movieList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import xyz.michaelobi.popcorn.R;
import xyz.michaelobi.popcorn.data.Movie;
import xyz.michaelobi.popcorn.movieDetails.DetailsActivity;

/**
 * Popcorn
 * Michael Obi
 * 11 04 2017 12:08 AM
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private final Context context;
    private List<Movie> movieList = new ArrayList<>();

    public MovieListAdapter(Context context) {
        this.context = context;
    }

    public void setMovieList(List<Movie> movies) {

        if (movieList == movies) {
            return;
        }
        movieList = movies;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        String imgUrl = movie.getPosterUrl();
        Picasso.with(context).load(imgUrl)
                .into(holder.poster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View itemView;
        Context context;
        ImageView poster;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.itemView = itemView;
            poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
