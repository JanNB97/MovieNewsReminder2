package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.FileManager;

import java.util.List;

public class MedZenFileMan
{
    private static final String NEWEST_BARCODE = "newestBarcode.txt";
    private static final String NEW_MOVIES = "newMovies.txt";
    private static final String HOT_MOVIES = "hotMovies.txt";
    private static final String MY_MOVIES = "myMovies.txt";

    // --- --- --- Newest movie --- --- ---
    public static int getNewestBarcode(Context context)
    {
        String s = FileManager.read(context, NEWEST_BARCODE);

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

    // --- --- --- New movies --- --- ---

    // --- --- --- hot movies --- --- ---

    // --- --- --- my movies --- --- ---

    // --- --- --- Movie file parsing --- --- ---
    //barcode;url
    private static Movie toMovie(String string)
    {
        if(string == null)
        {
            return null;
        }

        String[] split = string.split(";");

        if(split.length != 2)
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

        return new Movie(barcode, url);
    }

    private static String toLine(Movie movie)
    {
        return movie.getMediaBarcode() + ";" + movie.getURL();
    }
}
