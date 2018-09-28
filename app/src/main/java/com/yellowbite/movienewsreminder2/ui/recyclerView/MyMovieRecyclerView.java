package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    private MainActivity mainActivity;

    public MyMovieRecyclerView(MainActivity mainActivity, int id, MovieList movieList)
    {
        super(mainActivity, id, movieList, true, R.layout.movie_list_row);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        super.onSwiped(viewHolder, direction);
        this.mainActivity.handleOnSwiped(super.lastSwipedMovie);
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
            this.mainActivity.handleScrolledDown();
        }
        else
        {
            this.mainActivity.handleScrolledUp();
        }
    }
}
