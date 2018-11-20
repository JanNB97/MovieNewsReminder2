package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles;

import android.content.Context;

public final class MyMoviesList extends MovieListFromFile
{
    private static MyMoviesList instance;

    private MyMoviesList(Context context)
    {
        super(context, "myMovies.txt");
    }

    // --- --- --- Singleton methods --- --- ---
    public static MyMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new MyMoviesList(context);
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

    // --- --- --- Other methods --- --- ---
    public void loadHotMovies()
    {
        if(SortedHotMovieList.getInstance(super.context).size() <= 0)
        {
            return;
        }

        SortedHotMovieList.getInstance(super.context)
                .setIfHot(super.movieList);
    }
}
