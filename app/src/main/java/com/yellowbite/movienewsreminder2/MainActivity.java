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
    private RecyclerView.LayoutManager layoutManager;

    private TextView urlTextView;

    private List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        this.myMovies = new ArrayList<>();

        Button addMovieButton = findViewById(R.id.addMovieButton);
        addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);

        this.urlTextView = findViewById(R.id.urlTextView);

        this.initRecyclerView();
        this.loadMyMovies();

        NewsService.start(this);
    }

    private void initRecyclerView()
    {
        this.movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
        this.movieRecyclerView.setHasFixedSize(true);
        this.movieRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), this.movieRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
                handleClickedOnMovieItem(view, position);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                handleClickedLongOnMovieItem(view, position);
            }
        }));

        // use linear layout manager
        this.layoutManager = new LinearLayoutManager(this);
        this.movieRecyclerView.setLayoutManager(layoutManager);
    }

    private void handleClickedOnMovieItem(View view, int position)
    {

    }

    private void handleClickedLongOnMovieItem(View view, int position)
    {
        // TODO - Mark movie as hot
        Movie movie = myMovies.get(position);
        NotificationMan.showShortToast(this, movie.getTitel() + " is selected!");
    }

    private void removeTitleBar()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar = getSupportActionBar(); //hide the title bar
        if(actionBar != null)
        {
            actionBar.hide();
        }
    }

    private void handleOnAddMovieClicked(View view)
    {
        new AddMovieTask(this, (MovieAdapter)this.movieRecyclerView.getAdapter(), this.urlTextView)
                .execute(urlTextView.getText().toString());
    }

    private void loadMyMovies()
    {
        new LoadMoviesTask(this, this.movieRecyclerView).execute(myMovies);
    }
}
