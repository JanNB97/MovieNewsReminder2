package com.yellowbite.movienewsreminder2.ui.tasks;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class GetMoviesNotifier
{
    private AppCompatActivity activity;
    private LoadedMovieEvent event;

    private ThreadPoolExecutor executor;

    private Runnable onFinishedLoading;

    private AtomicInteger loadedMovies = new AtomicInteger(0);

    public GetMoviesNotifier(AppCompatActivity activity, LoadedMovieEvent event, List<Movie> movies, Runnable onFinishedLoading)
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
            this.executor.execute(() -> executeMovie(movie, numOfMovies));
        }
    }

    private void executeMovie(Movie movie, int numOfMovies)
    {
        try
        {
            MedZenMovieSiteScraper.getMovie(movie);
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
