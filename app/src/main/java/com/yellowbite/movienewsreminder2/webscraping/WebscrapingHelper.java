package com.yellowbite.movienewsreminder2.webscraping;

import com.yellowbite.movienewsreminder2.model.enums.Status;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class WebscrapingHelper
{
    public static Future<Document> getFutureDoc(String url)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> Jsoup.connect(url).maxBodySize(0).get());
    }

    public static Document getDoc(String url) throws IOException
    {
        return Jsoup.connect(url).maxBodySize(0).get();
    }

    // --- Returning text ---

    public static Status getStatus(Element element, String cssQuery)
    {
        if(element == null)
        {
            return null;
        }

        String text = getText(element, cssQuery);

        if(text == null)
        {
            return null;
        }
        else
        {
            for(Status status : Status.values())
            {
                if(status.getValue().equals(text))
                {
                    return status;
                }
            }

            Logger.getGlobal().severe("No fitting status has been found for " + text);
            return null;
        }
    }

    public static String getText(Element element, String cssQuery)
    {
        return getText(element, cssQuery, 0);
    }

    public static String getText(Element element, String cssQuery, int index)
    {
        if(element == null)
        {
            return null;
        }

        Elements elements = element.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.get(index).text();
    }

    public static int getInt(Element element, String cssQuery)
    {
        if(element == null)
        {
            return -1;
        }

        String text = getText(element, cssQuery);

        if(text == null)
        {
            return -1;
        }
        else
        {
            return Integer.parseInt(text);
        }
    }

    public static Date getDate(Element element, String cssQuery)
    {
        if(element == null)
        {
            return null;
        }

        DateFormat df = new SimpleDateFormat("dd.mm.yyyy");

        String string = getText(element, cssQuery);

        if(string == null)
        {
            return null;
        }
        else
        {
            try {
                return df.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // --- Returns attributes ---

    public static String getURL(Element element, String cssQuery)
    {
        if(element == null)
        {
            return null;
        }

        Elements elements = element.select(cssQuery);

        if(elements.size() == 0)
        {
            return null;
        }

        return elements.attr("href");
    }
}
