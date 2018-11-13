package com.yellowbite.movienewsreminder2.tasks.mainActivity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadMyMoviesRetryExecutor extends LoadMoviesRetryExecutor
{
    private Activity activity;
    private LoadedMoviesEvent event;

    private Runnable onFinishedLoading;

    private AtomicInteger loadedMovies = new AtomicInteger(0);

    public LoadMyMoviesRetryExecutor(Activity activity, LoadedMoviesEvent event, List<Movie> movies, Runnable onFinishedLoading)
    {
        super((ThreadPoolExecutor) Executors.newCachedThreadPool(), movies,
                MedZenMovieSiteScraper::getMovie,true);

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
