package com.yellowbite.movienewsreminder2.ui.tasks;

import android.os.AsyncTask;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;

public class GetMoviesTask extends AsyncTask<Movie, Void, Movie>
{
    private MovieRunnable onPostExecute;

    public GetMoviesTask(MovieRunnable onPostExecute)
    {
        this.onPostExecute = onPostExecute;
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
