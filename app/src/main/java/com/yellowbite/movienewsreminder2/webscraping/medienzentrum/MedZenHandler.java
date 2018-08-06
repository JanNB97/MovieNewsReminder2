package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import android.content.Context;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedZenHandler extends WebscrapingHandler
{
    public static final String MED_ZEN_NEW_MOVIES_URL = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=Y21kPTUmYW1wO3NDPWNfMD0xJSVtXzA9MSUlZl8wPTYzJSVvXzA9NiUldl8wPTI1LjA4LjIwMTYgMDA6MDA6MDArK2NfMT0xJSVtXzE9MSUlZl8xPTQyJSVvXzE9MSUldl8xPTQ2Uy1EVkQgKFNwaWVsZmlsbSkrK2NfMj0xJSVtXzI9MSUlZl8yPTQ4JSVvXzI9MSUldl8yPU1lZGllbnplbnRydW0gSGVyc2ZlbGQtUm90ZW5idXJnJmFtcDtTb3J0PVp1Z2FuZ3NkYXR1bSAoQmlibGlvdGhlayk=-B/ZW6RDg8Xg=";

    public MedZenHandler()
    {
        super("Medienzentrum");
    }

    @Override
    public boolean hasUpdated(Context context)
    {
        MedZenMovieListScraper listScraper;
        try
        {
            listScraper = new MedZenMovieListScraper(MED_ZEN_NEW_MOVIES_URL);
        } catch (IOException e)
        {
            // No internet connection
            e.printStackTrace();
            return false;
        }

        Movie thisMovie = listScraper.getEssentialMovie(0);
        int thisBarcode = thisMovie.getMediaBarcode();
        int lastBarcode = MedZenFileMan.getNewestBarcode(context);

        if(lastBarcode == -1 || thisBarcode != lastBarcode)
        {
            this.writeNewMoviesInFile(context, listScraper, thisMovie, lastBarcode);

            MedZenFileMan.setNewestBarcode(context, thisBarcode);
            return true;
        }
        else
        {
            return false;
        }
    }

    private void writeNewMoviesInFile(Context context, MedZenMovieListScraper listScraper, Movie newestMovie, int lastNewestBarcode)
    {
        List<Movie> newMovies = new ArrayList<>();
        newMovies.add(newestMovie);

        for(int i = 1; i < listScraper.getListEntrySize(); i++)
        {
            Movie movie = listScraper.getEssentialMovie(i);

            if(movie.getMediaBarcode() != lastNewestBarcode)
            {
                newMovies.add(movie);
            }
            else
            {
                break;
            }
        }

        MedZenFileMan.addNewMovies(context, newMovies);
    }
}
