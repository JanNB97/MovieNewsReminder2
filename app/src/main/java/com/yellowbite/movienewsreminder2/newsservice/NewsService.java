package com.yellowbite.movienewsreminder2.newsservice;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import com.yellowbite.movienewsreminder2.newsservice.messages.WebScraperMessage;
import com.yellowbite.movienewsreminder2.notifications.NotificationMan;

import java.util.List;
import java.util.logging.Logger;

public class NewsService extends JobService
{
    public static final int TIME_UNTIL_NEXT_UPDATE = 1000 * 60 * 30;

    public static void start(Activity app)
    {
        ComponentName name = new ComponentName(app, NewsService.class);
        JobInfo job = (new JobInfo.Builder(0, name))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(TIME_UNTIL_NEXT_UPDATE)
                .build();

        JobScheduler jobScheduler = (JobScheduler)app.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if(jobScheduler != null)
        {
            jobScheduler.schedule(job);
        }
        else
        {
            Logger.getGlobal().severe("jobScheduler is null");
        }
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        new Thread(() -> {
            checkWebscrapers();
            jobFinished(jobParameters, false);
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        return false;
    }

    private void checkWebscrapers()
    {
        List<WebScraperMessage> webScraperMessages = WebscrapingHandler.getWebScraperMessages(getApplicationContext());

        for(WebScraperMessage webScraperMessage : webScraperMessages)
        {
            NotificationMan.showNotification(getApplicationContext(), webScraperMessage);
        }
    }
}
