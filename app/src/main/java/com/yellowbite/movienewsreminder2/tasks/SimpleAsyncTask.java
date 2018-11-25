package com.yellowbite.movienewsreminder2.tasks;

import android.os.AsyncTask;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void>
{
    private Runnable doInBackground;
    private Runnable onPostExecute;

    public SimpleAsyncTask(Runnable doInBackground, Runnable onPostExecute)
    {
        this.doInBackground = doInBackground;
        this.onPostExecute = onPostExecute;
    }

    public static void runSimpleAsyncTask(Runnable doInBackground, Runnable onPostExecute)
    {
        new SimpleAsyncTask(doInBackground, onPostExecute).execute();
    }

    @Override
    protected Void doInBackground(Void[] objects)
    {
        this.doInBackground.run();
        return null;
    }

    @Override
    protected void onPostExecute(Void o)
    {
        this.onPostExecute.run();
    }
}
