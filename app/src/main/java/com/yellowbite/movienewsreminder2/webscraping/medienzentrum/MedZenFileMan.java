package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.util.FileManager;

import java.util.List;

public class MedZenFileMan
{
    private static final String NEW_MOVIE_LIST_FILE_NAME = "newMovieList.txt";

    // --- Barcode of the last movie, which was added to the database ---
    public static int getLastBarcode(Context context)
    {
        List<String> barcodes = FileManager.read(context, NEW_MOVIE_LIST_FILE_NAME);

        if(barcodes.size() > 0)
        {
            return Integer.parseInt(barcodes.get(0).split(";")[0]);
        }
        else
        {
            return -1;
        }
    }

    public static void setLastBarcode(Context context, int barcode, String url)
    {
        FileManager.write(context, NEW_MOVIE_LIST_FILE_NAME, barcode + ";" + url);
    }
}
