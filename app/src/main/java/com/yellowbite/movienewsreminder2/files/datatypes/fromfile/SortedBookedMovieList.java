package com.yellowbite.movienewsreminder2.files.datatypes.fromfile;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.ArrayList;
import java.util.List;

public final class SortedBookedMovieList extends UnsortedMovieListFromFile
{
    private static SortedBookedMovieList instance;

    private SortedBookedMovieList(Context context)
    {
        super(context, "bookedMovies.txt");
    }

    // --- --- --- Singleton methods --- --- ---
    public static SortedBookedMovieList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedBookedMovieList(context);
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
    public List<Movie> getAndAddDifference(List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        for(Movie newMovie : bookedMovies)
        {
            if(isNewAndAdd(newMovie))
            {
                difference.add(newMovie);
            }
        }

        return difference;
    }

    private boolean isNewAndAdd(Movie newMovie)
    {
        int i = 0;
        for(Movie oldMovie : super.movieList)
        {
            if(newMovie.equals(oldMovie))
            {
                return false;
            }

            if(newMovie.getMediaBarcode() < oldMovie.getMediaBarcode())
            {
                super.movieList.add(i, newMovie);
                this.dirty = true;
                return true;
            }

            i++;
        }

        super.movieList.add(newMovie);
        this.dirty = true;
        return true;
    }

    public boolean containsAndRemove(Movie movie)
    {
        int i = 0;
        for(Movie otherMovie : super.movieList)
        {
            if(otherMovie.getMediaBarcode() > movie.getMediaBarcode())
            {
                return false;
            }

            if(otherMovie.equals(movie))
            {
                super.remove(i);
                return true;
            }

            i++;
        }

        return false;
    }
}
