package com.yellowbite.movienewsreminder2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.newsService.NewsService;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.LoadMyMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.ui.NavigationDrawerActivity;
import com.yellowbite.movienewsreminder2.ui.addMovie.AddMovieActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.MyMovieRecyclerView;
import com.yellowbite.movienewsreminder2.ui.newMovies.NewMoviesActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends NavigationDrawerActivity implements LoadedMoviesEvent
{
    // main views
    private MyMovieRecyclerView myMovieRecyclerView;
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
        this.setTitle("Merkliste");

        this.loadingProgressBar = this.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView = this.findViewById(R.id.moviesUpdateTextView);
        this.addMovieFloatingButton = this.findViewById(R.id.addMovieFloatingButton);
        this.initAddMovieFloatingButton();
        this.myMovieRecyclerView = new MyMovieRecyclerView(this, R.id.movieRecyclerView,
                MyMoviesSortedList.getInstance(),
                this::handleScrolledDown, this::handleScrolledUp);

        this.loadMyMovies();

        NewsService.start(this);

        this.launchNewMoviesActivity();
    }

    private void initAddMovieFloatingButton()
    {
        this.addMovieFloatingButton.setEnabled(false);
        this.addMovieFloatingButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    // --- --- --- User interaction --- --- ---
    private void handleOnAddMovieClicked(View view)
    {
        AddMovieActivity.startForResult(this);
    }

    private void handleScrolledDown()
    {
        if(addMovieFloatingButton.getVisibility() == View.VISIBLE)
        {
            addMovieFloatingButton.hide();
        }
    }

    private void handleScrolledUp()
    {
        if(addMovieFloatingButton.getVisibility() != View.VISIBLE)
        {
            addMovieFloatingButton.show();
        }
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
}
