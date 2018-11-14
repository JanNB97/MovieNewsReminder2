package com.yellowbite.movienewsreminder2.tasks.mainActivity;

import android.os.AsyncTask;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.MovieRunnable;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;

public class GetMovieAsyncTask extends AsyncTask<Movie, Void, Movie>
{
    private MovieRunnable onPostExecute;

    public GetMovieAsyncTask(MovieRunnable onPostExecute)
    {
        this.onPostExecute = onPostExecute;
    }

    public static void getMovie(Movie movie, MovieRunnable onPostExecute)
    {
        new GetMovieAsyncTask(onPostExecute).execute(movie);
    }

    @Override
    protected Movie doInBackground(Movie...movies)
    {
        if(movies.length != 1)
        {
            throw new IllegalArgumentException();
        }

        Movie movie = movies[0];

        try
        {
            MedZenMovieSiteScraper.getMovie(movie);
            return movie;
        }
        catch (IllegalArgumentException | IOException ignored)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie movie)
    {
        this.onPostExecute.run(movie);
    }
}
