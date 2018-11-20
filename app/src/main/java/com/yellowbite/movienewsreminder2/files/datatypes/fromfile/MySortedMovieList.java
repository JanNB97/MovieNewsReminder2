package com.yellowbite.movienewsreminder2.files.datatypes.fromfile;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.List;

public final class MySortedMovieList extends SortedMovieListFromFile
{
    private static MySortedMovieList instance;

    private MySortedMovieList(Context context)
    {
        super(context, "myMovies.txt", Movie.STANDARD_COMPARATOR);
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
}
