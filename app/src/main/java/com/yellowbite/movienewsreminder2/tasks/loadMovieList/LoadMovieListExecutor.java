package com.yellowbite.movienewsreminder2.tasks.loadMovieList;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoadMovieListExecutor
{
    private AppCompatActivity activity;
    private ThreadPoolExecutor executor;

    private Runnable onSiteLoaded;
    private Runnable onFinishedLoading;

    private int maxPages;

    public LoadMovieListExecutor(AppCompatActivity activity, String urlToFirstSite, Runnable onSiteLoaded, Runnable onFinishedLoading)
    {
        this.activity = activity;
        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        this.onSiteLoaded = onSiteLoaded;
        this.onFinishedLoading = onFinishedLoading;

        executor.execute(() -> this.loadMovieList(urlToFirstSite));
    }

    private void loadMovieList(String siteURL)
    {
        if(siteURL == null)
        {
            return;
        }

        try
        {
            MedZenMovieListScraper listScraper = new MedZenMovieListScraper(siteURL);
            String urlToNextPage = listScraper.getURLToNextPage();

            executor.execute(() -> this.loadMovieList(urlToNextPage));

            SearchMovieList.getInstance().addMovieSite(listScraper);

            this.activity.runOnUiThread(() -> this.onSiteLoaded.run());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
