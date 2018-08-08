package com.yellowbite.movienewsreminder2.ui.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

import java.util.Collections;
import java.util.List;

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

    public void handleClickedOnMovieItem(View view, int position)
    {

    }

    public void handleClickedLongOnMovieItem(View view, int position)
    {
        Movie movie = movies.get(position);
        movie.setHot(!movie.isHot());
        // TODO - Save hot movie in file
        this.dataSetChanged(false);
        NotificationMan.showShortToast(this.context, movie.getTitel() + " is selected!");
    }

    public void addItems(List<Movie> movies, boolean saveInFile)
    {
        for(Movie movie : movies)
        {
            this.addItemSimple(movie);
        }

        Collections.sort(this.movies);
        this.dataSetChanged(saveInFile);
    }

    public void addItem(Movie movie, boolean saveInFile)
    {
        this.addItemSimple(movie);
        Collections.sort(movies);

        this.dataSetChanged(saveInFile);
    }

    private void addItemSimple(Movie movie)
    {
        if(!isNew(movie))
        {
            NotificationMan.showShortToast(this.context, movie.getTitel() + " is already in the database");
            return;
        }

        this.movies.add(movie);
    }

    public void removeItem(int position)
    {
        this.movies.remove(position);

        this.dataSetChanged(true);
    }

    private void dataSetChanged(boolean saveInFile)
    {
        this.notifyDataSetChanged();

        if(saveInFile)
        {
            new Thread(() -> MedZenFileMan.setMyMovies(this.context, this.movies))
                    .start();
        }
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
