package com.yellowbite.movienewsreminder2.webscraping;

import com.yellowbite.movienewsreminder2.data.Movie;
import com.yellowbite.movienewsreminder2.util.DateHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class WebscrapingHelper
{
    public static Document getDoc(String url) throws IOException
    {
        return Jsoup.connect(url).maxBodySize(0).get();
    }

    // --- search features --- --- ---
    public static String getNarrowSearchURL(String searchWord)
    {
        return "https://opac.winbiap.net/mzhr/search.aspx?q=\"" + getValidSearchString(searchWord) + "\" Spielfilm";
    }

    public static String getWideSearchURL(String searchWord)
    {
        return "https://opac.winbiap.net/mzhr/search.aspx?q=\"" + getValidSearchString(searchWord) + "\"";
    }

    public static String getValidSearchString(String searchWord)
    {
        searchWord = searchWord.replaceAll("ä", "ae");
        searchWord = searchWord.replaceAll("ü", "ue");
        searchWord = searchWord.replaceAll("ö", "oe");
        searchWord = searchWord.replaceAll("ß", "ss");

        return searchWord;
    }

    // --- Returning text ---

    public static Movie.Status getStatus(Element element, String cssQuery)
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
            for(Movie.Status status : Movie.Status.values())
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

        if(elements.isEmpty())
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

        String string = getText(element, cssQuery);

        if(string == null)
        {
            return null;
        }
        else
        {
            return DateHelper.toDate(cutToDate(string));
        }
    }

    private static String cutToDate(String string)
    {
        StringBuilder builder = null;

        int i = 0;
        for(char c : string.toCharArray())
        {
            if(c < 48 && c != 46 || c > 57 )
            {
                if(builder == null)
                {
                    builder = new StringBuilder(string);
                }

                builder.delete(i, i+1);
            }
            else
            {
                i++;
            }
        }

        if(builder == null)
        {
            return string;
        }
        else
        {
            return builder.toString();
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

        if(elements.isEmpty())
        {
            return null;
        }

        return elements.attr("href");
    }

    public static String getImageURL(Element element, String cssQuery)
    {
        if(element == null)
        {
            return null;
        }

        Elements elements = element.select(cssQuery);

        if(elements.isEmpty())
        {
            return null;
        }

        return elements.attr("data-src");
    }
}
