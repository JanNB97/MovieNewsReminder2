package com.yellowbite.movienewsreminder2.files.datatypes;

public interface MovieListFromFileInterface extends MovieList
{
    void save();
    boolean isDirty();
}
