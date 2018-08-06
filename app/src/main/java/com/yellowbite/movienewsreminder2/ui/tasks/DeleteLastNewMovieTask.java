package com.yellowbite.movienewsreminder2.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesController;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;

public class DeleteLastNewMovieTask extends AsyncTask<Void, Void, Void>
{
    Context context;
    Runnable runnable;

    public DeleteLastNewMovieTask(Context context, Runnable runnable)
    {
        this.context = context;
        this.runnable = runnable;
    }

    @Override
    protected Void doInBackground(Void... strings)
    {
        MedZenFileMan.deleteLastNewMovie(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        runnable.run();
    }
}
