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

    public LoadMovieListExecutor(AppCompatActivity activity, Runnable onSiteLoaded, Runnable onFinishedLoading)
    {
        this.activity = activity;

        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        this.onSiteLoaded = onSiteLoaded;
        this.onFinishedLoading = onFinishedLoading;
    }

    public void startToLoadMovieList(String urlToFirstPage)
    {
        executor.execute(() -> this.loadMovieList(urlToFirstPage, 1, -1));
    }

    private void loadMovieList(String siteURL, int page, int maxPages)
    {
        if(siteURL == null)
        {
            return;
        }

        try
        {
            MedZenMovieListScraper listScraper = new MedZenMovieListScraper(siteURL);
            if(page == 1)
            {
                maxPages = listScraper.getMaxPages();
            }
            String urlToNextPage = listScraper.getURLToNextPage();

            int finalMaxPages = maxPages;
            this.executor.execute(() -> this.loadMovieList(urlToNextPage, page + 1, finalMaxPages));

            SearchMovieList.getInstance().addMovieSite(listScraper);

            this.activity.runOnUiThread(() -> {
                this.onSiteLoaded.run();

                if(page == finalMaxPages)
                {
                    this.onFinishedLoading.run();
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
