package com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted;

import android.content.Context;

public final class WishedMoviesList extends UnsortedMovieListFromFile
{
    private static WishedMoviesList instance;

    private WishedMoviesList(Context context)
    {
        super(context, "wishedMovieList.txt", true);
    }

    // --- --- --- Singleton methods --- --- ---
    public static WishedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new WishedMoviesList(context);
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
