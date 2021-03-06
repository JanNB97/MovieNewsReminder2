package com.yellowbite.movienewsreminder2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.NewMovieQueue;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.util.CountDownThread;
import com.yellowbite.movienewsreminder2.newsservice.NewsService;
import com.yellowbite.movienewsreminder2.tasks.functionalinterfaces.LoadedMoviesEvent;
import com.yellowbite.movienewsreminder2.tasks.mainactivity.LoadMyMoviesRetryExecutor;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerviews.MyMovieRecyclerView;

import java.util.Collections;
import java.util.List;

public class MyMoviesFragment extends ToolbarFragment implements LoadedMoviesEvent
{
    public final static int FRAGMENT_ID = 0;

    private boolean firstLaunch = true;

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
    public MyMoviesFragment()
    {
        super(FRAGMENT_ID, R.layout.fragment_my_movies, 0, "Merkliste");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        this.findViewsById();
        this.addMovieFloatingButton.setEnabled(false);

        super.onViewCreated(view, savedInstanceState);
    }

    private void findViewsById()
    {
        this.loadingProgressBar     = super.getView().findViewById(R.id.loadingProgressBar);
        this.moviesUpdateTextView   = super.getView().findViewById(R.id.moviesUpdateTextView);
        this.addMovieFloatingButton = super.getView().findViewById(R.id.addMovieFloatingButton);
        this.addMovieFloatingButton.setOnClickListener(this::handleOnAddMovieClicked);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        this.initRecyclerView();

        if(this.firstLaunch)
        {
            this.firstLaunch = false;
            this.loadMyMovies();
            this.launchNewMoviesActivity();
        }
        else
        {
            this.setLoadedState();
        }

        super.onActivityCreated(savedInstanceState);
    }

    private void initRecyclerView()
    {
        this.myMovieRecyclerView = new MyMovieRecyclerView(super.getActivity(), R.id.movieRecyclerView,
                SortedMyMoviesList.getInstance(super.getContext()));

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

    // --- --- --- Modify toolbar --- --- ---
    @Override
    protected void modifyOptionsMenu()
    {
        super.modifyOptionsMenu();
        this.homeItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_undo:
                this.handleOnUndoClicked();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- --- --- Load movies --- --- ---
    private void loadMyMovies()
    {
        List<Movie> myMovies = SortedMyMoviesList.getInstance(super.getContext()).getAll();

        if(!myMovies.isEmpty())
        {
            this.loadingProgressBar.setMax(SortedMyMoviesList.getInstance(super.getContext()).size());

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
        Collections.sort(SortedMyMoviesList.getInstance(super.getContext()).getAll());
        this.setLoadedState();
    }

    private void setLoadedState()
    {
        this.loadingProgressBar.setVisibility(View.GONE);
        this.moviesUpdateTextView.setVisibility(View.GONE);
        this.addMovieFloatingButton.setEnabled(true);

        this.myMovieRecyclerView.showItems();
    }

    // --- --- --- User interactions --- --- ---
    // interactions with toolbar
    public void handleOnUndoClicked()
    {
        super.undoItem.setVisible(false);

        if(lastSwipedMovie != null)
        {
            SortedMyMoviesList.getInstance(super.getContext()).add(this.lastSwipedMovie);
            this.myMovieRecyclerView.dataSetChanged();
        }
    }

    // interactions with main components
    public void handleOnAddMovieClicked(View view)
    {
        FragmentMaster.sendShowFragmentRequest(this, this.getContext(),
                AddMovieFragment.FRAGMENT_ID);
    }

    public void handleOnSwiped(Movie swipedMovie)
    {
        this.lastSwipedMovie = swipedMovie;
        super.undoItem.setVisible(true);

        this.showUndoButtonThread = new CountDownThread(UNDO_SHOW_TIME,
                () -> !super.undoItem.isVisible(),
                () -> super.getActivity().runOnUiThread(() -> super.undoItem.setVisible(false))
        );

        this.showUndoButtonThread.start();
    }

    // interactions with recycler view
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

    // --- --- --- Launch and handle NewMoviesFragment --- --- ---
    private void launchNewMoviesActivity()
    {
        if(!NewMovieQueue.getInstance(super.getContext()).isEmpty())
        {
            FragmentMaster.sendShowFragmentRequest(this, this.getContext(),
                    NewMoviesFragment.FRAGMENT_ID);
        }
        else
        {
            NewsService.start(super.getActivity());
        }
    }

    @Override
    public void onShowFragmentRequestSent(int fragmentId)
    {
        super.onShowFragmentRequestSent(fragmentId);
        if(this.showUndoButtonThread != null)
        {
            this.showUndoButtonThread.interrupt();
        }
    }

    // --- --- --- Getter and Setter --- --- ---
    public MyMovieRecyclerView getMyMovieRecyclerView()
    {
        return myMovieRecyclerView;
    }
}
