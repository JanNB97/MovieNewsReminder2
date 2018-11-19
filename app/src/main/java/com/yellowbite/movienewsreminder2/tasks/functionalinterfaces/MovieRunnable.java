package com.yellowbite.movienewsreminder2.tasks.functionalinterfaces;

import com.yellowbite.movienewsreminder2.data.Movie;

@FunctionalInterface
public interface MovieRunnable
{
    void run(Movie movie);
}