package com.yellowbite.movienewsreminder2.tasks;

import android.content.res.ObbInfo;
import android.os.AsyncTask;

import java.util.concurrent.Callable;

public class AsyncTaskReturnValue<T> extends AsyncTask<Object, Void, T>
{
    private Callable<T> doInBackground;
    private OnPostExecutable<T> onPostExecute;

    public interface OnPostExecutable<T>
    {
        void execute(T result);
    }

    public AsyncTaskReturnValue(Callable<T> doInBackground, OnPostExecutable<T> onPostExecute)
    {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
    }

    public static <T> void executeReturnValue(Callable<T> doInBackground, OnPostExecutable<T> onPostExecute)
    {
        new AsyncTaskReturnValue(doInBackground, onPostExecute).execute();
    }

    @Override
    protected T doInBackground(Object[] objects)
    {
        try
        {
            return this.doInBackground.call();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(T result)
    {
        this.onPostExecute.execute(result);
    }
}
