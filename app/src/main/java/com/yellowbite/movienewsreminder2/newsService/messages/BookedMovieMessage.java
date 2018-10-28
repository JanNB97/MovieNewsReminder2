package com.yellowbite.movienewsreminder2.newsService.messages;

import com.yellowbite.movienewsreminder2.R;

public class BookedMovieMessage extends WebScraperMessage
{
    public BookedMovieMessage(int numNewMovies)
    {
        super(Message.BOOKED_MOVIES, getTitle(numNewMovies), "", R.drawable.addedmoviesicon, false);
    }

    private static String getTitle(int numNewMovies)
    {
        if(numNewMovies == 1)
        {
            return "Ein neuer Film wurde bestellt";
        }
        else
        {
            return numNewMovies + " neue Filme wurden bestellt";
        }
    }
}
