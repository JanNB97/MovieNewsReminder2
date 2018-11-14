package com.yellowbite.movienewsreminder2.fragments.ui.recyclerView.itemHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>
{
    private Context context;
    private MovieList movieList;

    private int viewHolderLayout;

    public MovieAdapter(Context context, MovieList movieList, int viewHolderLayout)
    {
        this.context = context;
        this.movieList = movieList;
        this.viewHolderLayout = viewHolderLayout;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.viewHolderLayout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        Movie movieToShow = movieList.get(position);

        if(movieToShow != null)
        {
            holder.showMovie(movieToShow);
        }
    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }
}
