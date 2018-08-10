package com.yellowbite.movienewsreminder2.newsService.messages;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.model.Movie;

import java.util.List;

public class HotMovieMessage extends WebScraperMessage
{
    public HotMovieMessage(List<Movie> hotVerfuegbarMovies)
    {
        super(toTitel(hotVerfuegbarMovies), toText(hotVerfuegbarMovies), R.drawable.ic_launcher_foreground, true);
    }

    private static String toTitel(List<Movie> hotVerfuegbarMovies)
    {
        if (hotVerfuegbarMovies.size() == 1)
        {
            return "Ein begehrte Film ist verfügbar";
        }
        else
        {
            return hotVerfuegbarMovies.size() + " begehrte Filme sind verfügbar";
        }
    }

    private static String toText(List<Movie> hotVerfuegbarMovies)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < hotVerfuegbarMovies.size(); i++)
        {
            builder.append(hotVerfuegbarMovies.get(i).getTitel());
            if(i != hotVerfuegbarMovies.size() - 1)
            {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
