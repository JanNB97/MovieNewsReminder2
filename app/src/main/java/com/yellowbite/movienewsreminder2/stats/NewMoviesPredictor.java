package com.yellowbite.movienewsreminder2.stats;

import com.yellowbite.movienewsreminder2.newsService.MedZenHandler;
import com.yellowbite.movienewsreminder2.newsService.NewsService;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class NewMoviesPredictor
{
    private static final int QUARTER_YEAR = 91;
    private static final int HALF_YEAR = 182;
    private static final int ONE_YEAR = 365;

    public static final int DISTANCE_FOR_STATS = QUARTER_YEAR;

    public static void getPrediction()
    {
        try
        {
            getPredictionHelper();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void getPredictionHelper() throws IOException
    {
        String newMoviesURL = MedZenHandler.MED_ZEN_NEW_MOVIES_50_URL;

        int newMovies = 0;
        Date lastDate = null;
        Date firstDate = null;

        int sumWaitingDays = 0;
        int sumMovies = 0;
        int numOfMovieLoads = 0;

        outerLoop:
        do
        {
            MedZenMovieListScraper listScraper = new MedZenMovieListScraper(newMoviesURL);

            for(int i = 0; i < listScraper.getListEntrySize(); i++)
            {
                Date date = listScraper.getZugang(i);

                if(date != null && !date.equals(lastDate))
                {
                    if(lastDate != null)
                    {
                        numOfMovieLoads++;
                        int daysUntilLastDate = DateHelper.getDistance(date, lastDate);
                        sumWaitingDays += daysUntilLastDate;
                        sumMovies += newMovies;
                        System.out.println(numOfMovieLoads + ": " + daysUntilLastDate + " days (" + newMovies + " movies)\t"
                                + DateHelper.toString(date) + " - " + DateHelper.toString(lastDate));

                        if(DateHelper.getDistance(lastDate, firstDate) > DISTANCE_FOR_STATS)
                        {
                            break outerLoop;
                        }
                    }
                    else
                    {
                        firstDate = date;
                    }
                    lastDate = date;

                    newMovies = 1;
                }
                else
                {
                    newMovies++;
                }
            }

            newMoviesURL = listScraper.getURLToNextPage();
            System.out.println();
        }
        while (newMoviesURL != null);

        System.out.println("\nFinished with: " + DateHelper.toString(lastDate));
        int averageWaitingTime = Math.round((float)sumWaitingDays / numOfMovieLoads);
        System.out.println("\nAverage waiting time: " + averageWaitingTime);
        System.out.println("Average new movies: " + Math.round((float)sumMovies / numOfMovieLoads));

        System.out.println("Predicted date: " + DateHelper.toString(DateHelper.incrementDays(firstDate, averageWaitingTime)));
    }
}
