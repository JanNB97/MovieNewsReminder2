package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;

public final class NewMovieQueue extends MovieListFromFile
{
    private static NewMovieQueue instance;

    protected NewMovieQueue(Context context)
    {
        super(context, "newMovies.txt");
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

    // --- --- --- Dirty data methods --- --- ---
    public void deleteLast()
    {   // TODO - make better
        super.remove(super.size() - 1);
        super.dirty = true;
    }

    // --- --- --- Clean data methods --- --- ---
    @Override
    public boolean isEmpty()
    {
        return FileManager.isEmpty(super.context, super.FILE_NAME);
    }
}
