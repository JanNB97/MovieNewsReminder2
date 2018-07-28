package com.yellowbite.movienewsreminder2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.MovieAdapter;

import java.util.Objects;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView movieRecyclerView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);

        // use linear layout manager
        layoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(layoutManager);

        // specify adapter
        movieAdapter = new MovieAdapter(new String []{"Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5",
                "Test 1", "Test 2", "Test 3", "Test 4", "Test 5"});
        movieRecyclerView.setAdapter(movieAdapter);

        this.startNewsJob();
    }

    private void startNewsJob()
    {
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
