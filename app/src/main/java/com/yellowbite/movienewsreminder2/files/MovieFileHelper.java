package com.yellowbite.movienewsreminder2.files;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.files.FileManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MovieFileHelper
{
    private static final String NEWEST_BARCODE = "newestBarcode.txt";
    private static final String HOT_MOVIES = "hotMovies.txt";

    // --- --- --- Newest movie --- --- ---
    public static int getNewestBarcode(Context context)
    {
        String s = FileManager.read(context, NEWEST_BARCODE);

        if(s == null)
        {
            return -1;
        }

        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ignored)
        {
            return -1;
        }
    }

    public static void setNewestBarcode(Context context, int barcode)
    {
        FileManager.write(context, NEWEST_BARCODE, Integer.toString(barcode));
    }

    // --- --- --- Movie file parsing --- --- ---
    //barcode;url;standort;zugang;titel
    public static Movie toMovie(String string)
    {
        if(string == null)
        {
            return null;
        }

        String[] split = string.split(";");

        if(split.length != 5)
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
        if("null".equals(standort))
        {
            standort = null;
        }
        Date zugang = DateHelper.toDate(split[3]);

        String titel = split[4];
        if("null".equals(titel))
        {
            titel = null;
        }

        return new Movie(
                barcode, url,
                null, -1, null,
                standort, zugang,
                titel);
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
                + movie.getTitel();
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

    // --- --- --- Parsing for sending between activites --- --- ---

    public static String toStatusLine(Movie movie)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(toLine(movie));

        builder.append(";" + movie.getStatus() + ";"
                + movie.getVorbestellungen() + ";"
                + DateHelper.toString(movie.getEntliehenBis()));

        return builder.toString();
    }

    public static Movie toStatusMovie(String string)
    {
        if(string == null)
        {
            return null;
        }

        String[] split = string.split(";");

        if(split.length != 8)
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
        if("null".equals(standort))
        {
            standort = null;
        }
        Date zugang = DateHelper.toDate(split[3]);

        String titel = split[4];
        if("null".equals(titel))
        {
            titel = null;
        }

        Movie.Status status = null;
        if(split[5] != null)
        {
            status = Movie.Status.valueOf(split[5]);
        }

        int vorbestellungen;
        try
        {
            vorbestellungen = Integer.parseInt(split[6]);
        }
        catch (NumberFormatException e)
        {
            vorbestellungen = -1;
        }

        Date entliehenBis = DateHelper.toDate(split[7]);

        return new Movie(
                barcode, url,
                status, vorbestellungen, entliehenBis,
                standort, zugang,
                titel);
    }
}
