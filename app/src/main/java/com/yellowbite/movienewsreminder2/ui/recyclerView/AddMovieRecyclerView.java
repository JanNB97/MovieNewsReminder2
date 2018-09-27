package com.yellowbite.movienewsreminder2.ui.recyclerView;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.ui.addMovie.AddMovieActivity;

public class AddMovieRecyclerView extends UnalterableRecyclerView
{
    private AddMovieActivity addMovieActivity;

    public AddMovieRecyclerView(AddMovieActivity addMovieActivity, int id, MovieList movieList)
    {
        super(addMovieActivity, id, movieList);
        this.addMovieActivity = addMovieActivity;
    }

    @Override
    protected void handleClickedOnMovieItem(View view, int position)
    {
        addMovieActivity.openMainActivity();
    }
}
