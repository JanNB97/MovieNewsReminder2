package com.yellowbite.movienewsreminder2.tasks;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class LoadMoviesRetryExecutor
{
    public interface MovieRunnableIOException
    {
        void run(Movie movie) throws IOException;
    }

    protected ThreadPoolExecutor executor;
    private final static int MAX_RETRIES_PER_MOVIE = 3;

    private MovieRunnableIOException getMovieRunnable;

    public LoadMoviesRetryExecutor(ThreadPoolExecutor executor, List<Movie> movies, MovieRunnableIOException getMovieRunnable, boolean ascending)
    {
        this.executor = executor;
        this.getMovieRunnable = getMovieRunnable;

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
            this.getMovieRunnable.run(movie);

            if(movie.getStatus() == null && numOfRetries < MAX_RETRIES_PER_MOVIE)
            {
                numOfRetries++;
                this.executeMovie(movie, numOfMovies, numOfRetries, id);
            }
            else
            {
                this.onMovieLoaded(numOfMovies, id);
            }
        } catch (IOException ignored) {}
    }

    protected abstract void onMovieLoaded(int numOfMovies, int id);
}
