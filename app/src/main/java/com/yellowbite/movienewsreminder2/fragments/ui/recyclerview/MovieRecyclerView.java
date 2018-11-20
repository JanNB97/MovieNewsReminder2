package com.yellowbite.movienewsreminder2.fragments.ui.recyclerview;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles.SortedHotMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.itemholder.MovieAdapter;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener.ScrollListener;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener.SwipeListener;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener.TouchListener;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener.touchlisteners.RecyclerTouchListener;
import com.yellowbite.movienewsreminder2.fragments.ui.recyclerview.listener.touchlisteners.SwipeCallback;

import java.util.ArrayList;
import java.util.List;

public abstract class MovieRecyclerView extends SwipeCallback
{
    protected Activity activity;
    protected RecyclerView recyclerView;

    protected MovieList movieList;

    protected MovieAdapter movieAdapter;

    protected boolean recentlySwiped = false;
    protected final static int SWIPE_COOLDOWN = 2000;

    protected Movie lastSwipedMovie;

    private List<SwipeListener> swipeListeners;
    private List<TouchListener> itemClickedListeners;
    private List<TouchListener> itemLongClickedListeners;
    private List<ScrollListener> scollListeners;

    // --- --- --- Initialization --- --- ---
    public MovieRecyclerView(Activity activity, @IdRes int id, MovieList movieList,
             boolean isSwipeable, int viewHolderLayout)
    {
        this.activity = activity;

        this.recyclerView = activity.findViewById(id);
        this.recyclerView.setHasFixedSize(true);

        this.movieList = movieList;

        this.initLayout();
        this.initTouchSwipeScrollListener(isSwipeable);

        this.movieAdapter = new MovieAdapter(this.activity, movieList, viewHolderLayout);
    }

    private void initLayout()
    {
        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    private void initTouchSwipeScrollListener(boolean isSwipeable)
    {
        this.swipeListeners = new ArrayList<>();
        this.itemClickedListeners = new ArrayList<>();
        this.itemLongClickedListeners = new ArrayList<>();
        this.scollListeners = new ArrayList<>();

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
        this.lastSwipedMovie = movieList.get(position);
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

        this.callAllSwipeListeners(viewHolder, direction);
    }

    protected void handleClickedOnMovieItem(View view, int position)
    {
        this.callAllTouchedListeners(this.itemClickedListeners, view, position);
    }

    protected void handleClickedLongOnMovieItem(View view, int position)
    {
        this.callAllTouchedListeners(this.itemLongClickedListeners, view, position);
    }

    protected void handleOnScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        this.callAllScrollListeners(recyclerView, dx, dy);
    }

    protected void handleOnScrollStateChanged(RecyclerView recyclerView, int newState){}

    // --- --- --- listeners --- --- ---
    public void setOnSwipeListener(SwipeListener swipeListener)
    {
        this.swipeListeners.add(swipeListener);
    }

    private void callAllSwipeListeners(RecyclerView.ViewHolder viewHolder, int direction)
    {
        for(SwipeListener swipeListener : swipeListeners)
        {
            swipeListener.onSwipe(viewHolder, direction, this.lastSwipedMovie);
        }
    }

    public void setOnClickedListener(TouchListener touchListener)
    {
        this.itemClickedListeners.add(touchListener);
    }

    public void setOnLongClickedListener(TouchListener touchListener)
    {
        this.itemLongClickedListeners.add(touchListener);
    }

    private void callAllTouchedListeners(List<TouchListener> touchListeners, View view, int position)
    {
        for(TouchListener touchListener : touchListeners)
        {
            touchListener.onTouch(view, position);
        }
    }

    public void setOnScrollListener(ScrollListener scrollListener)
    {
        this.scollListeners.add(scrollListener);
    }

    private void callAllScrollListeners(RecyclerView recyclerView, int dx, int dy)
    {
        for(ScrollListener scrollListener : this.scollListeners)
        {
            scrollListener.handleScroll(recyclerView, dx, dy);
        }
    }

    // --- --- --- manage items --- --- ---
    public void showItems()
    {
        this.recyclerView.setAdapter(movieAdapter);
    }

    public void addItems(List<Movie> movies)
    {
        this.movieList.addAll(movies);
        this.dataSetChanged();
    }

    public void addItem(Movie movie)
    {
        if(!this.movieList.add(movie))
        {
            NotificationMan.showShortToast(this.activity, movie.getTitel() + " is already in the database");
        }
        else
        {
            this.dataSetChanged();
        }
    }

    public void removeItem(int position)
    {
        Movie movieToRemove = this.movieList.get(position);
        if(movieToRemove.isHot())
        {
            SortedHotMovieList.getInstance(this.activity).delete(movieToRemove);
        }

        this.movieList.remove(position);
        this.notifyItemRemoved(position);
    }

    public void notifyItemRemoved(int position)
    {
        this.movieAdapter.notifyItemRemoved(position);
    }

    public void dataSetChanged()
    {
        this.movieAdapter.notifyDataSetChanged();
    }
}