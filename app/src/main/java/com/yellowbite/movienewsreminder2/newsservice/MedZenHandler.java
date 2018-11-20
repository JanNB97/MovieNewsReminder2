package com.yellowbite.movienewsreminder2.newsservice;

import android.content.Context;

import com.yellowbite.movienewsreminder2.datastructures.fromfile.sorted.SortedMyMoviesList;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.unsorted.NewMovieQueue;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.singlevalue.NewestMovie;
import com.yellowbite.movienewsreminder2.datastructures.fromfile.SortedBookedMovieList;
import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.files.MovieFileHelper;
import com.yellowbite.movienewsreminder2.newsservice.messages.AddedMovieMessage;
import com.yellowbite.movienewsreminder2.newsservice.messages.BookedMovieMessage;
import com.yellowbite.movienewsreminder2.newsservice.messages.HotMovieMessage;
import com.yellowbite.movienewsreminder2.newsservice.messages.WebScraperMessage;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieSiteScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedZenHandler extends WebscrapingHandler
{
    public static final String MED_ZEN_NEW_MOVIES_5_URL = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=cFM9NSZhbXA7U29ydD1adWdhbmdzZGF0dW0gKEJpYmxpb3RoZWspJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD02MyUlb18wPTYlJXZfMD0yNS4wOC4yMDE2IDAwOjAwOjAwKytjXzE9MSUlbV8xPTElJWZfMT00MiUlb18xPTElJXZfMT00NlMtRFZEIChTcGllbGZpbG0pKytjXzI9MSUlbV8yPTElJWZfMj00OCUlb18yPTElJXZfMj1NZWRpZW56ZW50cnVtIEhlcnNmZWxkLVJvdGVuYnVyZyZhbXA7Y21kPTE=-vg8oXcbJ4nw=";
    public static final String MED_ZEN_NEW_MOVIES_50_URL = "https://opac.winbiap.net/mzhr/acquisitions.aspx?data=cFM9NTAmYW1wO1NvcnQ9WnVnYW5nc2RhdHVtIChCaWJsaW90aGVrKSZhbXA7c0M9Y18wPTElJW1fMD0xJSVmXzA9NjMlJW9fMD02JSV2XzA9MjUuMDguMjAxNiAwMDowMDowMCsrY18xPTElJW1fMT0xJSVmXzE9NDIlJW9fMT0xJSV2XzE9NDZTLURWRCAoU3BpZWxmaWxtKSsrY18yPTElJW1fMj0xJSVmXzI9NDglJW9fMj0xJSV2XzI9TWVkaWVuemVudHJ1bSBIZXJzZmVsZC1Sb3RlbmJ1cmcmYW1wO2NtZD0x-g98+BeyN9rU=";

    public static final String MED_ZEN_ALL_BOOKED_MOVIES = "https://opac.winbiap.net/mzhr/search.aspx?data=cEk9MCZhbXA7Y21kPTEmYW1wO1NvcnQ9RXJzY2hlaW51bmdzamFociZhbXA7c0M9Y18wPTElJW1fMD0xJSVmXzA9NDIlJW9fMD0xJSV2XzA9NDZTLURWRCAoU3BpZWxmaWxtKSZhbXA7U2hvd0FsbD0xJmFtcDtmQz0zJSVpbiBCZWFyYmVpdHVuZyZhbXA7cFM9MTAw-b/sJwn4F20I=";

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

        WebScraperMessage bookedMovieMessage = this.checkForBookedMovies(context);
        if(bookedMovieMessage != null)
        {
            messages.add(bookedMovieMessage);
        }

        WebScraperMessage hotMovieMessage = this.checkForHotMovies(context);
        if(hotMovieMessage != null)
        {
            messages.add(hotMovieMessage);
        }

        MovieFileHelper.startSaveAllThread();
        return messages;
    }

    private WebScraperMessage checkForBookedMovies(Context context)
    {
        List<Movie> bookedMovies;
        try
        {
            bookedMovies = MedZenMovieListScraper.getAllEssentialMoviesAndNextPages(MED_ZEN_ALL_BOOKED_MOVIES);
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        List<Movie> newBookedMovies = SortedBookedMovieList.getInstance(context)
                .getAndAddDifference(bookedMovies);

        NewMovieQueue.getInstance(context).addAll(newBookedMovies);

        if(newBookedMovies.isEmpty())
        {
            return null;
        }

        return new BookedMovieMessage(newBookedMovies.size());
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

        if(listScraper.isEmpty())
        {
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
        List<Movie> shownMovies = new ArrayList<>();

        getVerfuegbarHotMovies(context, verfuegbarHotMovies, shownMovies);

        if(verfuegbarHotMovies.isEmpty())
        {
            return null;
        }
        else
        {
            return new HotMovieMessage(verfuegbarHotMovies, shownMovies);
        }
    }

    private void getVerfuegbarHotMovies(Context context, List<Movie> verfuegbarHotMovies, List<Movie> shownMovies)
    {
        for (Movie hotMovie : SortedMyMoviesList.getInstance(context).getAll())
        {
            if(!hotMovie.isHot())
            {
                continue;
            }
            try
            {
                if(MedZenMovieSiteScraper.isVerfuegbar(hotMovie))
                {
                    if(!hotMovie.notificationWasShown())
                    {
                        verfuegbarHotMovies.add(hotMovie);
                        hotMovie.setNotificationWasShown(context, true);
                    }
                    else
                    {
                        shownMovies.add(hotMovie);
                    }
                }
                else if (hotMovie.notificationWasShown())
                {
                    // again unavailable
                    hotMovie.setNotificationWasShown(context, false);
                }
            } catch (IOException ignored) {}
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

        NewMovieQueue.getInstance(context).addAll(newMovies);
        return newMovies.size();
    }
}
