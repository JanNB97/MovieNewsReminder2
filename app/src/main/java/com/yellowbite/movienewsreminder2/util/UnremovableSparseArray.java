package com.yellowbite.movienewsreminder2.util;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnremovableSparseArray<E> extends SparseArray<E>
{
    private List<E> values = new ArrayList<>();

    @Override
    public void put(int key, E value)
    {
        super.put(key, value);
        this.values.add(value);
    }

    @Override
    public void remove(int key) {}

    @Override
    public void removeAt(int index) {}

    @Override
    public void removeAtRange(int index, int size) {}

    @Override
    public void delete(int key) {}

    public Collection<E> values()
    {
        return this.values;
    }
}
