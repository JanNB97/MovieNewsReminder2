package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortedBookedMoviesList
{
    private static final String FILE_NAME = "bookedMovies.txt";

    private static SortedBookedMoviesList instance;

    private List<Integer> bookedMovies;

    public static SortedBookedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedBookedMoviesList();
            instance.getFromFileIfNecessary(context);
        }

        return instance;
    }

    public List<Movie> getAndAddDifference(Context context, List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        boolean bookedListChanged = false;

        for(Movie newStatus : bookedMovies)
        {
            if(isNew(newStatus.getMediaBarcode()))
            {
                difference.add(newStatus);
                bookedListChanged = true;
            }
        }

        if(bookedListChanged)
        {
            this.save(context);
        }

        return difference;
    }

    private boolean isNew(Integer newStatus)
    {
        for(int i = 0; i < this.bookedMovies.size(); i++)
        {
            Integer oldStatus = this.bookedMovies.get(i);

            if(newStatus.equals(oldStatus))
            {
                return false;
            }

            if(newStatus < oldStatus)
            {
                this.bookedMovies.add(i, newStatus);
                return true;
            }
        }

        this.bookedMovies.add(newStatus);
        return true;
    }

    public boolean containsAndRemove(Context context, Movie movie)
    {
        int movieCode = movie.getMediaBarcode();

        int position = 0;
        for(Integer mc : this.bookedMovies)
        {
            if(movieCode > mc)
            {
                return false;
            }

            if(mc == movieCode)
            {
                this.bookedMovies.remove(position);
                this.save(context);
                return true;
            }

            position++;
        }

        return false;
    }

    // --- --- --- file helper methods --- --- ---
    private void getFromFileIfNecessary(Context context)
    {
        this.bookedMovies = new ArrayList<>();
        List<String> readLines = FileManager.readAll(context, FILE_NAME);
        for(String s : readLines)
        {
            this.bookedMovies.add(Integer.parseInt(s));
        }
    }

    private void save(Context context)
    {
        FileManager.write(context, FILE_NAME, this.toLines());
    }

    private List<String> toLines()
    {
        List<String> lines = new ArrayList<>();

        for (Integer i : this.bookedMovies)
        {
            lines.add(i.toString());
        }

        return lines;
    }
}
