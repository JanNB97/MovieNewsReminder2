package com.yellowbite.movienewsreminder2.ui.mainActivity.recycler;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datastructures.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datastructures.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.mainActivity.recycler.touchListeners.SwipeCallback;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.mainActivity.recycler.touchListeners.RecyclerTouchListener;

import java.util.List;

public class MovieRecyclerView extends SwipeCallback
{
    private AppCompatActivity activity;
    private RecyclerView recyclerView;

    private MovieAdapter movieAdapter;

    private boolean recentlySwiped = false;
    private final static int SWIPE_COOLDOWN = 1000;

    // --- --- --- Initialization --- --- ---
    public MovieRecyclerView(AppCompatActivity activity, @IdRes int id)
    {
        this.activity = activity;

        this.recyclerView = activity.findViewById(id);
        this.recyclerView.setHasFixedSize(true);

        this.initLayout();
        this.initTouchSwipeListener();

        this.movieAdapter = new MovieAdapter(this.activity);
    }

    private void initLayout()
    {
        // use linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        this.recyclerView.setLayoutManager(layoutManager);
    }

    private void initTouchSwipeListener()
    {
        // register touch listener
        new ItemTouchHelper(this).attachToRecyclerView(this.recyclerView);

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

    // --- --- --- handle touches and swipes --- --- ---
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

    private void handleClickedOnMovieItem(View view, int position)
    {

    }

    private void handleClickedLongOnMovieItem(View view, int position)
    {
        if(recentlySwiped)
        {
            return;
        }

        Movie movie = MyMoviesSortedList.getInstance().get(this.activity, position);
        if(HotMoviesSortedList.switchSave(this.activity, movie))
        {
            this.dataSetChanged(false);
        }
    }

    // --- --- --- manage items --- --- ---
    public void showItems()
    {
        this.recyclerView.setAdapter(movieAdapter);
    }

    public void addItems(List<Movie> movies, boolean saveInFile)
    {
        MyMoviesSortedList.getInstance().addAll(this.activity, movies);
        this.dataSetChanged(saveInFile);
    }

    public void addItem(Movie movie, boolean saveInFile)
    {
        if(!MyMoviesSortedList.getInstance().add(activity, movie))
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
        Movie movieToRemove = MyMoviesSortedList.getInstance().get(this.activity, position);
        if(movieToRemove.isHot())
        {
            HotMoviesSortedList.deleteSave(this.activity, movieToRemove);
        }

        MyMoviesSortedList.getInstance().remove(this.activity, position);
        this.dataSetChanged(true);
    }

    public void dataSetChanged(boolean saveInFile)
    {
        movieAdapter.notifyDataSetChanged();

        if(saveInFile)
        {
            new Thread(() -> MyMoviesSortedList.getInstance().save(this.activity))
                    .start();
        }
    }
}
