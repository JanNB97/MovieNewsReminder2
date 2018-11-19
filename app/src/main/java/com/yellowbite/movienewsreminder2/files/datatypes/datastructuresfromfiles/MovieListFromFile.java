package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieListFromFileInterface;
import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MovieListFromFile implements MovieListFromFileInterface
{
    protected final String FILE_NAME;
    protected List<Movie> movieList;
    protected Context context;
    protected boolean dirty;

    protected MovieListFromFile(Context context, String fileName)
    {
        this.FILE_NAME = fileName;
        this.context = context;
        this.getFromFile(context);
    }

    // --- --- --- clean data methods --- --- ---
    @Override
    public Movie get(int i)
    {
        return this.movieList.get(i);
    }

    @Override
    public List<Movie> getAll()
    {
        return this.movieList;
    }

    @Override
    public int size()
    {
        return this.movieList.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    @Override
    public boolean isDirty()
    {
        return this.dirty;
    }

    // --- --- --- dirty data methods --- --- ---
    @Override
    public void addAll(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            if(this.isNew(movie))
            {
                this.movieList.add(movie);
                this.dirty = true;
            }
        }
    }

    @Override
    public boolean add(Movie movie)
    {
        if(!this.isNew(movie))
        {
            return false;
        }

        this.movieList.add(movie);
        this.dirty = true;

        return true;
    }

    @Override
    public void remove(int i)
    {
        this.movieList.remove(i);
        this.dirty = true;
    }

    // --- --- --- file methods --- --- ---
    @Override
    public void save()
    {
        if(movieList != null)
        {
            this.saveToFile();
            this.dirty = false;
        }
    }

    protected final void getFromFile(Context context)
    {
        List<String> lines = FileManager.readAll(context, this.FILE_NAME);
        this.movieList = MovieFileHelper.toMovies(lines, Collections.synchronizedList(new ArrayList<>()));
    }

    protected final void saveToFile()
    {
        FileManager.write(this.context, this.FILE_NAME, MovieFileHelper.toLines(this.movieList));
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
