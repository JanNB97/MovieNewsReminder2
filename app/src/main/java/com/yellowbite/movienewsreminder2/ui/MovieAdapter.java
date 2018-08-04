package com.yellowbite.movienewsreminder2.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>
{
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies)
    {
        this.context = context;
        this.movies = movies;
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
        Movie movieToShow = movies.get(position);

        if(movieToShow != null)
        {
            holder.showMovie(movieToShow);
        }
    }

    @Override
    public int getItemCount()
    {
        return movies.size();
    }

    public void addItem(Movie movie)
    {
        if(!isNew(movie))
        {
            NotificationMan.showShortToast(this.context, movie.getTitel() + " is already in the database");
            return;
        }

        this.movies.add(movie);
        Collections.sort(movies);

        this.dataSetChanged();
    }

    public void removeItem(int position)
    {
        this.movies.remove(position);

        this.dataSetChanged();
    }

    private void dataSetChanged()
    {
        this.notifyDataSetChanged();

        new Thread(() -> MedZenFileMan.setMyMovies(this.context, this.movies))
                .start();
    }

    private boolean isNew(Movie movie)
    {
        for(Movie movieInDatabase : this.movies)
        {
            if(movieInDatabase.getMediaBarcode() == movie.getMediaBarcode())
            {
                return false;
            }
        }

        return true;
    }
}
