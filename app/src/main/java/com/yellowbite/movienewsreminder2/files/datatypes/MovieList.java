package com.yellowbite.movienewsreminder2.files.datatypes;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

public interface MovieList
{
    Movie get(int i);
    List<Movie> getAll();

    void addAll(List<Movie> movies);
    boolean add(Movie movie);

    void remove(int i);

    void save();

    int size();
}
