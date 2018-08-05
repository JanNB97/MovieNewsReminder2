package com.yellowbite.movienewsreminder2.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.NotificationMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;

public class AddMovieTask extends AsyncTask<String, Void, Movie>
{
    private Context context;
    private MovieAdapter movieAdapter;

    private TextView urlTextView;

    public AddMovieTask(Context context, MovieAdapter movieAdapter, TextView urlTextView)
    {
        this.context = context;
        this.movieAdapter = movieAdapter;

        this.urlTextView = urlTextView;
    }

    @Override
    protected Movie doInBackground(String... strings)
    {
        if(strings.length != 1)
        {
            return null;
        }

        String url = strings[0];
        try
        {
            return MedZenMovieSiteScraper.getMovie(url);
        } catch (IOException e)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie movie)
    {
        urlTextView.setText("");

        if(movie == null)
        {
            NotificationMan.showShortToast(this.context, "Falsche URL oder keine Internetverbindung");
            return;
        }

        this.movieAdapter.addItem(movie);
    }
}
