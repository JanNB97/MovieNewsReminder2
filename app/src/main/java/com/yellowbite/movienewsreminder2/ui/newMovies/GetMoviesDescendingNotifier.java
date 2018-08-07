package com.yellowbite.movienewsreminder2.ui.newMovies;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.List;

public class GetMoviesDescendingNotifier
{
    private AppCompatActivity activity;
    private LoadedMovieEvent event;

    public GetMoviesDescendingNotifier(AppCompatActivity activity, LoadedMovieEvent event, List<Movie> movies)
    {
        this.activity = activity;
        this.event = event;

        new Thread(() -> this.getMovies(movies))
        .start();
    }

    private void getMovies(List<Movie> movies)
    {
        for(int i = movies.size() - 1; i >= 0; i--)
        {
            try
            {
                MedZenMovieSiteScraper.getMovie(movies.get(i));
            } catch (IOException ignored) {}

            int finalI = i;
            activity.runOnUiThread(() -> event.loadedMovie(finalI));
        }
    }
}
