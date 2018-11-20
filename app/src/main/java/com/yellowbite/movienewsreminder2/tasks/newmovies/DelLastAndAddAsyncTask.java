package com.yellowbite.movienewsreminder2.tasks.newmovies;

import android.content.Context;
import android.os.AsyncTask;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles.MySortedMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles.NewMovieQueue;
import com.yellowbite.movienewsreminder2.data.Movie;

public class DelLastAndAddAsyncTask extends AsyncTask<Movie, Void, Void>
{
    private Context context;
    private Runnable onTaskFinished;

    public DelLastAndAddAsyncTask(Context context, Runnable onTaskFinished)
    {
        this.context = context;
        this.onTaskFinished = onTaskFinished;
    }

    public static void delLastAndAdd(Context context, Movie movie, Runnable onTaskFinished)
    {
        new DelLastAndAddAsyncTask(context, onTaskFinished).execute(movie);
    }

    public static void delLast(Context context, Runnable onTaskFinished)
    {
        delLastAndAdd(context, null, onTaskFinished);
    }

    @Override
    protected Void doInBackground(Movie... movies)
    {
        if(movies.length > 1)
        {
            throw new IllegalArgumentException();
        }

        NewMovieQueue.getInstance(this.context).removeLast();

        if(movies.length != 0 && movies[0] != null)
        {
            MySortedMovieList.getInstance(this.context).add(movies[0]);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        onTaskFinished.run();
    }
}
