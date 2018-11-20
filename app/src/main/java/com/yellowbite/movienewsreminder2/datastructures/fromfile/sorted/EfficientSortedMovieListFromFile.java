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
        return this.addAndGetPosition(0, newMovie) != -1;
    }

    public int addAndGetPosition(int start, Movie newMovie)
    {
        int insertPos = this.getInsertPositionIfNew(start, newMovie);

        if(insertPos == -1)
        {
            // not new
            return -1;
        }

        super.movieList.add(insertPos, newMovie);
        this.dirty = true;
        return insertPos;
    }

    @Override
    public void addAll(List<Movie> movies)
    {
        this.getAndAddDifference(movies);
    }

    public List<Movie> getAndAddDifference(List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        super.sort(bookedMovies);

        int posInList = 0;
        int insertPos;

        for(Movie newMovie : bookedMovies)
        {
            if(posInList >= super.size())
            {
                // rest is new
                super.movieList.add(newMovie);
                difference.add(newMovie);
                posInList++;
                continue;
            }

            insertPos = this.addAndGetPosition(posInList, newMovie);

            if(insertPos != -1)
            {
                difference.add(newMovie);
                posInList = insertPos;
            }
            posInList++;
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

    protected int getInsertPositionIfNew(int start, Movie newMovie)
    {
        int i;
        for(i = start; i < super.size(); i++)
        {
            Movie oldMovie = super.movieList.get(i);

            if(newMovie.equals(oldMovie))
            {
                return -1;
            }

            if(super.compareWithComparator(newMovie, oldMovie) < 0)
            {
                return i;
            }
        }

        return i;
    }
}
