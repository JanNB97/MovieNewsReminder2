package com.yellowbite.movienewsreminder2.newsService;

import android.content.Context;

import com.yellowbite.movienewsreminder2.files.data.HotMoviesSortedList;
import com.yellowbite.movienewsreminder2.files.data.NewMoviesQueue;
import com.yellowbite.movienewsreminder2.files.data.NewestMovie;
import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.newsService.messages.AddedMovieMessage;
import com.yellowbite.movienewsreminder2.newsService.messages.HotMovieMessage;
import com.yellowbite.movienewsreminder2.newsService.messages.WebScraperMessage;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedZenHandler extends WebscrapingHandler
{
    public static final String MED_ZEN_NEW_MOVIES_5_URL = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=cFM9NSZhbXA7U29ydD1adWdhbmdzZGF0dW0gKEJpYmxpb3RoZWspJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD02MyUlb18wPTYlJXZfMD0yNS4wOC4yMDE2IDAwOjAwOjAwKytjXzE9MSUlbV8xPTElJWZfMT00MiUlb18xPTElJXZfMT00NlMtRFZEIChTcGllbGZpbG0pKytjXzI9MSUlbV8yPTElJWZfMj00OCUlb18yPTElJXZfMj1NZWRpZW56ZW50cnVtIEhlcnNmZWxkLVJvdGVuYnVyZyZhbXA7Y21kPTE=-vg8oXcbJ4nw=";
    private static final String MED_ZEN_NEW_MOVIES_50_URL = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=cFM9NTAmYW1wO1NvcnQ9WnVnYW5nc2RhdHVtIChCaWJsaW90aGVrKSZhbXA7c0M9Y18wPTElJW1fMD0xJSVmXzA9NjMlJW9fMD02JSV2XzA9MjUuMDguMjAxNiAwMDowMDowMCsrY18xPTElJW1fMT0xJSVmXzE9NDIlJW9fMT0xJSV2XzE9NDZTLURWRCAoU3BpZWxmaWxtKSsrY18yPTElJW1fMj0xJSVmXzI9NDglJW9fMj0xJSV2XzI9TWVkaWVuemVudHJ1bSBIZXJzZmVsZC1Sb3RlbmJ1cmcmYW1wO2NtZD0x-g98+BeyN9rU=";

    public MedZenHandler()
    {
        super("Medienzentrum");
    }

    @Override
    public List<WebScraperMessage> checkForNews(Context context)
    {
        List<WebScraperMessage> messages = new ArrayList<>();

        WebScraperMessage addedMovieMessage = this.checkForAddedMovies(context);
        if(addedMovieMessage != null)
        {
            messages.add(addedMovieMessage);
        }

        WebScraperMessage hotMovieMessage = this.checkForHotMovies(context);
        if(hotMovieMessage != null)
        {
            messages.add(hotMovieMessage);
        }

        return messages;
    }

    private WebScraperMessage checkForAddedMovies(Context context)
    {
        MedZenMovieListScraper listScraper;
        try
        {
            listScraper = new MedZenMovieListScraper(MED_ZEN_NEW_MOVIES_5_URL);
        } catch (IOException e)
        {
            // No internet connection
            e.printStackTrace();
            return null;
        }

        Movie thisMovie = listScraper.getEssentialMovie(0);
        int thisBarcode = thisMovie.getMediaBarcode();
        int lastBarcode = NewestMovie.getBarcode(context);

        if(lastBarcode == -1 || thisBarcode != lastBarcode)
        {
            int numOfNewMovies = this.writeNewMoviesInFile(context, listScraper, thisMovie, lastBarcode);

            NewestMovie.setBarcode(context, thisBarcode);
            return new AddedMovieMessage(numOfNewMovies);
        }
        else
        {
            return null;
        }
    }

    private WebScraperMessage checkForHotMovies(Context context)
    {
        List<Movie> verfuegbarHotMovies = new ArrayList<>();

        for (Movie hotMovie : HotMoviesSortedList.get(context))
        {
            try
            {
                if(MedZenMovieSiteScraper.isVerfuegbar(hotMovie))
                {
                    // TODO - Proove if message was already send for this movie
                    verfuegbarHotMovies.add(hotMovie);
                }
            } catch (IOException ignored) {}
        }

        if(verfuegbarHotMovies.isEmpty())
        {
            return null;
        }
        else
        {
            return new HotMovieMessage(verfuegbarHotMovies);
        }
    }

    private int writeNewMoviesInFile(Context context, MedZenMovieListScraper listScraper, Movie newestMovie, int lastNewestBarcode)
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

            if(i == listScraper.getListEntrySize() - 1)
            {
                // Reached end of list
                try
                {
                    listScraper = new MedZenMovieListScraper(MED_ZEN_NEW_MOVIES_50_URL);
                } catch (IOException e)
                {
                    break;
                }
            }
        }

        NewMoviesQueue.addAll(context, newMovies);
        return newMovies.size();
    }
}
