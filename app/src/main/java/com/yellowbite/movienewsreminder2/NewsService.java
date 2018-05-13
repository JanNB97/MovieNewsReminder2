package com.yellowbite.movienewsreminder2;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHandler;

import java.util.ArrayList;

public class NewsService extends JobService
{
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        System.out.println("Checks newswebsites...");
        new Thread(() -> {
            checkWebscrapers();
            jobFinished(jobParameters, false);
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void checkWebscrapers()
    {
        ArrayList<WebscrapingHandler> updatedWebscrapers = WebscrapingHandler.getUpdatedWebscrapers(getApplicationContext());

        if(updatedWebscrapers.size() == 1)
        {
            showNotification(updatedWebscrapers.get(0).getName() + " has been updated", "", R.drawable.ic_launcher_background);
        }
        else if(updatedWebscrapers.size() > 1)
        {
            StringBuilder builder = new StringBuilder();

            for(WebscrapingHandler webscrapingHandler : updatedWebscrapers)
            {
                builder.append(webscrapingHandler.getName() + "\n");
            }

            showNotification("The following websites have been updated:", builder.toString(), R.drawable.ic_launcher_background);
        }
    }

    private void showNotification(String title, String text, int icon)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }
}
