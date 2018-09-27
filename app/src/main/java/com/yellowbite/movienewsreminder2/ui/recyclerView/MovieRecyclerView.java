package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.recyclerView.itemHolder.MovieAdapter;
import com.yellowbite.movienewsreminder2.ui.recyclerView.touchListeners.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.ui.recyclerView.touchListeners.SwipeCallback;

import java.util.List;

public abstract class MovieRecyclerView extends SwipeCallback
{
    protected AppCompatActivity activity;
    protected RecyclerView recyclerView;

    protected MovieList movieList;

    protected MovieAdapter movieAdapter;

    protected boolean recentlySwiped = false;
    protected final static int SWIPE_COOLDOWN = 1000;

    // --- --- --- Initialization --- --- ---
    public MovieRecyclerView(AppCompatActivity activity, @IdRes int id, MovieList movieList,
             boolean isSwipeable)
    {
        this.activity = activity;

        this.recyclerView = activity.findViewById(id);
        this.recyclerView.setHasFixedSize(true);

        this.movieList = movieList;

        this.initLayout();
        this.initTouchSwipeScrollListener(isSwipeable);

        this.movieAdapter = new MovieAdapter(this.activity, movieList);
    }

    private void initLayout()
    {
        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    private void initTouchSwipeScrollListener(boolean isSwipeable)
    {
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                handleOnScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                handleOnScrollStateChanged(recyclerView, newState);
            }
        });

        // register touch listener
        if(isSwipeable)
        {
            new ItemTouchHelper(this).attachToRecyclerView(this.recyclerView);
        }

        // register swipe listener
        this.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.activity, this.recyclerView, new RecyclerTouchListener.ClickListener() {
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
    }

    // --- --- --- handle user interaction --- --- ---
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();
        this.removeItem(position);

        recentlySwiped = true;
        new Thread(() -> {
            try
            {
                Thread.sleep(SWIPE_COOLDOWN);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            this.recentlySwiped = false;
        }).start();
    }

    protected void handleClickedOnMovieItem(View view, int position){}

    protected void handleClickedLongOnMovieItem(View view, int position){}

    protected void handleOnScrolled(RecyclerView recyclerView, int dx, int dy){}

    protected void handleOnScrollStateChanged(RecyclerView recyclerView, int newState){}

    // --- --- --- manage items --- --- ---
    public void showItems()
    {
        this.recyclerView.setAdapter(movieAdapter);
    }

    public void addItems(List<Movie> movies, boolean saveInFile)
    {
        this.movieList.addAll(this.activity, movies);
        this.dataSetChanged(saveInFile);
    }

    public void addItem(Movie movie, boolean saveInFile)
    {
        if(!this.movieList.add(activity, movie))
        {
            NotificationMan.showShortToast(this.activity, movie.getTitel() + " is already in the database");
        }
        else
        {
            // no movie added
            this.dataSetChanged(saveInFile);
        }
    }

    public void removeItem(int position)
    {
        Movie movieToRemove = this.movieList.get(this.activity, position);
        if(movieToRemove.isHot())
        {
            HotMoviesSortedList.deleteSave(this.activity, movieToRemove);
        }

        this.movieList.remove(this.activity, position);
        this.dataSetChanged(true);
    }

    public void dataSetChanged(boolean saveInFile)
    {
        movieAdapter.notifyDataSetChanged();

        if(saveInFile)
        {
            new Thread(() -> this.movieList.save(this.activity))
                    .start();
        }
    }
}
