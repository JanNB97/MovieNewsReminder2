package com.yellowbite.movienewsreminder2.tasks.newmovies;

import android.app.Activity;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.functionalinterfaces.LoadedMovieEvent;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoadNewMoviesDescendingExecutor extends LoadMoviesRetryExecutor
{
    private Activity activity;
    private LoadedMovieEvent event;

    public LoadNewMoviesDescendingExecutor(Activity activity, LoadedMovieEvent event, List<Movie> movies)
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
