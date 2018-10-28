package com.yellowbite.movienewsreminder2.newsService.messages;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HotMovieMessage extends WebScraperMessage
{
    public HotMovieMessage(List<Movie> hotVerfuegbarMovies, List<Movie> shownMovies)
    {
        super(Message.HOT_MOVIE_AVAILABLE, toTitel(hotVerfuegbarMovies, shownMovies),
                toText(hotVerfuegbarMovies, shownMovies),
                R.drawable.hotmovieicon, true);
    }

    private static String toTitel(List<Movie> hotVerfuegbarMovies, List<Movie> shownMovies)
    {
        int numOfMovies = hotVerfuegbarMovies.size() + shownMovies.size();

        if (numOfMovies == 1)
        {
            return "Ein begehrte Film ist verfügbar";
        }
        else
        {
            return numOfMovies + " begehrte Filme sind verfügbar";
        }
    }

    private static String toText(List<Movie> hotVerfuegbarMovies, List<Movie> shownMovies)
    {
        Collections.sort(hotVerfuegbarMovies);
        Collections.sort(shownMovies);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < hotVerfuegbarMovies.size(); i++)
        {
            builder.append(hotVerfuegbarMovies.get(i).getTitel());
            if(hotVerfuegbarMovies.size() != 1 || !shownMovies.isEmpty())
            {
                builder.append(" (Neu)");
            }
            if(i != hotVerfuegbarMovies.size() - 1 || !shownMovies.isEmpty())
            {
                builder.append("\n");
            }
        }

        for(int i = 0; i < shownMovies.size(); i++)
        {
            builder.append(shownMovies.get(i).getTitel());
            if(i != shownMovies.size() - 1)
            {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
