package com.yellowbite.movienewsreminder2.ui.tasks;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class GetMoviesAsyn
{
    private final static int MAX_RETRIES_TO_LOAD = 3;

    private AppCompatActivity activity;
    private LoadedMovieEvent event;

    private ThreadPoolExecutor executor;

    private Runnable onFinishedLoading;

    private AtomicInteger loadedMovies = new AtomicInteger(0);

    public GetMoviesAsyn(AppCompatActivity activity, LoadedMovieEvent event, List<Movie> movies, Runnable onFinishedLoading)
    {
        this.activity = activity;
        this.event = event;

        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        this.onFinishedLoading = onFinishedLoading;

        this.getMovies(movies);
    }

    private void getMovies(List<Movie> movies)
    {
        final int numOfMovies = movies.size();

        for(Movie movie : movies)
        {
            this.executor.execute(() -> executeMovie(movie, numOfMovies, 0));
        }
    }

    private void executeMovie(Movie movie, int numOfMovies, int numOfRetries)
    {
        if(numOfRetries > MAX_RETRIES_TO_LOAD)
        {
            return;
        }

        try
        {
            MedZenMovieSiteScraper.getMovie(movie);

            if(movie.getStatus() == null)
            {
                numOfRetries++;
                int finalNumOfRetries = numOfRetries;
                this.executor.execute(() -> this.executeMovie(movie, numOfMovies, finalNumOfRetries));
                return;
            }
        } catch (IOException ignored) {}

        int l = loadedMovies.incrementAndGet();

        if(l >= numOfMovies)
        {
            // all movies loaded
            this.activity.runOnUiThread(() -> {
                this.event.loadedMovie(l);
                this.onFinishedLoading.run();
                this.executor.shutdown();
            });
        }
        else
        {
            this.activity.runOnUiThread(() -> this.event.loadedMovie(l));
        }
    }
}
