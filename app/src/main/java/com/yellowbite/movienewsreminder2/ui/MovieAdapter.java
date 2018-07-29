package com.yellowbite.movienewsreminder2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>
{
    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies)
    {
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MovieViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        holder.getTextView().setText(movies.get(position).toString());
    }

    @Override
    public int getItemCount()
    {
        return movies.size();
    }
}
