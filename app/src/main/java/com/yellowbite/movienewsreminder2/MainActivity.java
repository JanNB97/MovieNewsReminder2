package com.yellowbite.movienewsreminder2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComponentName name = new ComponentName(this, NewsService.class);
        JobInfo job = (new JobInfo.Builder(0, name))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(1000 * 60 * 30)
                .build();

        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if(jobScheduler != null)
        {
            jobScheduler.schedule(job);
        }
        else
        {
            Logger.getGlobal().severe("jobScheduler is null");
        }
    }
}
