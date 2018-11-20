package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedMovieListFromFile extends MovieListFromFile
{
    private Comparator<Movie> comparator;

    protected SortedMovieListFromFile(Context context, String fileName, Comparator<Movie> comparator)
    {
        super(context, fileName);
        this.comparator = comparator;
    }

    @Override
    public void addAll(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            this.add(movie, false);
        }
        this.sort();
    }

    @Override
    public boolean add(Movie movie)
    {
        return add(movie, true);
    }

    private boolean add(Movie movie, boolean sort)
    {
        if(super.contains(movie))
        {
            return false;
        }

        super.movieList.add(movie);
        if(sort)
        {
            this.sort();
        }
        super.dirty = true;

        return true;
    }

    @Override
    public void remove(int i)
    {
        // TODO
    }

    @Override
    public void removeLast()
    {
        // TODO
    }

    private void sort()
    {
        Collections.sort(super.movieList, this.comparator);
    }
}
