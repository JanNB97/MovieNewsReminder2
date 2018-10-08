package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.Collections;
import java.util.List;

public class MyMoviesSortedList extends MovieListFromFile
{
    private static MyMoviesSortedList instance;

    private MyMoviesSortedList(Context context)
    {
        super(context, "myMovies.txt");
    }

    // --- --- --- get Instance --- --- ---
    public static MyMoviesSortedList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new MyMoviesSortedList(context);
        }

        return instance;
    }

    // --- --- --- data operations --- --- ---
    @Override
    public void addAll(Context context, List<Movie> movies)
    {
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
        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.sort();

        return true;
    }

    public boolean contains(Context context, Movie movie)
    {
        for(Movie m2 : super.movieList)
        {
            if(m2.getMediaBarcode() == movie.getMediaBarcode())
            {
                return true;
            }
        }

        return false;
    }

    private void sort()
    {
        Collections.sort(super.movieList);
    }

    public void loadHotMovies(Context context)
    {
        if(HotMoviesSortedList.getInstance(context).size(context) <= 0)
        {
            return;
        }

        HotMoviesSortedList.getInstance(context)
                .setAllHot(context, super.movieList);
    }
}
