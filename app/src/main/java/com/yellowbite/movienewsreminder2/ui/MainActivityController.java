package com.yellowbite.movienewsreminder2.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.data.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.news.NewsService;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.tasks.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.GetMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.MovieRunnable;
import com.yellowbite.movienewsreminder2.data.MedZenFileMan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivityController implements LoadedMoviesEvent
{
    private MainActivity mainActivity;

    // main views
    private RecyclerView movieRecyclerView;
    private TextView urlTextView;
    private Button addMovieButton;

    // views for loading time
    private ProgressBar loadingProgressBar;
    private TextView moviesUpdateTextView;

    public MainActivityController(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;

        this.loadingProgressBar = this.mainActivity.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView = this.mainActivity.findViewById(R.id.moviesUpdateTextView);
        this.initAddMovieButton();
        this.initURLTextView();
        this.initRecyclerView();

        this.loadMyMovies();

        NewsService.start(this.mainActivity);
    }

    // --- --- --- Initialization of UI --- --- ---

    private void initAddMovieButton()
    {
        this.addMovieButton = this.mainActivity.findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    private void initURLTextView()
    {
        this.urlTextView = this.mainActivity.findViewById(R.id.urlTextView);
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
        this.movieRecyclerView = (RecyclerView) this.mainActivity.findViewById(R.id.movieRecyclerView);
        this.movieRecyclerView.setHasFixedSize(true);

        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.mainActivity);
        this.movieRecyclerView.setLayoutManager(layoutManager);
    }

    // --- --- --- Load movies --- --- ---

    private void loadMyMovies()
    {
        List<Movie> myMovies = MyMoviesSortedList.get(this.mainActivity);

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(MyMoviesSortedList.size(this.mainActivity));

            // download status
            new GetMoviesRetryExecutor(this.mainActivity, this, myMovies,
                    this::onLoadingFinished);
        }
        else
        {
            this.onLoadingFinished();
        }
    }

    @Override
    public void loadedMovies(int numOfMovies)
    {
        this.loadingProgressBar.setProgress(numOfMovies);
    }

    private void onLoadingFinished()
    {
        Collections.sort(MyMoviesSortedList.get(this.mainActivity));

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addAdapterToRecyclerView();
        this.urlTextView.setEnabled(true);
    }

    private void addAdapterToRecyclerView()
    {
        // specify adapter
        MovieAdapter movieAdapter = new MovieAdapter(this.mainActivity);
        this.movieRecyclerView.setAdapter(movieAdapter);

        new ItemTouchHelper(new SwipeCallback(movieAdapter)).attachToRecyclerView(this.movieRecyclerView);

        this.movieRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.mainActivity, this.movieRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position)
            {
                movieAdapter.handleClickedOnMovieItem(view, position);
            }

            @Override
            public void onLongClick(View view, int position)
            {
                movieAdapter.handleClickedLongOnMovieItem(view, position);
            }
        }));
    }

    // --- --- --- Handle NewMoviesActivity results --- --- ---

    public void onNewMoviesActivityResult(int requestCode, int expectedRequestCode, int resultCode, Intent data)
    {
        if(requestCode == expectedRequestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra("result");
                List<Movie> resultMovies = MedZenFileMan.toStatusMovies(result);
                if(resultMovies != null && !resultMovies.isEmpty())
                {
                    ((MovieAdapter)movieRecyclerView.getAdapter()).addItems(resultMovies, false);
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                // TODO
            }
        }
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked(View view)
    {
        Context context = this.mainActivity;
        new GetMovieAsyncTask(new MovieRunnable()
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

                ((MovieAdapter)movieRecyclerView.getAdapter()).addItem(movie, true);
            }
        }).execute(new Movie(urlTextView.getText().toString()));
    }
}
