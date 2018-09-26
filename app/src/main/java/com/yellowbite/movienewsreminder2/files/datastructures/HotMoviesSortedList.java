package com.yellowbite.movienewsreminder2.files.datastructures;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.List;

public final class HotMoviesSortedList
{
    private static final String FILE_NAME = "hotMovies.txt";

    private static List<Movie> hotMovies;

    public static boolean addSave(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);
        movie.setHot(true);

        boolean success = addSorted(movie);
        if(success)
        {
            saveToFile(context);
        }
        return success;
    }

    public static boolean switchSave(Context context, Movie movie)
    {
        if(movie.isHot())
        {
            return deleteSave(context, movie);
        }
        else
        {
            return addSave(context, movie);
        }
    }

    public static boolean deleteSave(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);
        movie.setHot(false);
        boolean success = delSorted(movie);
        if(success)
        {
            saveToFile(context);
        }
        return success;
    }

    public static void getMyHotMovies(Context context)
    {
        getFromFileIfNecessary(context);
        if(hotMovies.isEmpty())
        {
            return;
        }
        List<Movie> myMovies = MyMoviesSortedList.getAll(context);

        for(Movie movie : myMovies)
        {
            if(getIdInList(movie) != -1)
            {
                movie.setHot(true);
            }
        }
    }

    public static List<Movie> get(Context context)
    {
        getFromFileIfNecessary(context);
        return hotMovies;
    }

    public static void setNotificationWasShownSave(Context context, int id, boolean b)
    {
        getFromFileIfNecessary(context);
        hotMovies.get(id).setNotificationWasShown(b);
        saveToFile(context);
    }

    // --- --- --- data type operations --- --- ---

    private static int getIdInList(Movie movie)
    {
        int movieBarcode = movie.getMediaBarcode();

        int i = 0;
        for(Movie m2 : hotMovies)
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

    private static boolean addSorted(Movie movie)
    {
        int movieBarcode = movie.getMediaBarcode();

        int i = 0;
        for(Movie m2 : hotMovies)
        {
            int barcode2 = m2.getMediaBarcode();

            if(barcode2 == movieBarcode)
            {
                return false;
            }

            if(barcode2 > movieBarcode)
            {
                hotMovies.add(i, movie);
                return true;
            }

            i++;
        }
        hotMovies.add(movie);
        return true;
    }

    private static boolean delSorted(Movie movie)
    {
        int i = getIdInList(movie);
        boolean success = i != -1;

        if(success)
        {
            hotMovies.remove(i);
        }
        return success;
    }

    // --- --- --- file operations --- --- ---

    private static void getFromFileIfNecessary(Context context)
    {
        hotMovies = MovieFileHelper.toMovies(FileManager.readAll(context, FILE_NAME), new ArrayList<>());
    }

    private static void saveToFile(Context context)
    {
        FileManager.write(context, FILE_NAME, toLines(hotMovies));
    }

    private static List<String> toLines(List<Movie> movies)
    {
        List<String> lines = new ArrayList<>();
        for(Movie movie : movies)
        {
            lines.add(toLine(movie));
        }
        return lines;
    }

    private static String toLine(Movie movie)
    {
        return movie.getMediaBarcode() + ";" + movie.getURL() + ";;;"
                + movie.getTitel() + ";" + movie.notificationWasShown();
    }
}
