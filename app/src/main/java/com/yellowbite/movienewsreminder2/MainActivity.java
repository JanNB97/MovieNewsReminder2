package com.yellowbite.movienewsreminder2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.tasks.AddMovieTask;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.ui.tasks.LoadMoviesTask;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenFileMan;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView movieRecyclerView;
    private TextView urlTextView;
    private Button addMovieButton;
    private ProgressBar loadingProgressBar;

    private List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        this.myMovies = new ArrayList<>();

        this.addMovieButton = findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);

        this.urlTextView = findViewById(R.id.urlTextView);
        this.loadingProgressBar = findViewById(R.id.loadingProgressBar);

        this.initRecyclerView();
        this.loadMyMovies();

        NewsService.start(this);
    }

    // --- --- --- Initialization of UI --- --- ---

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar = getSupportActionBar(); //hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }

    private void initRecyclerView()
    {
        this.movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        this.movieRecyclerView.setHasFixedSize(true);

        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.movieRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadMyMovies()
    {
        new LoadMoviesTask(this, this.movieRecyclerView, this.loadingProgressBar, this.addMovieButton).execute(myMovies);
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked(View view)
    {
        new AddMovieTask(this, (MovieAdapter)this.movieRecyclerView.getAdapter(), this.urlTextView)
                .execute(urlTextView.getText().toString());
    }
}
