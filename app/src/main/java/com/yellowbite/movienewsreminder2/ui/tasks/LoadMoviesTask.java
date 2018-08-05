package com.yellowbite.movienewsreminder2.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class LoadMoviesTask extends AsyncTask<List<Movie>, Integer, List<Movie>>
{
    private Context context;
    private RecyclerView movieRecyclerView;

    private ProgressBar loadingProgressBar;
    private Button addMovieButton;
    private TextView moviesUpdateTextView;

    public LoadMoviesTask(AppCompatActivity app, RecyclerView movieRecyclerView)
    {
        this.context = app.getApplicationContext();
        this.movieRecyclerView = movieRecyclerView;

        this.loadingProgressBar = app.findViewById(R.id.loadingProgressBar);

        this.addMovieButton = app.findViewById(R.id.addMovieButton);
        this.moviesUpdateTextView = app.findViewById(R.id.moviesUpdateTextView);
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
        this.publishProgress(null, myMovies.size());

        int i = 0;
        // get status out of downloaded html-document
        for(Movie essentialMovie : myMovies)
        {
            try
            {
                MedZenMovieSiteScraper.getMovie(essentialMovie);
            }
            catch (IOException ignored) {}

            i++;
            this.publishProgress(i);
        }

        Collections.sort(myMovies);

        return myMovies;
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        if(values[0] == null)
        {
            int maxValue = values[1];
            this.loadingProgressBar.setMax(maxValue);
            return;
        }

        loadingProgressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<Movie> myMovies)
    {
        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addAdapterToRecyclerView(myMovies);
        this.addMovieButton.setEnabled(true);
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
