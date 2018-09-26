package com.yellowbite.movienewsreminder2.files.datastructures;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class NewMoviesQueue
{
    private static final String NEW_MOVIES = "newMovies.txt";

    private static List<Movie> newMovies;

    // data doesn't change
    public static List<Movie> getAll(Context context)
    {
        getFromFileIfNecessary(context);
        return newMovies;
    }

    public static Movie get(Context context, int i)
    {
        getFromFileIfNecessary(context);
        return newMovies.get(i);
    }

    public static boolean isEmpty(Context context)
    {
        return FileManager.isEmpty(context, NEW_MOVIES);
    }

    public static int size(Context context)
    {
        getFromFileIfNecessary(context);
        return newMovies.size();
    }

    // data changes -> autosave
    public static void addAll(Context context, Collection<Movie> movies)
    {
        getFromFileIfNecessary(context);
        newMovies.addAll(movies);
        saveToFile(context);
    }

    public static void deleteLast(Context context)
    {
        getFromFileIfNecessary(context);
        newMovies.remove(newMovies.size() - 1);
        saveToFile(context);
    }

    // --- --- --- file operations --- --- ---

    private static void getFromFileIfNecessary(Context context)
    {
        if(newMovies == null)
        {
            getNewMovies(context);
        }
    }

    private static void getNewMovies(Context context)
    {
        newMovies = MovieFileHelper.toMovies(FileManager.readAll(context, NEW_MOVIES), new ArrayList<>());
    }

    private static void saveToFile(Context context)
    {
        FileManager.write(context, NEW_MOVIES, MovieFileHelper.toLines(newMovies));
    }
}
