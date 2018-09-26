package com.yellowbite.movienewsreminder2.files.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.files.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMoviesSortedList extends MovieListFromFile
{
    private static MyMoviesSortedList instance;

    private MyMoviesSortedList()
    {
        super("myMovies.txt");
    }

    // --- --- --- get Instance --- --- ---
    public static MyMoviesSortedList getInstance()
    {
        if(instance == null)
        {
            instance = new MyMoviesSortedList();
        }

        return instance;
    }

    // --- --- --- data operations --- --- ---
    @Override
    public void addAll(Context context, List<Movie> movies)
    {
        this.getFromFileIfNecessary(context);

        for(Movie movie : movies)
        {
            if(this.isNew(movie))
            {
                this.movieList.add(movie);
            }
        }

        this.sort();
    }

    @Override
    public boolean add(Context context, Movie movie)
    {
        this.getFromFileIfNecessary(context);

        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.sort();

        return true;
    }

    private void sort()
    {
        Collections.sort(super.movieList);
    }
}
