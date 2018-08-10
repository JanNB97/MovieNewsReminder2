package com.yellowbite.movienewsreminder2.files.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.files.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyMoviesSortedList
{
    private static final String FILE_NAME = "myMovies.txt";

    private static List<Movie> myMovies;

    // --- --- --- data operations --- --- ---
    // getAll
    public static Movie get(Context context, int i)
    {
        getFromFileIfNecessary(context);
        return myMovies.get(i);
    }

    public static List<Movie> getAll(Context context)
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
            if(isNew(movie))
            {
                myMovies.add(movie);
            }
        }

        sort();
    }

    public static boolean add(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);

        if(!isNew(movie))
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
        List<String> lines = FileManager.readAll(context, FILE_NAME);
        return MovieFileHelper.toMovies(lines, Collections.synchronizedList(new ArrayList<>()));
    }

    private static void saveToFile(Context context)
    {
        FileManager.write(context, FILE_NAME, MovieFileHelper.toLines(myMovies));
    }

    // --- --- --- helper methods --- --- ---

    private static boolean isNew(Movie movie)
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
