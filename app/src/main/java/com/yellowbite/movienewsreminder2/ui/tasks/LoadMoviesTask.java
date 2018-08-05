package com.yellowbite.movienewsreminder2.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.util.List;
import java.util.logging.Logger;

public class LoadMoviesTask extends AsyncTask<List<Movie>, Void, List<Movie>>
{
    private Context context;
    private RecyclerView movieRecyclerView;

    public LoadMoviesTask(Context context, RecyclerView movieRecyclerView)
    {
        this.context = context;
        this.movieRecyclerView = movieRecyclerView;
    }

    @Override
    protected List<Movie> doInBackground(List<Movie>... lists)
    {
        if(lists.length != 1)
        {
            throw new IllegalArgumentException();
        }

        List<Movie> myMovies = lists[0];

        // load out of file
        MedZenFileMan.getMyMovies(this.context, myMovies);

        // get status out of downloaded html-document
        MedZenMovieSiteScraper.getMovies(myMovies);

        return myMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> myMovies)
    {
        this.addAdapterToRecyclerView(myMovies);
    }

    private void addAdapterToRecyclerView(List<Movie> myMovies)
    {
        // specify adapter
        MovieAdapter movieAdapter = new MovieAdapter(this.context, myMovies);
        this.movieRecyclerView.setAdapter(movieAdapter);

        new ItemTouchHelper(new SwipeCallback(movieAdapter)).attachToRecyclerView(this.movieRecyclerView);

        this.movieRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, this.movieRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
                movieAdapter.handleClickedOnMovieItem(view, position);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                movieAdapter.handleClickedLongOnMovieItem(view, position);
            }
        }));
    }
}
