package com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.MovieListFromFile;

import java.util.List;

public abstract class UnsortedMovieListFromFile extends MovieListFromFile
{
    protected boolean allowDuplicates;

    protected UnsortedMovieListFromFile(Context context, String fileName, boolean allowDuplicates)
    {
        super(context, fileName);
        this.allowDuplicates = allowDuplicates;
    }

    // --- --- --- dirty data methods --- --- ---
    @Override
    public void addAll(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            this.add(movie);
        }
    }

    @Override
    public boolean add(Movie movie)
    {
        if(!allowDuplicates && this.contains(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.dirty = true;

        return true;
    }
}
