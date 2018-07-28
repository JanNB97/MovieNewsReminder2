package com.yellowbite.movienewsreminder2.webscraping;

import com.yellowbite.movienewsreminder2.model.Movie;

import junit.framework.Assert;

public final class WebscrapingTestHelper
{
    public static final String WRONG_URL = "https://lsdkjf.net//dsf.dsfjl//ldskjf.de";

    public static void assertMovie(Movie expected, Movie actual)
    {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEssentialMovie(Movie expected, Movie actual)
    {
        Assert.assertEquals(new Movie(expected.getMediaBarcode(), expected.getURL(),
                null, -1, null, null, null, null, null, null),
                actual);
    }

    public static void assertIndexOutOfBoundException(Runnable runnable)
    {
        try
        {
            runnable.run();
            Assert.fail();
        }
        catch (IndexOutOfBoundsException ignored) {}
    }
}
