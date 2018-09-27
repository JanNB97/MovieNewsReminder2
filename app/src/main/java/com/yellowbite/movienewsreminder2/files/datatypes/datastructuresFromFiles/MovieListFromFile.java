package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresFromFiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieList;
import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MovieListFromFile implements MovieList
{
    private final String FILE_NAME;
    protected List<Movie> movieList;

    protected MovieListFromFile(String fileName)
    {
        this.FILE_NAME = fileName;
    }

    // --- --- --- data operations --- --- ---
    // getAll
    @Override
    public Movie get(Context context, int i)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList.get(i);
    }

    @Override
    public List<Movie> getAll(Context context)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList;
    }

    // others
    @Override
    public int size(Context context)
    {
        this.getFromFileIfNecessary(context);
        return this.movieList.size();
    }

    // add
    @Override
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
    }

    @Override
    public boolean add(Context context, Movie movie)
    {
        this.getFromFileIfNecessary(context);

        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);

        return true;
    }

    // remove
    @Override
    public void remove(Context context, int i)
    {
        this.getFromFileIfNecessary(context);
        this.movieList.remove(i);
    }

    // --- --- --- file operations --- --- ---
    @Override
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