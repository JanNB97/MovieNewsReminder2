package com.yellowbite.movienewsreminder2.files.datatypes.fromfile;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.Collection;

public final class SortedHotMovieList extends UnsortedMovieListFromFile
{
    private static SortedHotMovieList instance;

    private SortedHotMovieList(Context context)
    {
        super(context, "hotMovies.txt");
    }

    // --- --- --- Singleton methods --- --- ---
    public static SortedHotMovieList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedHotMovieList(context);
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
    @Override
    public boolean add(Movie movie)
    {
        boolean success = this.addSorted(movie);
        if(success)
        {
            super.dirty = true;
        }
        return success;
    }

    public void setNotificationWasShown(int id, boolean b)
    {
        super.movieList.get(id).setNotificationWasShown(b);
        super.dirty = true;
    }

    private boolean addSorted(Movie movie)
    {
        int movieBarcode = movie.getMediaBarcode();

        int i = 0;
        for(Movie m2 : super.movieList)
        {
            int barcode2 = m2.getMediaBarcode();

            if(barcode2 == movieBarcode)
            {
                return false;
            }

            if(barcode2 > movieBarcode)
            {
                super.movieList.add(i, movie);
                return true;
            }

            i++;
        }
        super.movieList.add(movie);
        return true;
    }
}
