package com.yellowbite.movienewsreminder2.tasks;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class LoadMoviesRetryExecutor
{
    protected ThreadPoolExecutor executor;
    private final static int MAX_RETRIES_PER_MOVIE = 3;

    public LoadMoviesRetryExecutor(ThreadPoolExecutor executor, List<Movie> movies, boolean ascending)
    {
        this.executor = executor;

        this.getMovies(movies, ascending);
    }

    private void getMovies(List<Movie> movies, boolean ascending)
    {
        final int numOfMovies = movies.size();

        if(ascending)
        {
            int i = 0;
            for(Movie movie : movies)
            {
                int finalI = i;
                this.executor.execute(() -> executeMovie(movie, numOfMovies, 0, finalI));
                i++;
            }
        }
        else
        {
            for(int i = movies.size() - 1; i >= 0; i--)
            {
                Movie movie = movies.get(i);
                int finalI = i;
                this.executor.execute(() -> executeMovie(movie, numOfMovies, 0, finalI));
            }
        }
    }

    private void executeMovie(Movie movie, int numOfMovies, int numOfRetries, int id)
    {
        try
        {
            MedZenMovieSiteScraper.getMovie(movie);

            if(movie.getStatus() == null && numOfRetries < MAX_RETRIES_PER_MOVIE)
            {
                this.retryToLoadMovie(movie, numOfMovies, numOfRetries, id);
                return;
            }
        } catch (IOException ignored) {}

        this.onMovieLoaded(numOfMovies, id);
    }

    private void retryToLoadMovie(Movie movie, int numOfMovies, int numOfRetries, int id)
    {
        int finalNumOfRetries = numOfRetries + 1;
        this.executor.execute(() -> this.executeMovie(movie, numOfMovies, finalNumOfRetries, id));
    }

    protected abstract void onMovieLoaded(int numOfMovies, int id);
}
