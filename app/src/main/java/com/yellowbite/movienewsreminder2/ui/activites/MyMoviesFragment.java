package com.yellowbite.movienewsreminder2.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MySortedMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.NewMovieQueue;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.SortedHotMovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.newsService.NewsService;
import com.yellowbite.movienewsreminder2.tasks.functionalInterfaces.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainActivity.LoadMyMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.ui.recyclerView.MyMovieRecyclerView;

import java.util.Collections;
import java.util.List;

public class MyMoviesFragment extends Fragment implements LoadedMoviesEvent
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

    private MenuItem undoItem;

    private Runnable readyRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_my_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById(view);
        this.addMovieFloatingButton.setEnabled(false);

        if(this.readyRunnable != null)
        {
            this.readyRunnable.run();
        }
        else
        {
            this.readyRunnable = () -> {};
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void start()
    {
        this.initRecyclerView();

        this.loadMyMovies();

        NewsService.start(super.getActivity());

        this.launchNewMoviesActivity();
    }

    private void initRecyclerView()
    {
        this.myMovieRecyclerView = new MyMovieRecyclerView(super.getActivity(), R.id.movieRecyclerView,
                MySortedMovieList.getInstance(super.getContext()));

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

    private void findViewsById(View view)
    {
        this.loadingProgressBar     = view.findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView   = view.findViewById(R.id.moviesUpdateTextView);
        this.addMovieFloatingButton = view.findViewById(R.id.addMovieFloatingButton);
    }

    // --- --- --- User interaction with main components --- --- ---
    public void handleOnAddMovieClicked(View view)
    {
        AddMovieActivity.startForResult(this.getActivity());
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

                super.getActivity().runOnUiThread(() -> this.undoItem.setVisible(false));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });

        this.showUndoButtonThread.start();
    }

    // --- --- --- user interactions with toolbar --- --- ---
    public void handleOnUndoClicked()
    {
        this.undoItem.setVisible(false);

        if(lastSwipedMovie != null)
        {
            MySortedMovieList.getInstance(super.getContext()).add(this.lastSwipedMovie);
            this.myMovieRecyclerView.dataSetChanged();

            if(this.lastSwipedMovie.isHot())
            {
                SortedHotMovieList.getInstance(super.getContext()).add(this.lastSwipedMovie);
            }
        }
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

    // --- --- --- Load movies --- --- ---
    private void loadMyMovies()
    {
        List<Movie> myMovies = MySortedMovieList.getInstance(super.getContext()).getAll();

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(MySortedMovieList.getInstance(super.getContext()).size());

            // download status
            new LoadMyMoviesRetryExecutor(super.getActivity(), this, myMovies, this::onLoadingFinished);
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
        Collections.sort(MySortedMovieList.getInstance(super.getContext()).getAll());

        MySortedMovieList.getInstance(super.getContext()).loadHotMovies();

        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addMovieFloatingButton.setEnabled(true);

        this.myMovieRecyclerView.showItems();
    }

    // --- --- --- launch and handle NewMoviesActivity --- --- ---
    private void launchNewMoviesActivity()
    {
        if(!NewMovieQueue.getInstance(super.getContext()).isEmpty())
        {
            NewMoviesActivity.startForResult(super.getActivity());
        }
    }

    public MyMovieRecyclerView getMyMovieRecyclerView()
    {
        return myMovieRecyclerView;
    }

    public void setUndoItem(MenuItem undoItem)
    {
        this.undoItem = undoItem;
    }
}
