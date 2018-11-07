package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;

public class MyMovieRecyclerView extends MovieRecyclerView
{
    public MyMovieRecyclerView(AppCompatActivity activity, int id, MovieList movieList)
    {
        super(activity, id, movieList, true, R.layout.movie_list_row);
    }

    @Override
    protected void handleClickedLongOnMovieItem(View view, int position)
    {
        if(super.recentlySwiped)
        {
            return;
        }

        Movie movie = super.movieList.get(position);
        if(HotMoviesSortedList.getInstance(super.activity).switchHot(movie))
        {
            this.dataSetChanged();
        }
        super.handleClickedLongOnMovieItem(view, position);
    }
}
