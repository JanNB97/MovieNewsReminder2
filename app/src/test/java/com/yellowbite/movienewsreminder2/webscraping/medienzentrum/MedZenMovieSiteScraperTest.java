package com.yellowbite.movienewsreminder2.webscraping.medienzentrum;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.webscraping.WebscrapingTestHelper;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class MedZenMovieSiteScraperTest
{
    public static final String MED_ZEN_THREE_BILLBOARDS_SITE = "https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1adWdhbmdzZGF0dW0gKEJpYmxpb3RoZWspJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD02MyUlb18wPTYlJXZfMD0yNS4wOC4yMDE2IDAwOjAwOjAwKytjXzE9MSUlbV8xPTElJWZfMT00MiUlb18xPTElJXZfMT00NlMtRFZEIChTcGllbGZpbG0pKytjXzI9MSUlbV8yPTElJWZfMj00OCUlb18yPTElJXZfMj1NZWRpZW56ZW50cnVtIEhlcnNmZWxkLVJvdGVuYnVyZyZhbXA7Y21kPTEmYW1wO0NhdGFsb2d1ZUlkPTE4ODQyNyZhbXA7cGFnZUlkPTMmYW1wO1NyYz0zJmFtcDtwUz0xMA==-0GF/mWXQzOo=";
    public static final String WRONG_URL = "https://lsdkjf.net//dsf.dsfjl//ldskjf.de";

    @Test
    public void testWrongLink()
    {
        try
        {
            new MedZenMovieSiteScraper(WRONG_URL);
            Assert.fail();
        } catch (IOException ignored){}
    }

    @Test
    public void testGetEssentialMovie()
    {
        MedZenMovieSiteScraper siteScraper = null;
        try
        {
            siteScraper = new MedZenMovieSiteScraper(MED_ZEN_THREE_BILLBOARDS_SITE);
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Movie threeBillboards = siteScraper.getEssentialMovie();
        Assert.assertNotNull(threeBillboards);

        WebscrapingTestHelper.assertEssentialMovie(threeBillboards, 81018134, MED_ZEN_THREE_BILLBOARDS_SITE);
    }
}
