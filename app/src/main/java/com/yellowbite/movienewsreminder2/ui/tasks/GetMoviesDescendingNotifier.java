package com.yellowbite.movienewsreminder2.ui.tasks;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class GetMoviesDescendingNotifier
{
    private static final int THREAD_POOL_SIZE = 2;

    private AppCompatActivity activity;
    private LoadedMovieEvent event;

    private ThreadPoolExecutor executor;

    public GetMoviesDescendingNotifier(AppCompatActivity activity, LoadedMovieEvent event, List<Movie> movies)
    {
        if(movies.isEmpty())
        {
            event.loadedMovie(0);
        }

        this.activity = activity;
        this.event = event;

        this.executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        this.getMovies(movies);
    }

    private void getMovies(List<Movie> movies)
    {
        for(int i = movies.size() - 1; i >= 0; i--)
        {
            int finalI = i;

            this.executor.execute(() -> {
                try
                {
                    MedZenMovieSiteScraper.getMovie(movies.get(finalI));
                } catch (IOException ignored) {}

                this.activity.runOnUiThread(() -> this.event.loadedMovie(finalI));
            });
        }
    }
}
