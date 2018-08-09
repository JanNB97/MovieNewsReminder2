package com.yellowbite.movienewsreminder2.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.util.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMoviesSortedList
{
    private static final String MY_MOVIES = "myMovies.txt";

    private static List<Movie> myMovies;

    // --- --- --- data operations --- --- ---
    // get
    public static Movie get(Context context, int i)
    {
        getFromFileIfNecessary(context);
        return myMovies.get(i);
    }

    public static List<Movie> get(Context context)
    {
        getFromFileIfNecessary(context);
        return myMovies;
    }

    // others
    public static int size(Context context)
    {
        getFromFileIfNecessary(context);
        return myMovies.size();
    }

    // add
    public static void addAll(Context context, List<Movie> movies)
    {
        getFromFileIfNecessary(context);

        for(Movie movie : movies)
        {
            if(isNew(context, movie))
            {
                myMovies.add(movie);
            }
        }

        sort();
    }

    public static boolean add(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);

        if(!isNew(context, movie))
        {
            return false;
        }

        myMovies.add(movie);
        sort();

        return true;
    }

    // remove
    public static void remove(Context context, int i)
    {
        getFromFileIfNecessary(context);
        myMovies.remove(i);
    }

    // others
    private static void sort()
    {
        Collections.sort(myMovies);
    }

    // --- --- --- file operations --- --- ---

    public static void save(Context context)
    {
        if(myMovies != null)
        {
            saveToFile(context);
        }
    }

    // file helper operations
    private static void getFromFileIfNecessary(Context context)
    {
        if(myMovies == null)
        {
            myMovies = getFromFile(context);
        }
    }

    private static List<Movie> getFromFile(Context context)
    {
        List<String> lines = FileManager.readAll(context, MY_MOVIES);
        return MedZenFileMan.toMovies(lines, new ArrayList<>());
    }

    private static void saveToFile(Context context)
    {
        FileManager.write(context, MY_MOVIES, MedZenFileMan.toLines(myMovies));
    }

    // --- --- --- helper methods --- --- ---

    private static boolean isNew(Context context, Movie movie)
    {
        for(Movie movieInDatabase : myMovies)
        {
            if(movieInDatabase.getMediaBarcode() == movie.getMediaBarcode())
            {
                return false;
            }
        }

        return true;
    }
}
