package com.yellowbite.movienewsreminder2.ui.recyclerView.itemHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datastructures.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>
{
    private Context context;
    private MovieList movieList;

    public MovieAdapter(Context context, MovieList movieList)
    {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        Movie movieToShow = movieList.get(this.context, position);

        if(movieToShow != null)
        {
            holder.showMovie(movieToShow);
        }
    }

    @Override
    public int getItemCount()
    {
        return movieList.size(this.context);
    }
}
