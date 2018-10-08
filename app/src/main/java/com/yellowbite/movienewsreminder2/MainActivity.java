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
import com.yellowbite.movienewsreminder2.ui.activites.MyMoviesToolbarActivity;
import com.yellowbite.movienewsreminder2.ui.activites.AddMovieActivity;
import com.yellowbite.movienewsreminder2.ui.recyclerView.MyMovieRecyclerView;
import com.yellowbite.movienewsreminder2.ui.activites.NewMoviesActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends MyMoviesToolbarActivity implements LoadedMoviesEvent
{
    // main views
    private MyMovieRecyclerView myMovieRecyclerView;
    private FloatingActionButton addMovieFloatingButton;
    private static final int UNDO_SHOW_TIME = 5000;

    // views for loading time
    private ProgressBar loadingProgressBar;
    private TextView moviesUpdateTextView;

    private Movie lastSwipedMovie;
    private Thread showUndoButtonThread;

    // --- --- --- Initialization --- --- ---
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_main);

        this.findViewsById();
        this.initialize();
    }

    private void initialize()
    {
        this.addMovieFloatingButton.setEnabled(false);

        this.initRecyclerView();

        this.loadMyMovies();

        NewsService.start(this);

        this.launchNewMoviesActivity();
    }

    private void initRecyclerView()
    {
        this.myMovieRecyclerView = new MyMovieRecyclerView(this, R.id.movieRecyclerView,
                MyMoviesSortedList.getInstance(this));

        this.myMovieRecyclerView.setOnSwipeListener(
                (v, d, lastRemovedMovie) -> this.handleOnSwiped(lastRemovedMovie)
        );
        this.myMovieRecyclerView.setOnScrollListener((r, dx, dy) -> {
                if(dy > 0)
                {
                    this.handleScrolledDown();
                }
                else
                {
                    this.handleScrolledUp();
                }
        }
        );
    }

    private void findViewsById()
    {
        this.loadingProgressBar     = this.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView   = this.findViewById(R.id.moviesUpdateTextView);
        this.addMovieFloatingButton = this.findViewById(R.id.addMovieFloatingButton);
    }

    // --- --- --- User interaction with main components --- --- ---
    public void handleOnAddMovieClicked(View view)
    {
        AddMovieActivity.startForResult(this);
    }

    public void handleOnSwiped(Movie swipedMovie)
    {
        this.lastSwipedMovie = swipedMovie;
        this.undoItem.setVisible(true);

        final int UNDO_TIME_PER_INTERVALL = 100;

        if(this.showUndoButtonThread != null)
        {
            this.showUndoButtonThread.interrupt();
        }
        this.showUndoButtonThread = new Thread(() -> {
            try
            {
                for(int i = 0; i <= UNDO_SHOW_TIME; i += UNDO_TIME_PER_INTERVALL)
                {
                    if(this.undoItem.isVisible())
                    {
                        Thread.sleep(UNDO_TIME_PER_INTERVALL);
                    }
                }

                this.runOnUiThread(() -> this.undoItem.setVisible(false));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });

        this.showUndoButtonThread.start();
    }

    // --- --- --- user interactions with recycler view --- --- ---
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

    // --- --- --- user interactions with toolbar --- --- ---
    @Override
    protected void handleOnUndoClicked()
    {
        this.undoItem.setVisible(false);

        if(lastSwipedMovie != null)
        {
            MyMoviesSortedList.getInstance(this).add(this.lastSwipedMovie);
            MyMoviesSortedList.getInstance(this).save();
            this.myMovieRecyclerView.dataSetChanged(false);

            if(this.lastSwipedMovie.isHot())
            {
                HotMoviesSortedList.getInstance(this).add(this.lastSwipedMovie);
            }
        }
    }

    // --- --- --- Load movies --- --- ---
    private void loadMyMovies()
    {
        List<Movie> myMovies = MyMoviesSortedList.getInstance(this).getAll();

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(MyMoviesSortedList.getInstance(this).size());

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
        Collections.sort(MyMoviesSortedList.getInstance(this).getAll());

        MyMoviesSortedList.getInstance(this).loadHotMovies();

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addMovieFloatingButton.setEnabled(true);

        this.myMovieRecyclerView.showItems();
    }

    // --- --- --- launch and handle NewMoviesActivity --- --- ---
    private void launchNewMoviesActivity()
    {
        if(!NewMoviesQueue.getInstance(this).isEmpty())
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
