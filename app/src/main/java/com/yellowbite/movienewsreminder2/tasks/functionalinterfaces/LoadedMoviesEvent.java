package com.yellowbite.movienewsreminder2.tasks.functionalinterfaces;

@FunctionalInterface
public interface LoadedMoviesEvent
{
    void loadedMovies(int numOfMovies);
}
