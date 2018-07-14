package com.yellowbite.movienewsreminder2.webscraping;

import com.yellowbite.movienewsreminder2.model.Movie;
import com.yellowbite.movienewsreminder2.model.enums.Status;

import junit.framework.Assert;

import java.util.Date;

public final class WebscrapingTestHelper
{
    public static void assertMovie(Movie expected, Movie actual)
    {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEssentialMovie(int expectedMediaBarcode, String expectedLink, Movie actual)
    {
        Assert.assertEquals(new Movie(expectedMediaBarcode, expectedLink,
                null, -1, null, null, null, null, null, null),
                actual);
    }
}
