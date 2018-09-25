package com.yellowbite.movienewsreminder2.tasks.functionalInterfaces;

@FunctionalInterface
public interface LoadedMoviesEvent
{
    void loadedMovies(int numOfMovies);
}
