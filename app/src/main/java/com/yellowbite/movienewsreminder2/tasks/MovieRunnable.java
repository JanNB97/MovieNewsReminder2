package com.yellowbite.movienewsreminder2.tasks;

import com.yellowbite.movienewsreminder2.model.Movie;

@FunctionalInterface
public interface MovieRunnable
{
    void run(Movie movie);
}