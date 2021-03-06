package com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.FileManager;

public final class NewMovieQueue extends UnsortedMovieListFromFile
{
    private static NewMovieQueue instance;

    private NewMovieQueue(Context context)
    {
        super(context, "newMovies.txt", false);
    }

    // --- --- --- Singleton methods --- --- ---
    public static NewMovieQueue getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new NewMovieQueue(context);
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

    // --- --- --- Clean data methods --- --- ---
    @Override
    public boolean isEmpty()
    {
        return FileManager.isEmpty(super.context, super.FILE_NAME);
    }
}
