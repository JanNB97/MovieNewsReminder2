package com.yellowbite.movienewsreminder2.files.helper;

import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.SortedHotMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.MySortedMovieList;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.NewMovieQueue;
import com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles.SortedBookedMovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MovieFileHelper
{
    // --- --- --- Movie file parsing --- --- ---
    //barcode;url;standort;zugang;titel
    public static Movie toMovie(String string)
    {
        if(string == null)
        {
            return null;
        }

        String[] split = string.split(";");

        if(split.length != 5 && split.length != 6)
        {
            return null;
        }

        int barcode;
        try
        {
            barcode = Integer.parseInt(split[0]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        String url = split[1];
        String standort = split[2];
        if("null".equals(standort) || standort.isEmpty())
        {
            standort = null;
        }
        Date zugang = DateHelper.toDate(split[3]);

        String titel = split[4];
        if("null".equals(titel) || titel.isEmpty())
        {
            titel = null;
        }

        Movie movie = new Movie(
                barcode, url,
                null, -1, null,
                standort, zugang,
                titel);

        if(split.length == 6)
        {
            movie.setNotificationWasShown(split[5].equals("true"));
        }

        return movie;
    }

    public static List<String> toLines(Collection<Movie> movies)
    {
        List<String> strings = new ArrayList<>();

        for(Movie movie : movies)
        {
            strings.add(toLine(movie));
        }

        return strings;
    }

    public static String toLine(Movie movie)
    {
        return movie.getMediaBarcode() + ";" + movie.getURL() + ";"
                + movie.getStandort() + ";" + DateHelper.toString(movie.getZugang()) + ";"
                + movie.getTitel() + ";" + movie.notificationWasShown();
    }

    public static List<Movie> toMovies(Collection<String> strings, List<Movie> output)
    {
        for(String s : strings)
        {
            Movie movie = toMovie(s);

            if(movie != null)
            {
                output.add(movie);
            }
        }

        return output;
    }

    public static void startSaveAllThread()
    {
        new Thread(() -> {
            SortedHotMovieList.saveInstance();
            MySortedMovieList.saveInstance();
            NewMovieQueue.saveInstance();
            SortedBookedMovieList.saveInstance();
        }).start();
    }
}
