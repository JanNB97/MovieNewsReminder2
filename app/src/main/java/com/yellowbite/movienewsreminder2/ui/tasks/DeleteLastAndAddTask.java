package com.yellowbite.movienewsreminder2.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;

public class DeleteLastAndAddTask extends AsyncTask<Movie, Void, Void>
{
    private Context context;
    private Runnable runnable;

    public DeleteLastAndAddTask(Context context, Runnable runnable)
    {
        this.context = context;
        this.runnable = runnable;
    }

    @Override
    protected Void doInBackground(Movie... movies)
    {
        if(movies.length > 1)
        {
            throw new IllegalArgumentException();
        }

        MedZenFileMan.deleteLastNewMovie(context);

        if(movies.length != 0 && movies[0] != null)
        {
            MedZenFileMan.addMyMovie(this.context, movies[0]);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        runnable.run();
    }
}
