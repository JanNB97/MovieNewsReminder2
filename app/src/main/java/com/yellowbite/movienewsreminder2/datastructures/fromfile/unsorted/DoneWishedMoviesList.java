package com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted;

import android.content.Context;

public final class DoneWishedMoviesList extends UnsortedMovieListFromFile
{
    private static DoneWishedMoviesList instance;

    private DoneWishedMoviesList(Context context)
    {
        super(context, "doneWishedMovieList.txt", true);
    }

    // --- --- --- Singleton methods --- --- ---
    public static DoneWishedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DoneWishedMoviesList(context);
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
