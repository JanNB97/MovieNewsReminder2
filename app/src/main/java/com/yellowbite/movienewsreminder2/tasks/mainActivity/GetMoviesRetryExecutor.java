package com.yellowbite.movienewsreminder2.tasks.mainActivity;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.tasks.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class GetMoviesRetryExecutor
{
    private final static int MAX_RETRIES_PER_MOVIE = 3;

    private AppCompatActivity activity;
    private LoadedMoviesEvent event;

    private ThreadPoolExecutor executor;

    private Runnable onFinishedLoading;

    private AtomicInteger loadedMovies = new AtomicInteger(0);

    public GetMoviesRetryExecutor(AppCompatActivity activity, LoadedMoviesEvent event, List<Movie> movies, Runnable onFinishedLoading)
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
        try
        {
            MedZenMovieSiteScraper.getMovie(movie);

            if(movie.getStatus() == null && numOfRetries < MAX_RETRIES_PER_MOVIE)
            {
                this.retryToLoadMovie(movie, numOfMovies, numOfRetries);
                return;
            }
        } catch (IOException ignored) {}

        this.callbackLoadedMovies(numOfMovies);
    }

    private void retryToLoadMovie(Movie movie, int numOfMovies, int numOfRetries)
    {
        int finalNumOfRetries = numOfRetries + 1;
        this.executor.execute(() -> this.executeMovie(movie, numOfMovies, finalNumOfRetries));
    }

    private void callbackLoadedMovies(int numOfMovies)
    {
        int l = loadedMovies.incrementAndGet();

        if(l >= numOfMovies)
        {
            // all movies loaded
            this.activity.runOnUiThread(() -> {
                this.event.loadedMovies(l);
                this.onFinishedLoading.run();
                this.executor.shutdown();
            });
        }
        else
        {
            this.activity.runOnUiThread(() -> this.event.loadedMovies(l));
        }
    }
}
