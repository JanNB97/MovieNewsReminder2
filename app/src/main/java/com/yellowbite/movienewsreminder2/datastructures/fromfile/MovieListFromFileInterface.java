package com.yellowbite.movienewsreminder2.datastructures.fromfile;

import com.yellowbite.movienewsreminder2.datastructures.MovieList;

public interface MovieListFromFileInterface extends MovieList
{
    void save();
    boolean isDirty();
    void setDirty(boolean isDirty);
}
