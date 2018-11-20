package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.Collections;
import java.util.List;

public final class MySortedMovieList extends MovieListFromFile
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
        super.addAll(movies);
        this.sort();
    }

    @Override
    public boolean add(Movie movie)
    {
        if(super.contains(movie))
        {
            return false;
        }

        super.movieList.add(movie);
        this.sort();
        super.dirty = true;

        return true;
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
}
