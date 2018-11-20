package com.yellowbite.movienewsreminder2.files.datatypes.fromfile;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.helper.FileManager;

public final class NewestMovie
{
    private static final String NEWEST_BARCODE = "newestBarcode.txt";

    public static int getBarcode(Context context)
    {
        String s = FileManager.read(context, NEWEST_BARCODE);

        if(s == null)
        {
            return -1;
        }

        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ignored)
        {
            return -1;
        }
    }

    public static void setBarcode(Context context, int barcode)
    {
        FileManager.write(context, NEWEST_BARCODE, Integer.toString(barcode));
    }
}
