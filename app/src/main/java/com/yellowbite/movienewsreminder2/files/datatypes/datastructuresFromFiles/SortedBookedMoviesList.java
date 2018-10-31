package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class SortedBookedMoviesList
{
    private static final String FILE_NAME = "bookedMovies.txt";

    private Context context;

    private static SortedBookedMoviesList instance;

    private List<Integer> bookedMovies;

    private SortedBookedMoviesList(Context context)
    {
        this.context = context;
        this.getFromFileIfNecessary();
    }

    public static SortedBookedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedBookedMoviesList(context);
        }

        return instance;
    }

    public List<Movie> getAndAddDifference(List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        boolean bookedListChanged = false;

        for(Movie newMovie : bookedMovies)
        {
            if(isNewAndAdd(newMovie.getMediaBarcode()))
            {
                difference.add(newMovie);
                bookedListChanged = true;
            }
        }

        if(bookedListChanged)
        {
            this.save();
        }

        return difference;
    }

    private boolean isNewAndAdd(Integer newMovie)
    {
        for(int i = 0; i < this.bookedMovies.size(); i++)
        {
            Integer oldMovie = this.bookedMovies.get(i);

            if(newMovie.equals(oldMovie))
            {
                return false;
            }

            if(newMovie < oldMovie)
            {
                this.bookedMovies.add(i, newMovie);
                return true;
            }
        }

        this.bookedMovies.add(newMovie);
        return true;
    }

    public boolean containsAndRemove(Movie movie)
    {
        int movieCode = movie.getMediaBarcode();

        int position = 0;
        for(Integer mc : this.bookedMovies)
        {
            if(mc > movieCode)
            {
                return false;
            }

            if(mc == movieCode)
            {
                this.bookedMovies.remove(position);
                this.save();
                return true;
            }

            position++;
        }

        return false;
    }

    // --- --- --- file helper methods --- --- ---
    private void getFromFileIfNecessary()
    {
        this.bookedMovies = new ArrayList<>();
        List<String> readLines = FileManager.readAll(context, FILE_NAME);
        for(String s : readLines)
        {
            this.bookedMovies.add(Integer.parseInt(s));
        }
    }

    public void save()
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
