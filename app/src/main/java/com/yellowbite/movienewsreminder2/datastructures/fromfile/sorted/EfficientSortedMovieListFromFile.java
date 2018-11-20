package com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class EfficientSortedMovieListFromFile extends SortedMovieListFromFile
{
    protected EfficientSortedMovieListFromFile(Context context, String fileName)
    {
        super(context, fileName, Movie.SIMPLE_COMPARATOR);
    }

    // --- --- --- Dirty data methods --- --- ---
    @Override
    public boolean add(Movie newMovie)
    {
        int insertPos = this.getInsertPositionIfNew(newMovie);

        if(insertPos == -1)
        {
            // not new
            return false;
        }

        super.movieList.add(insertPos, newMovie);
        this.dirty = true;
        return true;
    }

    public List<Movie> getAndAddDifference(List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        for(Movie newMovie : bookedMovies)
        {
            if(this.add(newMovie))
            {
                difference.add(newMovie);
            }
        }

        return difference;
    }

    @Override
    public boolean remove(Movie movie)
    {
        int pos = this.getPositionIfContains(movie);

        if (pos == -1)
        {
            return false;
        }

        return super.remove(pos);
    }

    // --- --- --- Clean data methods --- --- ---
    @Override
    public boolean contains(Movie newMovie)
    {
        return getPositionIfContains(newMovie) != -1;
    }

    // --- --- --- Help methods --- --- ---
    protected int getPositionIfContains(Movie newMovie)
    {
        int i = 0;
        for(Movie oldMovie : super.movieList)
        {
            if(newMovie.equals(oldMovie))
            {
                return i;
            }

            if(super.compareWithComparator(newMovie, oldMovie) < 0)
            {
                return -1;
            }
            i++;
        }

        return -1;
    }

    protected int getInsertPositionIfNew(Movie newMovie)
    {
        int i = 0;
        for(Movie oldMovie : super.movieList)
        {
            if(newMovie.equals(oldMovie))
            {
                return -1;
            }

            if(super.compareWithComparator(newMovie, oldMovie) < 0)
            {
                return i;
            }

            i++;
        }

        return i;
    }
}
