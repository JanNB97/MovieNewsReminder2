package com.yellowbite.movienewsreminder2.tasks.mainActivity;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadMyMoviesRetryExecutor extends LoadMoviesRetryExecutor
{
    private AppCompatActivity activity;
    private LoadedMoviesEvent event;

    private Runnable onFinishedLoading;

    private AtomicInteger loadedMovies = new AtomicInteger(0);

    public LoadMyMoviesRetryExecutor(AppCompatActivity activity, LoadedMoviesEvent event, List<Movie> movies, Runnable onFinishedLoading)
    {
        super((ThreadPoolExecutor) Executors.newCachedThreadPool(), movies, true);

        this.activity = activity;
        this.event = event;

        this.onFinishedLoading = onFinishedLoading;
    }

    @Override
    protected void onMovieLoaded(int numOfMovies, int id)
    {
        int l = loadedMovies.incrementAndGet();

        if(l >= numOfMovies)
        {
            // all movies loaded
            this.activity.runOnUiThread(() -> {
                this.event.loadedMovies(l);
                this.onFinishedLoading.run();
                executor.shutdown();
            });
        }
        else
        {
            this.activity.runOnUiThread(() -> this.event.loadedMovies(l));
        }
    }
}
