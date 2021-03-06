package com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.MovieListFromFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class SortedMovieListFromFile extends MovieListFromFile
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

    protected void sort(List<Movie> movies)
    {
        Collections.sort(movies, this.comparator);
    }

    protected void sort()
    {
        this.sort(super.movieList);
    }

    protected int compareWithComparator(Movie m1, Movie m2)
    {
        return this.comparator.compare(m1, m2);
    }
}
