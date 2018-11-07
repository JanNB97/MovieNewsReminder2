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

    public static void saveInstance()
    {
        if(instance != null)
        {
            instance.save();
        }
    }

    public boolean isEmpty()
    {
        return FileManager.isEmpty(super.context, super.FILE_NAME);
    }

    // data changes -> autosave
    public void addAll(List<Movie> movies)
    {   // TODO - make better
        super.addAll(movies);
        super.saveToFile();
    }

    public void deleteLast()
    {   // TODO - make better
        super.remove(super.size() - 1);
        super.saveToFile();
    }
}
