package com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.ArrayList;
import java.util.List;

public final class SortedBookedMovieList extends EfficientSortedMovieListFromFile
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
}
