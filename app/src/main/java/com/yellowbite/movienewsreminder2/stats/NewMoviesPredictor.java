package com.yellowbite.movienewsreminder2.stats;

import com.yellowbite.movienewsreminder2.newsservice.MedZenHandler;
import com.yellowbite.movienewsreminder2.util.DateHelper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.io.IOException;
import java.util.ArrayList;
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

        ArrayList<Integer> allWaitDays = new ArrayList<>();
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
                        sumMovies += newMovies;
                        System.out.println(numOfMovieLoads + ": " + daysUntilLastDate + " days (" + newMovies + " movies)\t"
                                + DateHelper.toString(date) + " - " + DateHelper.toString(lastDate));
                        allWaitDays.add(daysUntilLastDate);

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

        int sumWaitingDays = 0;
        for(Integer i : allWaitDays)
        {
            sumWaitingDays += i;
        }

        System.out.println("\nFinished with: " + DateHelper.toString(lastDate));
        float averageWaitingTime = (float)sumWaitingDays / numOfMovieLoads;
        System.out.println("\nAverage waiting time: " + averageWaitingTime);
        System.out.println("Average new movies: " + (float)sumMovies / numOfMovieLoads);

        System.out.println("Predicted date: " + DateHelper.toString(DateHelper.incrementDays(firstDate, (int)Math.ceil(averageWaitingTime))));

        ArrayList<Float> allDifToAverageWaitingTime = new ArrayList<>();
        for(Integer waitDays : allWaitDays)
        {
            allDifToAverageWaitingTime.add(Math.abs(averageWaitingTime - waitDays));
        }

        float difToAverageWaitingTimeSum = 0;
        for(Float dif : allDifToAverageWaitingTime)
        {
            difToAverageWaitingTimeSum += dif;
        }
        System.out.println("Abweichung: " + difToAverageWaitingTimeSum / allDifToAverageWaitingTime.size());
    }
}
