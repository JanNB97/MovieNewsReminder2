package com.yellowbite.movienewsreminder2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.MovieAdapter;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView movieRecyclerView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView urlTextView;

    private List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        Button addMovieButton = findViewById(R.id.addMovieButton);
        addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);

        urlTextView = findViewById(R.id.urlTextView);

        movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);

        // use linear layout manager
        layoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(layoutManager);

        // specify adapter
        movieAdapter = new MovieAdapter(this.loadMyMovies());
        movieRecyclerView.setAdapter(movieAdapter);

        NewsService.start(this);
    }

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
    }

    private void handleOnAddMovieClicked(View view)
    {
        String url = urlTextView.getText().toString();
        Movie movie;
        try
        {
            movie = MedZenMovieSiteScraper.getMovie(url);
        } catch (IOException ignored)
        {
            return;
        }
        finally
        {
            urlTextView.setText("");
        }

        myMovies.add(movie);
        Collections.sort(myMovies);
        this.movieAdapter.notifyDataSetChanged();
        MedZenFileMan.setMyMovies(this, myMovies);
    }

    private List<Movie> loadMyMovies()
    {
        myMovies = MedZenFileMan.getMyMovies(this);
        MedZenMovieSiteScraper.getMovies(myMovies);
        return myMovies;
    }
}
