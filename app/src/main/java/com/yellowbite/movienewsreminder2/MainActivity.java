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
    private FloatingActionButton undoFloatingButton;
    private static final int UNDO_SHOW_TIME = 5000;

    // views for loading time
    private ProgressBar loadingProgressBar;
    private TextView moviesUpdateTextView;

    private Movie lastSwipedMovie;

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
        this.undoFloatingButton = this.findViewById(R.id.undoFloatingButton);
        this.initAddMovieFloatingButton();
        this.initUndoFloatingButton();
        this.myMovieRecyclerView = new MyMovieRecyclerView(this, R.id.movieRecyclerView,
                MyMoviesSortedList.getInstance());

        this.loadMyMovies();

        NewsService.start(this);

        this.launchNewMoviesActivity();
    }

    private void initAddMovieFloatingButton()
    {
        this.addMovieFloatingButton.setEnabled(false);
        this.addMovieFloatingButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    private void initUndoFloatingButton()
    {
        this.undoFloatingButton.hide();
        this.undoFloatingButton.setOnClickListener(this::handleOnUndoClicked);
    }

    // --- --- --- User interaction --- --- ---
    private void handleOnAddMovieClicked(View view)
    {
        AddMovieActivity.startForResult(this);
    }

    public void handleOnSwiped(Movie swipedMovie)
    {
        this.lastSwipedMovie = swipedMovie;
        this.undoFloatingButton.show();

        new Thread(() -> {
            try
            {
                Thread.sleep(UNDO_SHOW_TIME);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            this.undoFloatingButton.hide();
        }).start();
    }

    // --- --- --- user interactions with recycler view --- --- ---
    private void handleOnUndoClicked(View view)
    {
        this.undoFloatingButton.hide();

        if(lastSwipedMovie != null)
        {
            MyMoviesSortedList.getInstance().add(this, this.lastSwipedMovie);
            MyMoviesSortedList.getInstance().save(this);
            this.myMovieRecyclerView.dataSetChanged(false);
        }
    }

    public void handleScrolledDown()
    {
        if(addMovieFloatingButton.getVisibility() == View.VISIBLE)
        {
            addMovieFloatingButton.hide();
        }
    }

    public void handleScrolledUp()
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

    // --- --- --- launch and handle NewMoviesActivity --- --- ---
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
