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
    private BoolRunnable onFinishedLoading;

    private String searchEntry;

    private boolean successSearch;

    public interface BoolRunnable
    {
        void run(boolean gotResults);
    }

    public LoadMovieListExecutor(AppCompatActivity activity, BoolRunnable onSiteLoaded, BoolRunnable onFinishedLoading)
    {
        this.activity = activity;

        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        this.onSiteLoaded = onSiteLoaded;
        this.onFinishedLoading = onFinishedLoading;
    }

    public void startToLoadMovieList(String searchEntry)
    {
        this.searchEntry = searchEntry;
        this.successSearch = false;
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
            if(gotResult)
            {
                this.successSearch = true;
            }

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

        this.executor.execute(() -> this.loadMovieList(urlToNextPage, page + 1, maxPages));
    }

    private void notifyMovieLoaded(int page, int maxPages, boolean gotResults)
    {
        this.activity.runOnUiThread(() -> {
            this.onSiteLoaded.run(gotResults);

            if(page >= maxPages)
            {
                if(!this.successSearch)
                {
                    this.showUnsuccessfulSearch();
                }

                this.onFinishedLoading.run(this.successSearch);
            }
        });
    }

    private void finishWithoutResults()
    {
        this.activity.runOnUiThread(() -> {
            this.showUnsuccessfulSearch();
            this.onFinishedLoading.run(false);
        });
    }

    private void showUnsuccessfulSearch()
    {
        NotificationMan.showShortToast(this.activity, "Keine Treffer gefunden");
    }

    private void handleExceptionWhileWebscraping(Exception e)
    {
        e.printStackTrace();
        this.activity.runOnUiThread(() -> {
            NotificationMan.showShortToast(this.activity, "Es ist ein Fehler aufgetreten");
            this.onFinishedLoading.run(false);
        });
    }
}
