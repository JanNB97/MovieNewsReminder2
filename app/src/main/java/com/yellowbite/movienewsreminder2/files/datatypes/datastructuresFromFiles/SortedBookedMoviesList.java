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
    private boolean dirty;

    private List<Integer> bookedMovies;

    private SortedBookedMoviesList(Context context)
    {
        this.context = context;
        this.getFromFileIfNecessary();
    }

    // --- --- --- Singleton methods --- --- ---
    public static SortedBookedMoviesList getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SortedBookedMoviesList(context);
        }

        return instance;
    }

    public static void saveInstance()
    {
        if(instance != null && instance.isDirty())
        {
            instance.save();
        }
    }

    // --- --- --- Dirty data methods --- --- ---
    public List<Movie> getAndAddDifference(List<Movie> bookedMovies)
    {
        List<Movie> difference = new ArrayList<>();

        for(Movie newMovie : bookedMovies)
        {
            if(isNewAndAdd(newMovie.getMediaBarcode()))
            {
                difference.add(newMovie);
            }
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
                this.dirty = true;
                return true;
            }
        }

        this.bookedMovies.add(newMovie);
        this.dirty = true;
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
                this.dirty = true;
                return true;
            }

            position++;
        }

        return false;
    }

    // --- --- --- Clean data methods --- --- ---
    public boolean isDirty()
    {
        return this.dirty;
    }

    // --- --- --- File helper methods --- --- ---
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
        this.dirty = false;
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
