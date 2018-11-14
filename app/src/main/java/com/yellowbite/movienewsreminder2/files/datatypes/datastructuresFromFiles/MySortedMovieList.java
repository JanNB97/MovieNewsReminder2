package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.Collections;
import java.util.List;

public class MySortedMovieList extends MovieListFromFile
{
    private static MySortedMovieList instance;

    private MySortedMovieList(Context context)
    {
        super(context, "myMovies.txt");
    }

    // --- --- --- Singleton methods --- --- ---
    public static MySortedMovieList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new MySortedMovieList(context);
        }

        return instance;
    }

    public static void saveInstance()
    {
        if(instance != null && instance.isDirty())
        {
            instance.save();
        }
    }

    // --- --- --- Dirty data methods --- --- ---
    @Override
    public void addAll(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            if(this.isNew(movie))
            {
                this.movieList.add(movie);
                super.dirty = true;
            }
        }

        this.sort();
    }

    @Override
    public boolean add(Movie movie)
    {
        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.sort();
        super.dirty = true;

        return true;
    }

    // --- --- --- Clean data methods --- --- ---
    public boolean contains(Movie movie)
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

    // --- --- --- Other methods --- --- ---
    public void loadHotMovies()
    {
        if(SortedHotMovieList.getInstance(super.context).size() <= 0)
        {
            return;
        }

        SortedHotMovieList.getInstance(super.context)
                .setIfHot(super.movieList);
    }

    // --- --- --- helper methods --- --- ---
    private void sort()
    {
        Collections.sort(super.movieList);
    }
}