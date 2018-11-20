package com.yellowbite.movienewsreminder2.datastructures;

import com.yellowbite.movienewsreminder2.data.Movie;

import java.util.List;

public interface MovieList
{
    Movie get(int i);
    List<Movie> getAll();

    void addAll(List<Movie> movies);
    boolean add(Movie movie);

    void remove(int i);
    void remove(Movie movie);
    void removeLast();

    boolean contains(Movie movie);

    int size();

    boolean isEmpty();
}
