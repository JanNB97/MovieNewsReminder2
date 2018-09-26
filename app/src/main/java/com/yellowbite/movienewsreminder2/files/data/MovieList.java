package com.yellowbite.movienewsreminder2.files.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

public abstract class MovieList
{
    public abstract Movie get(Context context, int i);

    public abstract List<Movie> getAll(Context context);

    public abstract void addAll(Context context, List<Movie> movies);

    public abstract boolean add(Context context, Movie movie);

    public abstract void remove(Context context, int i);

    public abstract void save(Context context);
}
