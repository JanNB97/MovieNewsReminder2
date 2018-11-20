package com.yellowbite.movienewsreminder2.files.datatypes.datastructuresfromfiles;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yellowbite.movienewsreminder2.files.datatypes.MovieListFromFileInterface;
import com.yellowbite.movienewsreminder2.files.helper.FileManager;
import com.yellowbite.movienewsreminder2.files.helper.MovieFileHelper;
import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class UnsortedMovieListFromFile extends MovieListFromFile
{
    protected UnsortedMovieListFromFile(Context context, String fileName)
    {
        super(context, fileName);
    }

    // --- --- --- dirty data methods --- --- ---
    @Override
    public void addAll(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            this.add(movie);
        }
    }

    @Override
    public boolean add(Movie movie)
    {
        if(this.contains(movie))
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

    @Override
    public void removeLast()
    {
        this.remove(this.size() - 1);
    }
}
