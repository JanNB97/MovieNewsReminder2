package com.yellowbite.movienewsreminder2.tasks;

import android.os.AsyncTask;

public class SimpleAsyncTask extends AsyncTask
{
    private Runnable doInBackground;
    private Runnable onPostExecute;

    public SimpleAsyncTask(Runnable doInBackground, Runnable onPostExecute)
    {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
    }

    public static void runSimpleAsynTask(Runnable doInBackground, Runnable onPostExecute)
    {
        new SimpleAsyncTask(doInBackground, onPostExecute).execute();
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        this.doInBackground.run();
        return null;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        this.onPostExecute.run();
    }
}
