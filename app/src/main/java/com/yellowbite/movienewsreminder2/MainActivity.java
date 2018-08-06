package com.yellowbite.movienewsreminder2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.ui.tasks.GetMoviesTask;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.tasks.LoadMoviesTask;
import com.yellowbite.movienewsreminder2.ui.tasks.MovieRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView movieRecyclerView;
    private TextView urlTextView;
    private Button addMovieButton;

    private List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.removeTitleBar();
        setContentView(R.layout.activity_main);

        this.launchNewMoviesActivity();

        this.myMovies = new ArrayList<>();

        this.initAddMovieButton();
        this.initURLTextView();
        this.initRecyclerView();

        this.loadMyMovies();

        NewsService.start(this);
    }

    // --- --- --- Open NewMoviesActivity --- --- ---

    private void launchNewMoviesActivity()
    {
        Intent intent = new Intent(this, NewMoviesActivity.class);
        this.startActivity(intent);
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

    private void initAddMovieButton()
    {
        this.addMovieButton = findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    private void initURLTextView()
    {
        this.urlTextView = findViewById(R.id.urlTextView);
        this.urlTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                addMovieButton.setEnabled(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
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
        new LoadMoviesTask(this, this.movieRecyclerView, this.urlTextView).execute(myMovies);
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked(View view)
    {
        Context context = this;
        new GetMoviesTask(new MovieRunnable()
        {
            @Override
            public void run(Movie movie)
            {
                urlTextView.setText("");

                if(movie == null)
                {
                    NotificationMan.showShortToast(context, "Falsche URL oder keine Internetverbindung");
                    return;
                }

                ((MovieAdapter)movieRecyclerView.getAdapter()).addItem(movie);
            }
        })
        .execute(new Movie(urlTextView.getText().toString()));
    }
}
