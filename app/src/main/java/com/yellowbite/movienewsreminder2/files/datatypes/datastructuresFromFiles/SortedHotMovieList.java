package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.Collection;

public final class SortedHotMovieList extends MovieListFromFile
{
    private static SortedHotMovieList instance;

    protected SortedHotMovieList(Context context)
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

    public boolean switchHot(Movie movie)
    {
        movie.setHot(!movie.isHot());
        super.dirty = true;
        if(!movie.isHot())
        {
            return delete(movie);
        }
        else
        {
            return this.add(movie);
        }
    }

    public boolean delete(Movie movie)
    {
        boolean success = this.delSorted(movie);
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

    // --- --- --- Clean methods --- --- ---
    public void setIfHot(Collection<Movie> movies)
    {
        for(Movie movie : movies)
        {
            if(SortedHotMovieList.getInstance(super.context).getIdInList(movie) != -1)
            {
                movie.setHot(true);
            }
        }
    }

    // --- --- --- Help methods --- --- ---
    private int getIdInList(Movie movie)
    {
        int movieBarcode = movie.getMediaBarcode();

        int i = 0;
        for(Movie m2 : super.movieList)
        {
            int b2 = m2.getMediaBarcode();

            if(b2 == movieBarcode)
            {
                return i;
            }

            if(b2 > movieBarcode)
            {
                return -1;
            }

            i++;
        }

        return -1;
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

    private boolean delSorted(Movie movie)
    {
        int i = this.getIdInList(movie);
        boolean success = i != -1;

        if(success)
        {
            super.movieList.remove(i);
        }
        return success;
    }
}
