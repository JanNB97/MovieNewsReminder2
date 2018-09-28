package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    private Runnable handleOnScrolledUp;
    private Runnable handleOnScrolledDown;

    private Runnable handleOnSwiped;

    public MyMovieRecyclerView(AppCompatActivity activity, int id, MovieList movieList,
                               Runnable handleOnScrolledDown, Runnable handleOnScrolledUp,
                                Runnable handleOnSwiped)
    {
        super(activity, id, movieList, true, R.layout.movie_list_row);

        this.handleOnScrolledUp = handleOnScrolledUp;
        this.handleOnScrolledDown = handleOnScrolledDown;
        this.handleOnSwiped = handleOnSwiped;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        super.onSwiped(viewHolder, direction);
        handleOnSwiped.run();
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

    @Override
    protected void handleOnScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.handleOnScrolled(recyclerView, dx, dy);

        if(dy > 0)
        {
            this.handleOnScrolledDown.run();
        }
        else
        {
            this.handleOnScrolledUp.run();
        }
    }
}
