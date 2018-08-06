package com.yellowbite.movienewsreminder2.ui.tasks;

import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

@FunctionalInterface
public interface MovieRunnable
{
    void run(Movie movie);
}