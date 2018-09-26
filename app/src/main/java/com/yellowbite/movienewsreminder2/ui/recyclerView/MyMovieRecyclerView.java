package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datastructures.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datastructures.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.recyclerView.touchListeners.SwipeCallback;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.ui.recyclerView.touchListeners.RecyclerTouchListener;

import java.util.List;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    public MyMovieRecyclerView(AppCompatActivity activity, int id, MovieList movieList)
    {
        super(activity, id, movieList, true);
    }

    @Override
    protected void handleClickedOnMovieItem(View view, int position)
    {

    }

    @Override
    protected void handleClickedLongOnMovieItem(View view, int position)
    {
        if(super.recentlySwiped)
        {
            return;
        }

        Movie movie = super.movieList.get(super.activity, position);
        if(HotMoviesSortedList.switchSave(super.activity, movie))
        {
            this.dataSetChanged(false);
        }
    }
}
