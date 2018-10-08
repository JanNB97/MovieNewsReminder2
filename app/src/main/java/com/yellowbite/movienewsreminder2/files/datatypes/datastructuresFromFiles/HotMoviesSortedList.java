package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class HotMoviesSortedList extends MovieListFromFile
{
    private static HotMoviesSortedList instance;

    protected HotMoviesSortedList(Context context)
    {
        super(context, "hotMovies.txt");
    }

    public static HotMoviesSortedList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new HotMoviesSortedList(context);
        }

        return instance;
    }

    public void setAllHot(Context context, Collection<Movie> movies)
    {
        for(Movie movie : movies)
        {
            if(HotMoviesSortedList.getInstance(context).getIdInList(movie) != -1)
            {
                movie.setHot(true);
            }
        }
    }

    @Override
    public boolean add(Context context, Movie movie)
    {
        boolean success = this.addSorted(movie);
        if(success)
        {
            saveToFile(context);
        }
        return success;
    }

    public boolean switchSave(Context context, Movie movie)
    {
        movie.setHot(!movie.isHot());
        if(!movie.isHot())
        {
            return deleteSave(context, movie);
        }
        else
        {
            return this.add(context, movie);
        }
    }

    public boolean deleteSave(Context context, Movie movie)
    {
        boolean success = this.delSorted(movie);
        if(success)
        {
            super.saveToFile(context);
        }
        return success;
    }

    public void setNotificationWasShownSave(Context context, int id, boolean b)
    {
        super.movieList.get(id).setNotificationWasShown(b);
        super.saveToFile(context);
    }

    // --- --- --- data type operations --- --- ---

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
