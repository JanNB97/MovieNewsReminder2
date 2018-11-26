package com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted;

import android.content.Context;

public final class ToDoWishedMoviesList extends UnsortedMovieListFromFile
{
    private static ToDoWishedMoviesList instance;

    private ToDoWishedMoviesList(Context context)
    {
        super(context, "todoWishedMovieList.txt", true);
    }

    // --- --- --- Singleton methods --- --- ---
    public static ToDoWishedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new ToDoWishedMoviesList(context);
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
