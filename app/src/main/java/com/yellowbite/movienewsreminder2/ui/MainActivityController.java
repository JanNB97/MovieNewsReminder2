package com.yellowbite.movienewsreminder2.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
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
import com.yellowbite.movienewsreminder2.files.data.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.data.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.data.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.newsService.NewsService;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.recycler.MovieRecyclerView;
import com.yellowbite.movienewsreminder2.ui.recycler.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recycler.SwipeCallback;
import com.yellowbite.movienewsreminder2.tasks.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.LoadMyMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.tasks.MovieRunnable;

import java.util.Collections;
import java.util.List;

public class MainActivityController implements LoadedMoviesEvent
{
    private MainActivity mainActivity;

    // main views
    private MovieRecyclerView movieRecyclerView;
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
        this.movieRecyclerView = new MovieRecyclerView(mainActivity, R.id.movieRecyclerView);

        this.loadMyMovies();

        NewsService.start(this.mainActivity);

        this.launchNewMoviesActivity();
    }

    // --- --- --- Initialization of UI --- --- ---

    private void initAddMovieButton()
    {
        this.addMovieButton = this.mainActivity.findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(v -> this.handleOnAddMovieClicked());
        this.addMovieButton.setVisibility(View.GONE);
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
                if(charSequence.length() != 0)
                {
                    addMovieButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    addMovieButton.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // --- --- --- Load movies --- --- ---

    private void loadMyMovies()
    {
        List<Movie> myMovies = MyMoviesSortedList.getAll(this.mainActivity);

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(MyMoviesSortedList.size(this.mainActivity));

            // download status
            new LoadMyMoviesRetryExecutor(this.mainActivity, this, myMovies, this::onLoadingFinished);
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
        Collections.sort(MyMoviesSortedList.getAll(this.mainActivity));

        HotMoviesSortedList.getMyHotMovies(this.mainActivity);

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addAdapterToRecyclerView();
        this.urlTextView.setEnabled(true);
    }

    private void addAdapterToRecyclerView()
    {
        movieRecyclerView.addAdapter();
    }

    // --- --- --- Launch and Handle NewMoviesActivity --- --- ---

    private void launchNewMoviesActivity()
    {
        if(!NewMoviesQueue.isEmpty(mainActivity))
        {
            NewMoviesActivity.startForResult(this.mainActivity, MainActivity.REQUEST_CODE);
        }
    }

    public void onNewMoviesActivityResult(int requestCode, int expectedRequestCode, int resultCode, Intent data)
    {
        if(requestCode == expectedRequestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                this.movieRecyclerView.dataSetChanged(false);
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                // TODO
            }
        }
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked()
    {
        Context context = this.mainActivity;
        new GetMovieAsyncTask(movie -> {
            urlTextView.setText("");

            if(movie == null)
            {
                NotificationMan.showShortToast(context, "Falsche URL oder keine Internetverbindung");
                return;
            }

            movieRecyclerView.addItem(movie, true);
        }).execute(new Movie(urlTextView.getText().toString()));
    }
}
