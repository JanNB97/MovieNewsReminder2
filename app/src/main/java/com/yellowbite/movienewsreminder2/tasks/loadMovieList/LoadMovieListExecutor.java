package com.yellowbite.movienewsreminder2.tasks.loadMovieList;

import android.support.v7.app.AppCompatActivity;

import com.yellowbite.movienewsreminder2.files.datatypes.otherDatastructures.SearchMovieList;
import com.yellowbite.movienewsreminder2.ui.notifications.NotificationMan;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingHelper;
import com.yellowbite.movienewsreminder2.webscraping.medienzentrum.MedZenMovieListScraper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoadMovieListExecutor
{
    private AppCompatActivity activity;
    private ThreadPoolExecutor executor;

    private BoolRunnable onSiteLoaded;
    private Runnable onFinishedLoading;

    private String searchEntry;

    public interface BoolRunnable
    {
        void run(boolean gotResults);
    }

    public LoadMovieListExecutor(AppCompatActivity activity, BoolRunnable onSiteLoaded, Runnable onFinishedLoading)
    {
        this.activity = activity;

        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        this.onSiteLoaded = onSiteLoaded;
        this.onFinishedLoading = onFinishedLoading;
    }

    public void startToLoadMovieList(String searchEntry)
    {
        this.searchEntry = searchEntry;
        String searchULR = WebscrapingHelper.getWideSearchURL(searchEntry);
        executor.execute(() -> this.loadMovieList(searchULR, 1, -1));
    }

    private void loadMovieList(String siteURL, int page, int maxPages)
    {
        try
        {
            MedZenMovieListScraper listScraper = new MedZenMovieListScraper(siteURL);
            listScraper.setSearchFilter(this.searchEntry);
            if(listScraper.isEmpty())
            {
                this.finishWithoutResults();
                return;
            }

            if(page == 1)
            {
                maxPages = listScraper.getMaxPages();
            }

            this.scrapeNextPage(listScraper, page, maxPages);

            boolean gotResult = SearchMovieList.getInstance().addMovieSite(listScraper);

            this.notifyMovieLoaded(page, maxPages, gotResult);
        } catch (IOException e)
        {
            this.handleExceptionWhileWebscraping(e);
        }
    }

    private void scrapeNextPage(MedZenMovieListScraper listScraper, int page, int maxPages)
    {
        String urlToNextPage = listScraper.getURLToNextPage();

        if(urlToNextPage == null)
        {
            return;
        }

        int finalMaxPages = maxPages;
        this.executor.execute(() -> this.loadMovieList(urlToNextPage, page + 1, finalMaxPages));
    }

    private void notifyMovieLoaded(int page, int maxPages, boolean gotResults)
    {
        this.activity.runOnUiThread(() -> {
            this.onSiteLoaded.run(gotResults);

            if(page >= maxPages)
            {
                this.onFinishedLoading.run();
            }
        });
    }

    private void finishWithoutResults()
    {
        this.activity.runOnUiThread(() -> {
            NotificationMan.showShortToast(this.activity, "Keine Treffer gefunden");
            this.onFinishedLoading.run();
        });
    }

    private void handleExceptionWhileWebscraping(Exception e)
    {
        e.printStackTrace();
        this.activity.runOnUiThread(() -> {
            NotificationMan.showShortToast(this.activity, "Es ist ein Fehler aufgetreten");
            this.onFinishedLoading.run();
        });
    }
}
