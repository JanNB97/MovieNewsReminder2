package com.yellowbite.movienewsreminder2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.newsService.NewsService;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.GetMovieAsyncTask;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.LoadMyMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;
import com.yellowbite.movienewsreminder2.ui.addMovie.AddMovieActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.MyMovieRecyclerView;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;

import java.util.Collections;
import java.util.List;

public class MainActivity extends NoTitleBarActivity implements LoadedMoviesEvent
{
    // main views
    private MyMovieRecyclerView myMovieRecyclerView;
    private TextView urlTextView;
    private Button addMovieButton;
    private FloatingActionButton addMovieFloatingButton;

    // views for loading time
    private ProgressBar loadingProgressBar;
    private TextView moviesUpdateTextView;

    // --- --- --- Initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_main);

        this.initialize();
    }

    public void initialize()
    {
        this.loadingProgressBar = this.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView = this.findViewById(R.id.moviesUpdateTextView);
        this.addMovieFloatingButton = this.findViewById(R.id.addMovieFloatingButton);
        this.initAddMovieFloatingButton();
        this.initAddMovieButton();
        this.initURLTextView();
        this.myMovieRecyclerView = new MyMovieRecyclerView(this, R.id.movieRecyclerView,
                MyMoviesSortedList.getInstance());

        this.loadMyMovies();

        NewsService.start(this);

        this.launchNewMoviesActivity();
    }

    private void initAddMovieFloatingButton()
    {
        this.addMovieFloatingButton.setEnabled(false);
        this.addMovieFloatingButton.setOnClickListener(v -> {
            AddMovieActivity.startForResult(this);
        });
    }

    private void initAddMovieButton()
    {
        this.addMovieButton = this.findViewById(R.id.addMovieButton);
        this.addMovieButton.setOnClickListener(v -> this.handleOnAddMovieClicked());
        this.addMovieButton.setVisibility(View.GONE);
    }

    private void initURLTextView()
    {
        this.urlTextView = this.findViewById(R.id.urlTextView);
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
        List<Movie> myMovies = MyMoviesSortedList.getInstance().getAll(this);

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(MyMoviesSortedList.getInstance().size(this));

            // download status
            new LoadMyMoviesRetryExecutor(this, this, myMovies, this::onLoadingFinished);
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
        Collections.sort(MyMoviesSortedList.getInstance().getAll(this));

        HotMoviesSortedList.getMyHotMovies(this);

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.urlTextView.setEnabled(true);
        this.addMovieFloatingButton.setEnabled(true);

        this.myMovieRecyclerView.showItems();
    }

    // --- --- --- Launch and Handle NewMoviesActivity --- --- ---

    private void launchNewMoviesActivity()
    {
        if(!NewMoviesQueue.isEmpty(this))
        {
            NewMoviesActivity.startForResult(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case NewMoviesActivity.REQUEST_CODE:
                this.handleNewMovieResults(resultCode);
                break;
            case AddMovieActivity.REQUEST_CODE:
                this.handleAddMovieResult();
                break;
        }
    }

    private void handleNewMovieResults(int resultCode)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            this.myMovieRecyclerView.dataSetChanged(false);
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            // TODO
        }
    }

    private void handleAddMovieResult()
    {
        this.myMovieRecyclerView.dataSetChanged(false);
    }

    // --- --- --- Interaction with user --- --- ---

    private void handleOnAddMovieClicked()
    {
        Movie movieToAdd = new Movie(urlTextView.getText().toString());

        GetMovieAsyncTask.getMovie(movieToAdd,
            (Movie movie) -> {
                urlTextView.setText("");

                if(movie == null)
                {
                    NotificationMan.showShortToast(this, "Falsche URL oder keine Internetverbindung");
                    return;
                }

                myMovieRecyclerView.addItem(movie, true);
        });
    }
}
