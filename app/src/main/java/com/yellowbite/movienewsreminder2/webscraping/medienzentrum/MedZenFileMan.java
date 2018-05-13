package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.util.FileManager;

import java.util.List;

public class MedZenFileMan
{
    // --- Barcode of the last movie, which was added to the database ---
    public static int getLastBarcode(Context context)
    {
        List<String> barcodes = FileManager.readLines(context, "medienzentrum.txt");

        if(barcodes.size() > 0)
        {
            return Integer.parseInt(barcodes.get(0));
        }
        else
        {
            return -1;
        }
    }

    public static void setLastBarcode(Context context, int barcode)
    {
        FileManager.writeLine(context, "medienzentrum.txt", Integer.toString(barcode), Context.MODE_PRIVATE);
    }
}
