package com.yellowbite.movienewsreminder2.newsService;

import com.yellowbite.movienewsreminder2.R;

public class AddedMovieMessage extends WebScraperMessage
{
    public AddedMovieMessage(int numNewMovies)
    {
        super(getTitle(numNewMovies), "", R.drawable.ic_launcher_background);
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
