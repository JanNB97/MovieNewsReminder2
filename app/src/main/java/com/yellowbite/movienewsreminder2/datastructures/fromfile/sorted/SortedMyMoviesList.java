package com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

public final class SortedMyMoviesList extends SortedMovieListFromFile
{
    private static SortedMyMoviesList instance;

    private SortedMyMoviesList(Context context)
    {
        super(context, "myMovies.txt", Movie.STANDARD_COMPARATOR);
    }

    // --- --- --- Singleton methods --- --- ---
    public static SortedMyMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedMyMoviesList(context);
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
}
