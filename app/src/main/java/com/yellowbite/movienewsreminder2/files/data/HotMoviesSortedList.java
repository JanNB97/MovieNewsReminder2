package com.yellowbite.movienewsreminder2.files.data;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.FileManager;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class HotMoviesSortedList
{
    private static final String FILE_NAME = "hotMovies.txt";

    private static List<Integer> hotMovies;

    public static boolean addSave(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);
        movie.setHot(true);

        boolean success = addSorted(movie.getMediaBarcode());
        if(success)
        {
            saveToFile(context);
        }
        return success;
    }

    public static boolean switchSave(Context context, Movie movie)
    {
        if(movie.isHot())
        {
            return deleteSave(context, movie);
        }
        else
        {
            return addSave(context, movie);
        }
    }

    public static boolean deleteSave(Context context, Movie movie)
    {
        getFromFileIfNecessary(context);
        movie.setHot(false);
        boolean success = delSorted(movie.getMediaBarcode());
        if(success)
        {
            saveToFile(context);
        }
        return success;
    }

    public static void getMyHotMovies(Context context)
    {
        getFromFileIfNecessary(context);
        if(hotMovies.isEmpty())
        {
            return;
        }
        List<Movie> myMovies = MyMoviesSortedList.getAll(context);
        List<Integer> hotMoviesToDel = clone(hotMovies);

        for(Movie movie : myMovies)
        {
            int i = getIdInList(movie.getMediaBarcode());
            if(i != -1)
            {
                movie.setHot(true);
                hotMoviesToDel.set(i, -1);
            }
        }

        for(int i = 0; i < hotMoviesToDel.size(); i++)
        {
            int barcode = hotMoviesToDel.get(i);

            if(barcode != -1)
            {
                hotMoviesToDel.remove(i);
            }
        }
    }

    // --- --- --- data type operations --- --- ---

    private static int getIdInList(int barcode)
    {
        int i = 0;
        for(Integer b2 : hotMovies)
        {
            if(b2 == barcode)
            {
                return i;
            }

            if(b2 > barcode)
            {
                return -1;
            }

            i++;
        }

        return -1;
    }

    private static boolean addSorted(int barcode)
    {
        int i = 0;
        for(Integer b2 : hotMovies)
        {
            if(b2 == barcode)
            {
                return false;
            }

            if(b2 > barcode)
            {
                hotMovies.add(i, barcode);
                return true;
            }

            i++;
        }
        hotMovies.add(barcode);
        return true;
    }

    private static boolean delSorted(int barcode)
    {
        int i = getIdInList(barcode);
        boolean success = i != -1;

        if(success)
        {
            hotMovies.remove(i);
        }
        return success;
    }

    // --- --- --- file operations --- --- ---

    private static List<Integer> clone(List<Integer> list)
    {
        List<Integer> newList = new ArrayList<>();
        for(Integer integer : list)
        {
            newList.add(Integer.valueOf(integer));
        }
        return newList;
    }

    private static void getFromFileIfNecessary(Context context)
    {
        hotMovies = toList(FileManager.readAll(context, FILE_NAME));
    }

    private static List<Integer> toList(List<String> strings)
    {
        List<Integer> intergerList = new ArrayList<>();

        for(String string : strings)
        {
            try
            {
                int i = Integer.parseInt(string);
                intergerList.add(i);
            }
            catch (NumberFormatException ignored) {}
        }

        return intergerList;
    }

    private static List<String> toLines(List<Integer> barcodes)
    {
        List<String> lines = new ArrayList<>();

        for (Integer i : barcodes)
        {
            lines.add(i.toString());
        }

        return lines;
    }

    private static void saveToFile(Context context)
    {
        FileManager.write(context, FILE_NAME, toLines(hotMovies));
    }
}
