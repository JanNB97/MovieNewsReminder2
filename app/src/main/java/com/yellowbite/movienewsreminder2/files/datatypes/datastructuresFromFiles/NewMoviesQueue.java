package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class NewMoviesQueue extends MovieListFromFile
{
    private static NewMoviesQueue instance;

    protected NewMoviesQueue(Context context)
    {
        super(context, "newMovies.txt");
    }

    public static NewMoviesQueue getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new NewMoviesQueue(context);
        }
        return instance;
    }

    public boolean isEmpty(Context context)
    {
        return FileManager.isEmpty(context, super.FILE_NAME);
    }

    // data changes -> autosave
    public void addAll(Context context, List<Movie> movies)
    {
        super.addAll(context, movies);
        super.saveToFile(context);
    }

    public void deleteLast(Context context)
    {
        super.remove(context, super.size(context) - 1);
        super.saveToFile(context);
    }
}
