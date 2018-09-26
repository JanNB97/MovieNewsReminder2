package com.yellowbite.movienewsreminder2.files.datatypes;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

public interface MovieList
{
    Movie get(Context context, int i);
    List<Movie> getAll(Context context);

    void addAll(Context context, List<Movie> movies);
    boolean add(Context context, Movie movie);

    void remove(Context context, int i);

    void save(Context context);

    int size(Context context);
}
