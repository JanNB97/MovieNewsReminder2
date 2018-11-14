package com.yellowbite.movienewsreminder2.tasks.functionalInterfaces;

import com.yellowbite.movienewsreminder2.data.Movie;

@FunctionalInterface
public interface MovieRunnable
{
    void run(Movie movie);
}