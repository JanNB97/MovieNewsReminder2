package com.yellowbite.movienewsreminder2.files.datatypes;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.List;

public interface MovieList
{
    Movie get(int i);
    List<Movie> getAll();

    void addAll(List<Movie> movies);
    boolean add(Movie movie);

    void remove(int i);

    int size();

    boolean isEmpty();
}
