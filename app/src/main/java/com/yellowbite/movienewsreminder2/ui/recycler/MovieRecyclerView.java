package com.yellowbite.movienewsreminder2.ui.recycler;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.data.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.data.MyMoviesSortedList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;

import java.util.List;

public class MovieRecyclerView
{
    private AppCompatActivity activity;
    private RecyclerView recyclerView;

    private MovieAdapter movieAdapter;

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
        new ItemTouchHelper(new SwipeCallback(this)).attachToRecyclerView(this.recyclerView);

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

    // --- --- --- handle movie clicks --- --- ---
    private void handleClickedOnMovieItem(View view, int position)
    {

    }

    private void handleClickedLongOnMovieItem(View view, int position)
    {
        Movie movie = MyMoviesSortedList.get(this.activity, position);
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
        MyMoviesSortedList.addAll(this.activity, movies);
        this.dataSetChanged(saveInFile);
    }

    public void addItem(Movie movie, boolean saveInFile)
    {
        if(!MyMoviesSortedList.add(activity, movie))
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
        Movie movieToRemove = MyMoviesSortedList.get(this.activity, position);
        if(movieToRemove.isHot())
        {
            HotMoviesSortedList.deleteSave(this.activity, movieToRemove);
        }

        MyMoviesSortedList.remove(this.activity, position);
        this.dataSetChanged(true);
    }

    public void dataSetChanged(boolean saveInFile)
    {
        movieAdapter.notifyDataSetChanged();

        if(saveInFile)
        {
            new Thread(() -> MyMoviesSortedList.save(this.activity))
                    .start();
        }
    }
}
