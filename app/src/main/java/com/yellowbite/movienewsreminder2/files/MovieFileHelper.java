package com.yellowbite.movienewsreminder2.files;

import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.DoneWishedMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.NewMovieQueue;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedBookedMovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.ToDoWishedMoviesList;
import com.yellowbite.movienewsreminder2.util.DateHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MovieFileHelper
{
    // --- --- --- Movie file parsing --- --- ---
    public static List<String> toLines(Collection<Movie> movies)
    {
        List<String> strings = new ArrayList<>();

        for(Movie movie : movies)
        {
            strings.add(toLine(movie));
        }

        return strings;
    }

    private static String toLine(Movie movie)
    {
        return movie.getMediaBarcode() + ";" + movie.getURL() + ";"
                + movie.getStandort() + ";" + DateHelper.toString(movie.getZugang()) + ";"
                + movie.getTitel() + ";" + movie.notificationWasShown() + ";"
                + movie.isHot();
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

    private static Movie toMovie(String string)
    {
        if(string == null)
        {
            return null;
        }

        String[] split = string.split(";");

        if(split.length != 6 && split.length != 7)
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

        boolean notificationWasShown = split[5].equals("true");

        boolean isHot = false;
        if(split.length == 7)
        {
            isHot = split[6].equals("true");
        }

        return new Movie(
                barcode, url,
                null, -1, null,
                standort, zugang,
                titel, notificationWasShown, isHot);
    }

    public static void startSaveAllThread()
    {
        new Thread(() -> {
            SortedMyMoviesList.saveInstance();
            NewMovieQueue.saveInstance();
            SortedBookedMovieList.saveInstance();
            ToDoWishedMoviesList.saveInstance();
            DoneWishedMoviesList.saveInstance();
        }).start();
    }
}
