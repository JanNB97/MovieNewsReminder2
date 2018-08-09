package com.yellowbite.movienewsreminder2.tasks.newMovies;

import android.content.Context;
import android.os.AsyncTask;

import com.yellowbite.movienewsreminder2.data.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.data.MedZenFileMan;

public class DelLastAndAddAsyncTask extends AsyncTask<Movie, Void, Void>
{
    private Context context;
    private Runnable onTaskFinished;

    public DelLastAndAddAsyncTask(Context context, Runnable onTaskFinished)
    {
        this.context = context;
        this.onTaskFinished = onTaskFinished;
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
            MyMoviesSortedList.add(this.context, movies[0]);
            MyMoviesSortedList.save(this.context);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        onTaskFinished.run();
    }
}