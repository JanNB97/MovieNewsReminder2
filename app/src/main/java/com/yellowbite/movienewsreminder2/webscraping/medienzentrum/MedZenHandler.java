package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHandler;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class MedZenHandler extends WebscrapingHandler
{
    public static final String MED_ZEN_NEW_MOVIES_LINK = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=Y21kPTUmYW1wO3NDPWNfMD0xJSVtXzA9MSUlZl8wPTYzJSVvXzA9NiUldl8wPTI1LjA4LjIwMTYgMDA6MDA6MDArK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkrK2NfMj0xJSVtXzI9MSUlZl8yPTQ4JSVvXzI9MSUldl8yPU1lZGllbnplbnRydW0gSGVyc2ZlbGQtUm90ZW5idXJnJmFtcDtTb3J0PVp1Z2FuZ3NkYXR1bSAoQmlibGlvdGhlayk=-B/ZW6RDg8Xg=";

    public MedZenHandler() {
        super("Medienzentrum");
    }

    @Override
    public boolean hasUpdated(Context context)
    {
        MedZenMovieListScraper listScraper = null;
        try
        {
            listScraper = new MedZenMovieListScraper(MED_ZEN_NEW_MOVIES_LINK);
        } catch (IOException e)
        {
            // No internet connection
            e.printStackTrace();
            return false;
        }


        int thisBarcode = listScraper.getMediaBarcode(0);
        int lastBarcode = MedZenFileMan.getLastBarcode(context);

        if(lastBarcode == -1 || thisBarcode != lastBarcode)
        {
            // TODO - Write new movies in file

            MedZenFileMan.setLastBarcode(context, thisBarcode);
            return true;
        }
        else
        {
            return false;
        }
    }
}
