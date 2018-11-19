package com.yellowbite.movienewsreminder2.newsservice.messages;

import com.yellowbite.movienewsreminder2.R;

public class AddedMovieMessage extends WebScraperMessage
{
    public AddedMovieMessage(int numNewMovies)
    {
        super(Message.ADDED_MOVIE, getTitle(numNewMovies), "", R.drawable.addedmoviesicon, false);
    }

    private static String getTitle(int numNewMovies)
    {
        if(numNewMovies == 1)
        {
            return "Ein neuer Film wurde hinzugefügt";
        }
        else
        {
            return numNewMovies + " neue Filme wurden hinzugefügt";
        }
    }
}
