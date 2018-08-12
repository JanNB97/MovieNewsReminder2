package com.yellowbite.movienewsreminder2.tasks.newMovies;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoadNewMoviesDescendingExecutor extends LoadMoviesRetryExecutor
{
    private AppCompatActivity activity;
    private LoadedMovieEvent event;

    public LoadNewMoviesDescendingExecutor(AppCompatActivity activity, LoadedMovieEvent event, List<Movie> movies)
    {
        super((ThreadPoolExecutor) Executors.newFixedThreadPool(2), movies,
                MedZenMovieSiteScraper::getMovieWithDetails,false);

        this.activity = activity;
        this.event = event;
    }

    @Override
    protected void onMovieLoaded(int numOfMovies, int id)
    {
        this.activity.runOnUiThread(() -> {
            this.event.loadedMovie(id);
        });
    }
}
