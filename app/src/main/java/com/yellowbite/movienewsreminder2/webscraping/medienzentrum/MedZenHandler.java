package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.util.FileManager;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class MedZenHandler extends WebscrapingHandler
{
    public MedZenHandler() {
        super("Medienzentrum");
    }

    @Override
    public boolean hasUpdated(Context context)
    {
        try {
            Document doc = Jsoup.connect("https://opac.winbiap.net/mzhr/acquisitions.aspx?data=Y21kPTUmYW1wO3NDPWNfMD0xJSVtXzA9MSUlZl8wPTYzJSVvXzA9NiUldl8wPTI1LjA4LjIwMTYgMDA6MDA6MDArK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkrK2NfMj0xJSVtXzI9MSUlZl8yPTQ4JSVvXzI9MSUldl8yPU1lZGllbnplbnRydW0gSGVyc2ZlbGQtUm90ZW5idXJnJmFtcDtTb3J0PVp1Z2FuZ3NkYXR1bSAoQmlibGlvdGhlayk=-B/ZW6RDg8Xg=").maxBodySize(0).get();

            int thisBarcode = MedZenMovieScraper.getMediaBarcode(doc, 0);
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

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
