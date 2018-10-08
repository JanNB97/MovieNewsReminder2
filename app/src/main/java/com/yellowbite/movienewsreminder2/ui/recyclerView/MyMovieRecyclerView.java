package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.ui.activites.MyMoviesToolbarActivity;
import com.yellowbite.movienewsreminder2.ui.activites.ToolbarActivity;

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
        if(HotMoviesSortedList.getInstance(super.activity).switchSave(movie))
        {
            this.dataSetChanged(false);
        }
        super.handleClickedLongOnMovieItem(view, position);
    }
}
