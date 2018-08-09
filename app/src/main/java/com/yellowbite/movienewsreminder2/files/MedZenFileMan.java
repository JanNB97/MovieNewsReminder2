package com.yellowbite.movienewsreminder2.files;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.util.FileManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    // --- --- --- my movies --- --- ---
    public static void addMyMovie(Context context, Movie movie)
    {
        List<Movie> movies = getMyMovies(context, new ArrayList<>());
        movies.add(movie);
        setMyMovies(context, movies);
    }

    public static List<Movie> getMyMovies(Context context, List<Movie> myMovies)
    {
        List<String> lines = FileManager.readAll(context, MY_MOVIES);
        return toMovies(lines, myMovies);
    }

    public static void setMyMovies(Context context, Collection<Movie> myMovies)
    {
        FileManager.write(context, MY_MOVIES, toLines(myMovies));
    }

    // --- --- --- New movies --- --- ---

    public static List<Movie> getNewMovies(Context context)
    {
        return toMovies(FileManager.readAll(context, NEW_MOVIES), new ArrayList<>());
    }

    public static void addNewMovies(Context context, Collection<Movie> newMovies)
    {
        FileManager.insertFirst(context, NEW_MOVIES, toLines(newMovies));
    }

    public static void deleteLastNewMovie(Context context)
    {
        FileManager.deleteLast(context, NEW_MOVIES);
    }

    public static boolean newMoviesIsEmpty(Context context)
    {
        return FileManager.isEmpty(context, NEW_MOVIES);
    }

    // --- --- --- hot movies --- --- ---
    // TODO

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

    //movie1\nmovie2\nmovie3
    public static String toStatusLine(List<Movie> movies)
    {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (Movie movie : movies)
        {
            if(i != movies.size() - 1)
            {
                builder.append(toStatusLine(movie) + '\n');
            }
            else
            {
                builder.append(toStatusLine(movie));
            }
            i++;
        }

        return builder.toString();
    }

    public static List<Movie> toStatusMovies(String string)
    {
        String[] lines = string.split("\n");

        List<Movie> movies = new ArrayList<>();
        for (String line : lines)
        {
            movies.add(toStatusMovie(line));
        }

        return movies;
    }
}
