package com.yellowbite.movienewsreminder2.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebscrapingHelper
{
    public static Document getDoc(String url) throws IOException
    {
        return Jsoup.connect(url).maxBodySize(0).get();
    }
}
