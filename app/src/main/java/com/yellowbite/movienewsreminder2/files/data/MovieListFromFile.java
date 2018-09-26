package com.yellowbite.movienewsreminder2.files.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.FileManager;
import com.yellowbite.movienewsreminder2.files.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MovieListFromFile
{
    private final String FILE_NAME;
    private List<Movie> movieList;

    protected MovieListFromFile(String fileName)
    {
        this.FILE_NAME = fileName;
    }

    // --- --- --- data operations --- --- ---
    // getAll
    public Movie get(Context context, int i)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList.get(i);
    }

    public List<Movie> getAll(Context context)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList;
    }

    // others
    public int size(Context context)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList.size();
    }

    // add
    public void addAll(Context context, List<Movie> movies)
    {
        this.getFromFileIfNecessary(context);

        for(Movie movie : movies)
        {
            if(this.isNew(movie))
            {
                this.movieList.add(movie);
            }
        }

        this.sort();
    }

    public boolean add(Context context, Movie movie)
    {
        this.getFromFileIfNecessary(context);

        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.sort();

        return true;
    }

    // remove
    public void remove(Context context, int i)
    {
        this.getFromFileIfNecessary(context);
        this.movieList.remove(i);
    }

    // others
    protected void sort()
    {
        Collections.sort(this.movieList);
    }

    // --- --- --- file operations --- --- ---

    public void save(Context context)
    {
        if(movieList != null)
        {
            this.saveToFile(context);
        }
    }

    // file helper operations
    protected final void getFromFileIfNecessary(Context context)
    {
        if(this.movieList == null)
        {
            this.movieList = this.getFromFile(context);
        }
    }

    protected final List<Movie> getFromFile(Context context)
    {
        List<String> lines = FileManager.readAll(context, this.FILE_NAME);
        return MovieFileHelper.toMovies(lines, Collections.synchronizedList(new ArrayList<>()));
    }

    protected final void saveToFile(Context context)
    {
        FileManager.write(context, this.FILE_NAME, MovieFileHelper.toLines(this.movieList));
    }

    // --- --- --- helper methods --- --- ---

    protected boolean isNew(Movie movie)
    {
        for(Movie movieInDatabase : this.movieList)
        {
            if(movieInDatabase.getMediaBarcode() == movie.getMediaBarcode())
            {
                return false;
            }
        }

        return true;
    }
}
