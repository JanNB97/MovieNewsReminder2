package com.yellowbite.movienewsreminder2.newsService.messages;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

public class HotMovieMessage extends WebScraperMessage
{
    public HotMovieMessage(int numOfVerfuegbarHotMovies, List<Movie> hotVerfuegbarMovies)
    {
        super(toTitel(hotVerfuegbarMovies), numOfVerfuegbarHotMovies + " begehrte Filme sind verfügbar", R.drawable.ic_launcher_foreground);
    }

    private static String toTitel(List<Movie> hotVerfuegbarMovies)
    {
        if (hotVerfuegbarMovies.size() == 1)
        {
            return hotVerfuegbarMovies.get(0).getTitel() + " ist verfügbar";
        }
        else
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < hotVerfuegbarMovies.size(); i++)
            {
                builder.append(hotVerfuegbarMovies.get(i).getTitel());
                if(i < hotVerfuegbarMovies.size() - 2)
                {
                    builder.append(", ");
                }
                else if(i == hotVerfuegbarMovies.size() - 1)
                {
                    builder.append(" und ");
                }
            }
            builder.append(" sind verfügbar");
            return builder.toString();
        }
    }
}
